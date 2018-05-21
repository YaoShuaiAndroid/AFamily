package com.family.afamily.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ReleaseVideoView;
import com.family.afamily.activity.mvp.presents.ReleaseVideoPresenter;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.BaseDialog;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/7.
 */

public class ReleaseVideoActivity extends BaseActivity<ReleaseVideoPresenter> implements ReleaseVideoView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.release_add_video)
    RelativeLayout releaseAddVideo;
    @BindView(R.id.release_title_et)
    EditText releaseTitleEt;
    @BindView(R.id.release_type_sp)
    Spinner releaseTypeSp;
    @BindView(R.id.release_add_video_ll)
    LinearLayout releaseAddVideoLl;
    @BindView(R.id.release_player)
    LandLayoutVideo releasePlayer;
    @BindView(R.id.release_close_iv)
    ImageView releaseCloseIv;
    @BindView(R.id.release_preview_rl)
    RelativeLayout releasePreviewRl;
    @BindView(R.id.video_size_tv)
    TextView videoSizeTv;

    private String token;
    private ArrayAdapter<String> adapter;

    private List<Map<String, String>> typeList;

    protected boolean isPlay;
    protected boolean isPause;
    protected OrientationUtils orientationUtils;
    private File videoFile = null;

    // private List<UploadVideoData> uploadData;
    private UploadDao uploadDao;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_video);
        token = (String) SPUtils.get(mActivity, "token", "");
        //  uploadData = (List<UploadVideoData>) Utils.load(FileUtile.uploadPath(this));
    }

    @Override
    public void netWorkConnected() {

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
        String title = releaseTitleEt.getText().toString();
        if(typeList == null||typeList.isEmpty()){
            toast("未获取到分类列表");
            return;
        }
        String str = releaseTypeSp.getSelectedItem().toString();
        if (TextUtils.isEmpty(title)) {
            toast("请输入视频标题");
        } else if (TextUtils.isEmpty(str)) {
            toast("未获取到视频分类,正在重新获取...");
            presenter.getTypeList(token);
        } else if (videoFile == null) {
            toast("请选择一个视频");
        } else {
            float size = Utils.getFileSize(videoFile);
            if (size < 100) {
                if (Utils.isWifi(mActivity)) {
                    int i = releaseTypeSp.getSelectedItemPosition();
                    String id = typeList.get(i).get("id");
                    String user_id = (String) SPUtils.get(mActivity, "user_id", "");
                    UploadVideoData videoData = new UploadVideoData();
                    videoData.setUserId(user_id);
                    videoData.setTitle(title);
                    videoData.setCurrentIndex(0);
                    videoData.setFlag(1);
                    videoData.setType(id);
                    videoData.setUploadFlag(0);
                    videoData.setName(videoFile.getName());
                    videoData.setFilePath(videoFile.getPath());
                    videoData.setTotalSize(Utils.getFileSizeByte(videoFile));
                    videoData.setCurrentSize(0);

                    uploadDao = new UploadDao(mActivity);
                    uploadDao.insertDB(videoData);
                    Intent intent = new Intent(mActivity, MyVideoActivity.class);
                    intent.putExtra("index", 4);
                    startActivity(intent);
                    finish();

                    Intent service = new Intent(mActivity, UploadVideoService.class);
                    startService(service);
                } else {
                    showDeleteDialog(title);
                }

                // pullFP(videoFile.getPath(), videoFile.getName(), title, id);
            } else {
                toast("上传视频不能大于100M");
            }
        }
    }


    private void showDeleteDialog(final String title) {
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

                        int i = releaseTypeSp.getSelectedItemPosition();
                        String id = typeList.get(i).get("id");
                        String user_id = (String) SPUtils.get(mActivity, "user_id", "");
                        UploadVideoData videoData = new UploadVideoData();
                        videoData.setUserId(user_id);
                        videoData.setTitle(title);
                        videoData.setCurrentIndex(0);
                        videoData.setFlag(1);
                        videoData.setType(id);
                        videoData.setUploadFlag(0);
                        videoData.setName(videoFile.getName());
                        videoData.setFilePath(videoFile.getPath());
                        videoData.setTotalSize(Utils.getFileSizeByte(videoFile));
                        videoData.setCurrentSize(0);

                        uploadDao = new UploadDao(mActivity);
                        uploadDao.insertDB(videoData);
                        Intent intent = new Intent(mActivity, MyVideoActivity.class);
                        intent.putExtra("index", 4);
                        startActivity(intent);
                        finish();

                        Intent service = new Intent(mActivity, UploadVideoService.class);
                        startService(service);

                    }
                });
            }
        };
    }

    @Override
    public ReleaseVideoPresenter initPresenter() {
        return new ReleaseVideoPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "发布视频");
        baseTitleRightTv.setText("提交");
        if (Utils.isConnected(mActivity)) {
            presenter.getTypeList(token);
        }
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
    public void submitSuccess() {
        finish();
    }

    @Override
    public void successTypeData(List<Map<String, String>> data) {
        if (data != null && data.size() > 0) {
            typeList = data;
            String item[] = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                item[i] = data.get(i).get("type");
            }
            // 建立Adapter并且绑定数据源
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //绑定 Adapter到控件
            releaseTypeSp.setAdapter(adapter);
        }
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
        }
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
