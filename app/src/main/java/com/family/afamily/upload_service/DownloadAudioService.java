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

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.Range;
import com.family.afamily.entity.AudioData;
import com.family.afamily.upload_db.AudioDao;
import com.family.afamily.utils.Config;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hp2015-7 on 2018/1/26.
 */

public class DownloadAudioService extends Service {
    // 服务是否在运行
    public static Boolean isServiceRunning = false;
    private ConnectionChangeReceiver changeReceiver;//网络状态监听
    private MyRunnable myRunnable;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 3;
    private boolean isWifi = true;
    private boolean isStopAll = false;
    private AudioDao audioDao;
    private String user_id;
    private OSSClient oss;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
        user_id = (String) SPUtils.get(getBaseContext(), "user_id", "");
        audioDao = new AudioDao(getBaseContext());
        //注册网络监听
        changeReceiver = new ConnectionChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(changeReceiver, mFilter);
        // 创建线程池
        mThreadPool = Executors.newFixedThreadPool(DEAFULT_THREAD_COUNT);
        mTaskQueue = new LinkedList<>();

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(com.family.afamily.utils.Config.OSS_ACCESS_ID, com.family.afamily.utils.Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(getApplicationContext(), com.family.afamily.utils.Config.endpoint, credentialProvider);

    }

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
        List<AudioData> list = audioDao.getAudioList(user_id);
        if (list != null && list.size() > 0) {
            myRunnable = new MyRunnable();
            handler.postDelayed(myRunnable, 1000);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 构建任务
     *
     * @param videoData
     * @return
     */
    private Runnable buildTask(final AudioData videoData) {
        return new Runnable() {
            @Override
            public void run() {
                ossDownload(videoData);
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
                    final List<AudioData> list = audioDao.getAudioList(user_id);
                    if (list == null || list.size() == 0)
                        return;
                    for (final AudioData data : list) {
                        if (data.getDownloadFlag() == 2) {
                            ContentValues values = new ContentValues();
                            values.put("download_flag", 1);
                            audioDao.updateMsm(values, data.getId());
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
                    final List<AudioData> list = audioDao.getAudioList(user_id);
                    for (final AudioData data : list) {
                        if (data.getDownloadFlag() == 1 || data.getDownloadFlag() == 4) {
                            ContentValues values = new ContentValues();
                            values.put("download_flag", 2);
                            audioDao.updateMsm(values, data.getId());
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


    class MyRunnable implements Runnable {
        @Override
        public void run() {
            isStopAll = false;
            isWifi = Utils.isWifi(getApplicationContext());
            mTaskQueue.clear();
            final List<AudioData> list = audioDao.getAudioList(user_id);
            for (final AudioData data : list) {
                if (data.getDownloadFlag() == 1 || data.getDownloadFlag() == 4) {
                    Runnable ble = buildTask(data);
                    if (mThreadPool == null || mThreadPool.isShutdown()) {
                        mThreadPool = Executors.newFixedThreadPool(DEAFULT_THREAD_COUNT);
                    }
                    mThreadPool.execute(ble);
                }
            }
        }
    }

    private void ossDownload(final AudioData audioData) {
        try {
            final RandomAccessFile raf = new RandomAccessFile(audioData.getAudioPath(), "rwd");
            String url = audioData.getAudioDownload();
            GetObjectRequest get = new GetObjectRequest(Config.bucket, getFileName(url));
            L.e("tag", "------->" + Config.bucket +"--------->"+getFileName(url));
            // 设置范围
            L.e("tag", "---------------已下载--->" + raf.length());
            get.setRange(new Range(raf.length(), Range.INFINITE)); // 下载指定位置到结尾
            final OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                @Override
                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                    try {
                        long lastStart = raf.length();
                        ContentValues values = new ContentValues();
                        L.e("tag", "----------------总大小-->" + result.getContentLength());
                        values.put("total_size", (int) result.getContentLength() + lastStart);
                        audioDao.updateMsm(values, audioData.getId());
                        Intent intent = new Intent("update_audio_data");
                        sendBroadcast(intent);
                        // 请求成功
                        InputStream inputStream = result.getObjectContent();
                        raf.seek(lastStart);
                        final byte[] bf = new byte[1024 * 4];
                        int len = -1;
                        while (true) {
                            if (isStopAll) {
                                L.e("tag", "--------------------停止所有->");
                                return;
                            }
                            if (audioData.getDownloadFlag() != 4) {
                                if (!isWifi) {
                                    L.e("tag", "--------------------当前不是wifi->");
                                    return;
                                }
                            }
                            len = inputStream.read(bf);
                            if (len == -1) {
                                break;
                            }
                            raf.write(bf, 0, len);
                            lastStart += len;
                            L.e("tag", "----------------下载进度----》" + lastStart);
                            ContentValues values2 = new ContentValues();
                            values2.put("current_size", lastStart);
                            audioDao.updateMsm(values2, audioData.getId());
                        }
                        if (isStopAll) {
                            L.e("tag", "--------------------停止所有->");
                            return;
                        }
                        if (audioData.getDownloadFlag() != 4) {
                            if (!isWifi) {
                                L.e("tag", "--------------------当前不是wifi->");
                                return;
                            }
                        }
                        ContentValues values2 = new ContentValues();
                        values2.put("download_flag", 3);
                        audioDao.updateMsm(values2, audioData.getId());
                        L.e("tag", "--------下载完成----->");
                        Intent intent1 = new Intent("close_audio_data");
                        sendBroadcast(intent1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        if (isWifi) {
                            clientExcepion.printStackTrace();
                            ContentValues values2 = new ContentValues();
                            values2.put("download_flag", 5);
                            audioDao.updateMsm(values2, audioData.getId());
                        }
                    }
                    if (serviceException != null) {
                        // 服务异常
//                        Log.e("ErrorCode", serviceException.getErrorCode());
//                        Log.e("RequestId", serviceException.getRequestId());
//                        Log.e("HostId", serviceException.getHostId());
//                        Log.e("RawMessage", serviceException.getRawMessage());
                        ContentValues values2 = new ContentValues();
                        values2.put("download_flag", 6);
                        values2.put("current_size", 1);
                        audioDao.updateMsm(values2, audioData.getId());
                        File file = new File(audioData.getAudioPath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String path) {
        int separatorIndex = path.indexOf("/", 20);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
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

}
