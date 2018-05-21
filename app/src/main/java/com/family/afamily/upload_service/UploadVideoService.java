package com.family.afamily.upload_service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.utils.Config;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hp2015-7 on 2018/1/24.
 */

public class UploadVideoService extends Service {
    // 服务是否在运行
    public static Boolean isServiceRunning = false;
    private String user_id;
    private OSSClient oss;
    private UploadDao uploadDao;
    private ConnectionChangeReceiver changeReceiver;//网络状态监听
    private MyRunnable myRunnable;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 3;
    private boolean isWifi = true;
    private boolean isStopAll = false;
    /**
     * 任务队列
     */
    private LinkedList<Map<Integer, Runnable>> mTaskQueue;

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        L.e("tag", "-----------服务启动---------------->");
        isServiceRunning = true;
        user_id = (String) SPUtils.get(getBaseContext(), "user_id", "");
        uploadDao = new UploadDao(getBaseContext());
        //注册网络监听
        changeReceiver = new ConnectionChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(changeReceiver, mFilter);
        // 创建线程池
        mThreadPool = Executors.newFixedThreadPool(DEAFULT_THREAD_COUNT);
        mTaskQueue = new LinkedList<>();

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.OSS_ACCESS_ID, Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(getApplicationContext(), Config.endpoint, credentialProvider);

    }


    /**
     * 构建任务
     *
     * @param videoData
     * @return
     */
    private Runnable buildTask(final UploadVideoData videoData) {
        return new Runnable() {
            @Override
            public void run() {
                pullFP(videoData.getFilePath(), videoData.getName(), videoData);
            }
        };
    }

    /**
     * 网络变化监听
     */
    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                // 网络连接
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    ///WiFi网络
                    isStopAll = true;
                    isWifi = true;
                    mThreadPool.shutdownNow();
                    mTaskQueue.clear();
                    if (myRunnable != null) {
                        handler.removeCallbacks(myRunnable);
                        myRunnable = null;
                    }
                    final List<UploadVideoData> list = uploadDao.getUploadList(user_id);
                    if (list == null || list.size() == 0)
                        return;
                    for (final UploadVideoData data : list) {
                        if (data.getUploadFlag() == 2) {
                            ContentValues values = new ContentValues();
                            values.put("upload_flag", 0);
                            uploadDao.updateMsm(values, data.getId());
                        }
                    }


                    L.e("tag", "-------------重新开始任务----------------->");
                    myRunnable = new MyRunnable();
                    handler.postDelayed(myRunnable, 1000);

                } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    //有线网络

                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifi = false;
                    //3g网络
                    final List<UploadVideoData> list = uploadDao.getUploadList(user_id);
                    for (final UploadVideoData data : list) {
                        if (data.getUploadFlag() == 0 || data.getUploadFlag() == 1 || data.getUploadFlag() == 4) {
                            ContentValues values = new ContentValues();
                            values.put("upload_flag", 2);
                            uploadDao.updateMsm(values, data.getId());
                        }
                    }
                    Intent b = new Intent("update_data");
                    sendBroadcast(b);
                    mThreadPool.shutdownNow();
                    mTaskQueue.clear();
                    if (myRunnable != null) {
                        handler.removeCallbacks(myRunnable);
                        myRunnable = null;
                    }
                    // mThreadPool = null;
                    L.e("tag", "---------------关闭线程-------->");
                }
            } else {
                //网络断开
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStopAll = true;
        isWifi = false;
        L.e("tag", "-------------重新开始任务----------------->");
        mThreadPool.shutdownNow();
        mTaskQueue.clear();
        if (myRunnable != null) {
            handler.removeCallbacks(myRunnable);
            myRunnable = null;
        }
        List<UploadVideoData> list = uploadDao.getUploadList(user_id);
        if (list != null && list.size() > 0) {
            myRunnable = new MyRunnable();
            handler.postDelayed(myRunnable, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopSelf();
                }
            }, 1000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            isStopAll = false;
            isWifi = Utils.isWifi(getApplicationContext());
            mTaskQueue.clear();
            final List<UploadVideoData> list = uploadDao.getUploadList(user_id);
            for (final UploadVideoData data : list) {
                if (data.getUploadFlag() == 0 || data.getUploadFlag() == 1 || data.getUploadFlag() == 4) {
                    Runnable ble = buildTask(data);
                    //addTask(data.getId(),ble);
                    if (mThreadPool == null || mThreadPool.isShutdown()) {
                        mThreadPool = Executors.newFixedThreadPool(DEAFULT_THREAD_COUNT);
                    }
                    mThreadPool.execute(ble);
                    // taskList.put(data.getId(),ble);
                }
            }
        }
    }


    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e("tag", "-----------服务停止------------------>");
        isServiceRunning = false;
        //退出时取消网络监听
        if (changeReceiver != null) {
            unregisterReceiver(changeReceiver);
        }
    }

    public void pullFP(final String filePath, final String name, final UploadVideoData videoData) {
        String uploadId = videoData.getUploadId();
        int current_index = 0;
        List<PartETag> partETags = new ArrayList<PartETag>(); // 保存分片上传的结果
        //之前有上传过
        if (!TextUtils.isEmpty(uploadId)) {
            L.e("tag", "----------之前有上传-------------->");
            ListPartsRequest listParts = new ListPartsRequest(Config.bucket, name, uploadId);
            ListPartsResult result = null;
            try {
                result = oss.listParts(listParts);
                current_index = result.getParts().size();
                L.e("tag", "----------之前有上传-----------上传--->" + current_index);
                for (int i = 0; i < result.getParts().size(); i++) {
                    String part = result.getParts().get(i).getETag();
                    part = part.replaceAll("\"", "");
                    partETags.add(new PartETag(result.getParts().get(i).getPartNumber(), part));
                }
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        } else {
            L.e("tag", "----------未上传过-------------->");
            InitiateMultipartUploadRequest init = new InitiateMultipartUploadRequest(Config.bucket, name);
            InitiateMultipartUploadResult initResult = null;
            try {
                initResult = oss.initMultipartUpload(init);
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            if (initResult == null) return;
            uploadId = initResult.getUploadId();

            videoData.setUploadId(uploadId);

            ContentValues values = new ContentValues();
            values.put("upload_Id", uploadId);
            values.put("upload_flag", 1);
            uploadDao.updateMsm(values, videoData.getId());
        }

        long partSize = 205 * 1024; // 设置分片大小
        // int currentIndex = 1; // 上传分片编号，从1开始
        File uploadFile = new File(filePath); // 需要分片上传的文件

        InputStream input = null;
        try {
            input = new FileInputStream(uploadFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long fileLength = uploadFile.length();
        long uploadedLength = 0;
        int end_index = (fileLength % partSize) > 0 ? 1 : 0;
        //总分片数量
        int total_Part = (int) ((fileLength / partSize) + end_index);

        // L.e("tag", "--file-->" + fileLength + "--total_Part-->" + total_Part + "----->" + end_index);
        uploadedLength = current_index * partSize;
        L.e("tag", current_index + "-------------current_index------------>" + total_Part);
        if (total_Part != current_index) {
            try {
                //跳过已上传位置
                input.skip(uploadedLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent("update_data_time");
            sendBroadcast(intent);

            // L.e("tag", "------------已上传->" + uploadedLength);
            for (int i = current_index; i < total_Part; i++) {
                if (isStopAll) {
                    L.e("tag", "--------------------停止所有->");
                    return;
                }
                if (videoData.getUploadFlag() != 4) {
                    if (!isWifi) {
                        L.e("tag", "--------------------当前不是wifi->");
                        return;
                    }
                }

                int partLength = (int) Math.min(partSize, fileLength - uploadedLength);
                byte[] partData = new byte[0]; // 按照分片大小读取文件的一段内容
                try {
                    partData = IOUtils.readStreamAsBytesArray(input, partLength);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UploadPartRequest uploadPart = new UploadPartRequest(Config.bucket, name, uploadId, (i + 1));
                uploadPart.setPartContent(partData); // 设置分片内容
                UploadPartResult uploadPartResult = null;
                try {
                    uploadPartResult = oss.uploadPart(uploadPart);
                    partETags.add(new PartETag((i + 1), uploadPartResult.getETag())); // 保存分片上传成功后的结果
                    uploadedLength += partLength;

                    ContentValues values = new ContentValues();
                    values.put("current_size", uploadedLength);
                    uploadDao.updateMsm(values, videoData.getId());
//                Intent intent = new Intent("update_data");
//                sendBroadcast(intent);
                    Log.e("currentIndex" + uploadPartResult.getETag(), (i + 1) + "---totalSize=" + fileLength + "----currentSize=" + uploadedLength);
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isStopAll) {
            L.e("tag", "--------------------停止所有->");
            return;
        }
        if (videoData.getUploadFlag() != 4) {
            if (!isWifi) {
                L.e("tag", "--------------------当前不是wifi->");
                return;
            }
        }

        for (int i = 0; i < partETags.size(); i++) {
            L.e("tag", partETags.get(i).getPartNumber() + "-----" + partETags.get(i).getETag());
        }

        CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(Config.bucket, name, uploadId, partETags);
        final CompleteMultipartUploadResult completeResult;
        try {
            completeResult = oss.completeMultipartUpload(complete);
            complete.setCallbackParam(new HashMap<String, String>() {
                {
                    Log.e("uploadEnd", "上传完成");
                    String token = (String) SPUtils.get(getBaseContext(), "token", "");
                    if (videoData.getFlag() == 1) {
                        submitData(token, completeResult.getLocation(), videoData.getTitle(), videoData.getType(), videoData);
                    } else {
                        submitVideo(token, videoData.getChild_id(), videoData.getTitle(), completeResult.getLocation(),
                                videoData.getCreate_time(), videoData.getAddress(), videoData);
                    }
                }
            });
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布早教视频
     *
     * @param token
     * @param url
     * @param title
     * @param id
     * @param videoData
     */
    public void submitData(String token, final String url, String title, String id, final UploadVideoData videoData) {
        // view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", id);
        params.put("intro", title);
        params.put("video_url", url);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_PUT_VIDEO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                L.e("tag", "-------发布失败，保存下次发布--------------->");
                ContentValues values = new ContentValues();
                values.put("upload_flag", 3);
                values.put("video_path", url);
                uploadDao.updateMsm(values, videoData.getId());

                Intent intent = new Intent("update_data");
                sendBroadcast(intent);
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                if (response == null || response.getRet_code() != 1) {
                    L.e("tag", "-------发布失败，保存下次发布--------------->");
                    ContentValues values = new ContentValues();
                    values.put("upload_flag", 3);
                    values.put("video_path", url);
                    uploadDao.updateMsm(values, videoData.getId());

                    Intent intent = new Intent("update_data");
                    sendBroadcast(intent);
                } else {
                    L.e("tag", "-------发布成功--------------->");
                    uploadDao.delData(videoData.getId());
                    Intent intent = new Intent("update_data");
                    sendBroadcast(intent);
                }
            }
        }, params);
    }

    /**
     * 发布视频
     *
     * @param token
     * @param child_id
     * @param content
     * @param video
     * @param create_time
     * @param address
     */
    public void submitVideo(String token, String child_id, String content, final String video, String create_time, String address, final UploadVideoData videoData) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("child_id", child_id);
        params.put("content", content);
        params.put("video", video);
        params.put("create_time", create_time);
        params.put("address", address);
        params.put("type", "2");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BABY_ISSUE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                L.e("tag", "-------发布失败，保存下次发布--------------->");
                ContentValues values = new ContentValues();
                values.put("upload_flag", 3);
                values.put("video_path", video);
                uploadDao.updateMsm(values, videoData.getId());

                Intent intent = new Intent("update_data");
                sendBroadcast(intent);
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                if (response == null || response.getRet_code() != 1) {
                    L.e("tag", "-------发布失败，保存下次发布--------------->");
                    ContentValues values = new ContentValues();
                    values.put("upload_flag", 3);
                    values.put("video_path", video);
                    uploadDao.updateMsm(values, videoData.getId());

                    Intent intent = new Intent("update_data");
                    sendBroadcast(intent);
                } else {
                    L.e("tag", "-------发布成功--------------->");
                    uploadDao.delData(videoData.getId());
                    Intent intent = new Intent("update_data");
                    sendBroadcast(intent);
                }
            }
        }, params);
    }

}
