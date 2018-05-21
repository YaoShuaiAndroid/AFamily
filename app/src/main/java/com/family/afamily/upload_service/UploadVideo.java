package com.family.afamily.upload_service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.utils.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/22.
 */

public class UploadVideo {
    //视频路径
    private String filePath;
    //上传内容
    private UploadVideoData videoData;

    private OSSClient oss;
    private OSSAsyncTask task;
    private Context context;
    private String uploadId;
    private int current_index = 0;

    public UploadVideo(Context context, UploadVideoData videoData) {
        this.videoData = videoData;
        this.context = context;
        filePath = videoData.getFilePath();
        uploadId = videoData.getUploadId();

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.OSS_ACCESS_ID, Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(context, Config.endpoint, credentialProvider);

    }

    public void pullFP(final String filePath, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //之前有上传过
                if (!TextUtils.isEmpty(uploadId)) {
                    ListPartsRequest listParts = new ListPartsRequest(Config.bucket, name, uploadId);
                    ListPartsResult result = null;
                    try {
                        result = oss.listParts(listParts);
                        current_index = result.getParts().size() - 1;
                        for (int i = 0; i < result.getParts().size(); i++) {
                            Log.e("已上传分片", "partNum: " + result.getParts().get(i).getPartNumber());
                            Log.e("已上传分片", "partEtag: " + result.getParts().get(i).getETag());
                            Log.e("已上传分片", "lastModified: " + result.getParts().get(i).getLastModified());
                            Log.e("已上传分片", "partSize: " + result.getParts().get(i).getSize());
                        }
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                } else {
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
                List<PartETag> partETags = new ArrayList<PartETag>(); // 保存分片上传的结果

                int end_index = fileLength % partSize < 0 ? 1 : 0;
                //总分片数量
                int total_Part = (int) ((fileLength / partSize) + end_index);

                for (int i = current_index; i < total_Part; i++) {
//                    当前上传长度
                    uploadedLength = i * partSize;

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
                            // mActivity.runOnUiThread(new Runnable() {
                            //    @Override
                            //   public void run() {
                            // hideProgress();
                            Log.e("uploadEnd", "上传完成");
                            // presenter.submitData(token, completeResult.getLocation(), title, type);
                            // Log.e("multipartUpload", "multipart upload success! Location: " + completeResult.getLocation());
                            //  }
                            // });

                        }
                    });

                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //删除上传
    public void delete(String name) {
        AbortMultipartUploadRequest abort = new AbortMultipartUploadRequest(Config.bucket, name, uploadId);
        try {
            oss.abortMultipartUpload(abort);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
