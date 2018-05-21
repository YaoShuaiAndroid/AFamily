package com.family.afamily.upload_service;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
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
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/24.
 */

public class UploadAsynTask extends AsyncTask<UploadVideoData, Integer, Integer> {

    private Activity mActivity;
    // private ProgressBar progressBar;
    private String user_id;
    private OSSClient oss;
    private UploadDao uploadDao;
    private UpdateData updateData;

    public UploadAsynTask(Activity mActivity) {
        this.mActivity = mActivity;
        //  this.progressBar = progressBar;

        user_id = (String) SPUtils.get(mActivity, "user_id", "");
        uploadDao = new UploadDao(mActivity);
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.OSS_ACCESS_ID, Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mActivity, Config.endpoint, credentialProvider);

    }

    public UpdateData getUpdateData() {
        return updateData;
    }

    public void setUpdateData(UpdateData updateData) {
        this.updateData = updateData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        L.e("tag", "开始任务---------》");
    }

    @Override
    protected Integer doInBackground(UploadVideoData... params) {
        for (UploadVideoData video : params) {
            pullFP(video.getFilePath(), video.getName(), video);
        }

        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        L.e("tag", "更新中---------》");

//        Integer [] v = values;
//        if(v!=null&&v.length>1){
//            progressBar.setMax(v[0]);
//            progressBar.setProgress(v[1]);
//        }


    }

    @Override
    protected void onCancelled(Integer integer) {
        super.onCancelled(integer);
        L.e("tag", "任务结束---------》");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        L.e("tag", "取消任务---------》");
    }

    public void pullFP(final String filePath, final String name, final UploadVideoData videoData) {
        String uploadId = videoData.getUploadId();
        int current_index = 0;
        int mCurrent = 0;
        List<PartETag> partETags = new ArrayList<PartETag>(); // 保存分片上传的结果
        //之前有上传过
        if (!TextUtils.isEmpty(uploadId)) {
            L.e("tag", "----------之前有上传-------------->" + updateData);
            ListPartsRequest listParts = new ListPartsRequest(Config.bucket, name, uploadId);
            ListPartsResult result = null;
            try {
                result = oss.listParts(listParts);
                mCurrent = result.getParts().size();
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
            L.e("tag", "----------未上传过-------------->" + updateData);
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

//            List<UploadVideoData> videoDataList = uploadDao.getUploadList(user_id);
//            videoDataList.set(index, videoData);
            ContentValues values = new ContentValues();
            values.put("upload_Id", uploadId);
            uploadDao.updateMsm(values, videoData.getId());

        }

        long partSize = 512 * 1024; // 设置分片大小
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

        uploadedLength = mCurrent * partSize;
        if (updateData != null) {
            updateData.updateTotalSize(fileLength, uploadedLength);
        }
        //  onProgressUpdate((int)fileLength,(int)uploadedLength);

        try {
            //跳过已上传位置
            input.skip(uploadedLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.e("tag", "------------已上传->" + uploadedLength);
        for (int i = current_index; i < total_Part; i++) {
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
                if (updateData != null) {
                    updateData.updateTotalSize(fileLength, uploadedLength);
                }
                // onProgressUpdate((int)fileLength,(int)uploadedLength);
                Log.e("currentIndex" + uploadPartResult.getETag(), (i + 1) + "---totalSize=" + fileLength + "----currentSize=" + uploadedLength);
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(Config.bucket, name, uploadId, partETags);
        final CompleteMultipartUploadResult completeResult;
        try {
            completeResult = oss.completeMultipartUpload(complete);
            complete.setCallbackParam(new HashMap<String, String>() {
                {
                    Log.e("uploadEnd", "上传完成");
                    if (videoData.getFlag() == 1) {
                        String token = (String) SPUtils.get(mActivity, "token", "");
                        submitData(token, completeResult.getLocation(), videoData.getTitle(), videoData.getType(), videoData);
                    }
                }
            });
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


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
                if (updateData != null) {
                    updateData.updateData();
                }
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                if (response == null || response.getRet_code() != 1) {
                } else {
                    L.e("tag", "-------发布成功--------------->");
                    uploadDao.delData(videoData.getId());
                    if (updateData != null) {
                        updateData.updateData();
                    }

                }
            }
        }, params);
    }

    public interface UpdateData {
        void updateData();

        void updateTotalSize(long total, long current);
    }
}
