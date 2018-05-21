package com.family.afamily.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BabyIssueView;
import com.family.afamily.activity.mvp.presents.BabyIssuePresenter;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.Config;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.SampleListener;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.LandLayoutVideo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/14.
 */

public class BabyIssueVideoActivity extends BaseActivity<BabyIssuePresenter> implements BabyIssueView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.issue_video_content)
    EditText issueVideoContent;
    @BindView(R.id.issue_video_time)
    TextView issueVideoTime;
    @BindView(R.id.issue_video_time_rl)
    RelativeLayout issueVideoTimeRl;
    @BindView(R.id.issue_video_address)
    TextView issueVideoAddress;
    @BindView(R.id.issue_video_address_rl)
    RelativeLayout issueVideoAddressRl;
    @BindView(R.id.video_size_tv)
    TextView videoSizeTv;
    @BindView(R.id.release_player)
    LandLayoutVideo releasePlayer;
    @BindView(R.id.release_close_iv)
    ImageView releaseCloseIv;
    @BindView(R.id.release_preview_rl)
    RelativeLayout releasePreviewRl;
    @BindView(R.id.release_add_video)
    RelativeLayout releaseAddVideo;
    @BindView(R.id.release_add_video_ll)
    LinearLayout releaseAddVideoLl;

    private String id;
    private String token;
    private ArrayAdapter<String> adapter;
    private OSSClient oss;
    private OSSAsyncTask task;
    private List<Map<String, String>> typeList;

    protected boolean isPlay;
    protected boolean isPause;
    protected OrientationUtils orientationUtils;
    private File videoFile = null;
    private int address_index = -1;
    private String address_str = "不显示位置";
    private UploadDao uploadDao;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_issue_video);
        token = (String) SPUtils.get(mActivity, "token", "");
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)) {
            toast("宝贝ID有误");
            finish();
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public BabyIssuePresenter initPresenter() {
        return new BabyIssuePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "宝宝新变化");
        baseTitleRightTv.setText("发布");

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.OSS_ACCESS_ID, Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(getApplicationContext(), Config.endpoint, credentialProvider);

        //-----------------------------
        releasePlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                releasePlayer.startWindowFullscreen(mActivity, true, true);
            }
        });
    }

    /**
     * 选择时间
     */
    @OnClick(R.id.issue_video_time_rl)
    public void clickTime() {
        presenter.showDateDialog(issueVideoTime);
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.issue_video_address_rl)
    public void clickAddress() {
        Intent intent = new Intent(mActivity, NearbySiteActivity.class);
        intent.putExtra("position", address_index);
        intent.putExtra("address", address_str);
        startActivityForResult(intent, 100);
    }

    /**
     * 添加视频
     */
    @OnClick(R.id.release_add_video_ll)
    public void clickAddVideo() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 删除已选视频
     */
    @OnClick(R.id.release_close_iv)
    public void clickCloseVideo() {
        releasePreviewRl.setVisibility(View.GONE);
        releaseAddVideoLl.setVisibility(View.VISIBLE);
        videoSizeTv.setVisibility(View.GONE);
        videoSizeTv.setText("");
        videoFile = null;
        if (isPlay) {
            getCurPlay().release();
        }
    }

    /**
     * 提示视频发布
     */
    @OnClick(R.id.base_title_right_tv)
    public void clickSubmit() {
        String title = issueVideoContent.getText().toString();
        String time = issueVideoTime.getText().toString();
        String address = issueVideoAddress.getText().toString();
        String addre = TextUtils.isEmpty(address) ? "不显示地址" : address;
        if (TextUtils.isEmpty(title)) {
            toast("请输入视频描述");
        } else if (TextUtils.isEmpty(time)) {
            toast("请选择视频拍摄时间");
        } else if (videoFile == null) {
            toast("请选择一个视频");
        } else {
//            showProgress(2);
//            pullFP(videoFile.getPath(), videoFile.getName(), title, time, addre);

            float size = Utils.getFileSize(videoFile);
            if (size < 100) {
                if (Utils.isWifi(mActivity)) {
                    String user_id = (String) SPUtils.get(mActivity, "user_id", "");
                    UploadVideoData videoData = new UploadVideoData();
                    videoData.setUserId(user_id);
                    videoData.setTitle(title);
                    videoData.setCurrentIndex(0);
                    videoData.setFlag(2);
                    videoData.setUploadFlag(0);
                    videoData.setName(videoFile.getName());
                    videoData.setFilePath(videoFile.getPath());
                    videoData.setTotalSize(Utils.getFileSizeByte(videoFile));
                    videoData.setCurrentSize(0);
                    videoData.setChild_id(id);
                    videoData.setAddress(addre);
                    videoData.setCreate_time(time);
                    uploadDao = new UploadDao(mActivity);
                    uploadDao.insertDB(videoData);

                    Intent service = new Intent(mActivity, UploadVideoService.class);
                    startService(service);

                    Intent intent = new Intent(mActivity, MyVideoActivity.class);
                    intent.putExtra("index", 4);
                    startActivity(intent);
                    finish();
                } else {
                    showDeleteDialog(title, time, addre);
                }
                // pullFP(videoFile.getPath(), videoFile.getName(), title, id);
            } else {
                toast("上传视频不能大于100M");
            }
        }
    }


    private void showDeleteDialog(final String title, final String time, final String addre) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("当前网络不是WIFI状态下是否继续发布？");
                dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        String user_id = (String) SPUtils.get(mActivity, "user_id", "");
                        UploadVideoData videoData = new UploadVideoData();
                        videoData.setUserId(user_id);
                        videoData.setTitle(title);
                        videoData.setCurrentIndex(0);
                        videoData.setFlag(2);
                        videoData.setUploadFlag(0);
                        videoData.setName(videoFile.getName());
                        videoData.setFilePath(videoFile.getPath());
                        videoData.setTotalSize(Utils.getFileSizeByte(videoFile));
                        videoData.setCurrentSize(0);
                        videoData.setChild_id(id);
                        videoData.setAddress(addre);
                        videoData.setCreate_time(time);
                        uploadDao = new UploadDao(mActivity);
                        uploadDao.insertDB(videoData);

                        Intent service = new Intent(mActivity, UploadVideoService.class);
                        startService(service);

                        Intent intent = new Intent(mActivity, MyVideoActivity.class);
                        intent.putExtra("index", 4);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        };
    }

    @Override
    public void successData() {
        setResult(100);
        finish();
    }


    /**
     * 播放视频设置数据
     */
    private void playVideo(String url) {
        //增加封面
        resolveNormalVideoUI();
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, releasePlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setStandardVideoAllCallBack(new SampleListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        Debuger.printfError("***** onPrepared **** " + objects[0]);
                        Debuger.printfError("***** onPrepared **** " + objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                }).build(releasePlayer);
        //播放视频
        // releasePlayer.startPlayLogic();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
                        String path;
                        if (media.isCompressed()) {
                            path = media.getCompressPath();
                        } else if (media.isCut()) {
                            path = media.getCutPath();
                        } else {
                            path = media.getPath();
                        }
                        videoFile = new File(path);
                        // uploadVideo(oss, file.getName(), path);
                        releasePreviewRl.setVisibility(View.VISIBLE);
                        releaseAddVideoLl.setVisibility(View.GONE);
                        playVideo(path);
                        videoSizeTv.setVisibility(View.VISIBLE);
                        videoSizeTv.setText("视频大小：" + Utils.floatFormat(Utils.getFileSize(videoFile)) + "MB");
                    }
                    break;
            }
        } else if (requestCode == 100 && resultCode == 100) {
            if (data != null) {
                address_index = data.getIntExtra("position", -1);
                address_str = data.getStringExtra("address");
                issueVideoAddress.setText(address_str);
            }
        }
    }

    /**
     * 分片上传
     **/
    public void pullFP(final String filePath, final String name, final String title, final String time, final String address) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String uploadId;
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
                String requestud = initResult.getRequestId();

                long partSize = 512 * 1024; // 设置分片大小
                int currentIndex = 1; // 上传分片编号，从1开始
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
                while (uploadedLength < fileLength) {
                    int partLength = (int) Math.min(partSize, fileLength - uploadedLength);
                    byte[] partData = new byte[0]; // 按照分片大小读取文件的一段内容
                    try {
                        partData = IOUtils.readStreamAsBytesArray(input, partLength);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadPartRequest uploadPart = new UploadPartRequest(Config.bucket, name, uploadId, currentIndex);
                    uploadPart.setPartContent(partData); // 设置分片内容
                    UploadPartResult uploadPartResult = null;
                    try {
                        uploadPartResult = oss.uploadPart(uploadPart);
                        partETags.add(new PartETag(currentIndex, uploadPartResult.getETag())); // 保存分片上传成功后的结果
                        uploadedLength += partLength;
                        currentIndex++;
                        Log.e("currentIndex" + uploadPartResult.getETag(), currentIndex + "---totalSize=" + fileLength + "----currentSize=" + uploadedLength);
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
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // hideProgress();
                                    Log.e("uploadEnd", "上传完成");
                                    presenter.submitVideo(token, id, title, completeResult.getLocation(), time, address);
                                    Log.e("multipartUpload", "multipart upload success! Location: " + completeResult.getLocation());
                                }
                            });

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

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
        }
        relaseVideo();
    }

    private void relaseVideo() {
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            releasePlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    private void resolveNormalVideoUI() {
        //增加title
        releasePlayer.getTitleTextView().setVisibility(View.GONE);
        releasePlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (releasePlayer.getFullWindowPlayer() != null) {
            return releasePlayer.getFullWindowPlayer();
        }
        return releasePlayer;
    }

}
