package com.family.afamily.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ZaoJiaoDetailsView;
import com.family.afamily.activity.mvp.presents.ZaoJiaoDetailsPresenter;
import com.family.afamily.adapters.ZJCommcentAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.SampleListener;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.LandLayoutVideo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/6.
 */

public class ZaoJaoDetailsActivity extends BaseActivity<ZaoJiaoDetailsPresenter> implements ZaoJiaoDetailsView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.zaoj_title_ll)
    LinearLayout zaojTitleLl;
    TextView zaojDTitle;
    ImageView zaojDHeadIv;
    TextView zaojNickTv;
    TextView zaojTimeTv;
    ImageView zaojZanIv;
    TextView zaojZanCount;
    TextView zaojCommentCountTv;
    LinearLayout zaoJZanLl;
    //@BindView(R.id.zaoj_video_player)
    LandLayoutVideo zaojVideoPlayer;

    @BindView(R.id.zaoj_comment_list_rv)
    SuperRecyclerView zaojCommentListRv;
    @BindView(R.id.zaoj_comment_et)
    EditText zaojCommentEt;
    @BindView(R.id.zaoj_pic_iv)
    ImageView zaojPicIv;
    @BindView(R.id.zaoj_sc_iv)
    ImageView zaojScIv;
    @BindView(R.id.zaoj_follow_tv)
    TextView zaojFollowTv;
    @BindView(R.id.zaoj_bottom_ll)
    LinearLayout zaojBottomLl;


    private ZJCommcentAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private String id;
    private String token;
    private String study, str;
    private BasePageBean<Map<String, String>> commentData;
    private String collect;
    protected boolean isPlay;
    protected boolean isPause;
    protected OrientationUtils orientationUtils;
    private String current_user_id = "";
    private String oneself;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zaojiao_details);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            Utils.getStatusHeight(mActivity, findViewById(R.id.zaoj_title_ll));
//        }
        token = (String) SPUtils.get(mActivity, "token", "");
        id = getIntent().getStringExtra("id");
        study = getIntent().getStringExtra("study");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public ZaoJiaoDetailsPresenter initPresenter() {
        return new ZaoJiaoDetailsPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this,"视频详情");
        zaojCommentListRv.setLayoutManager(new LinearLayoutManager(this));
        zaojCommentListRv.setRefreshEnabled(false);// 可以定制是否开启下拉刷新
        zaojCommentListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        zaojCommentListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        zaojCommentListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        RecyclerViewDivider myDecoration = new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, 2, Color.parseColor("#f8f8f8"));
        zaojCommentListRv.addItemDecoration(myDecoration);
        adapter = new ZJCommcentAdapter(mActivity, list, new ZJCommcentAdapter.CommentItemClick() {
            @Override
            public void clickItem(String userName, String uid) {
                if (TextUtils.isEmpty(uid)) {
                    zaojCommentEt.setHint("评论");
                    zaojCommentEt.setTag("");
                } else {
                    zaojCommentEt.setTag(uid);
                    zaojCommentEt.setHint("回复" + userName);
                }
            }
        });
        zaojCommentListRv.setAdapter(adapter);
        addHead();

        if (TextUtils.isEmpty(id)) {
            toast("数据异常");
        } else {
            if (Utils.isConnected(mActivity)) {
                str = study.equalsIgnoreCase("N") ? "0" : "1";
                presenter.getData(token, id, str);
                presenter.getCommentList(token, id, 1, 1);
            }
        }

        zaojCommentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String uid = (String) zaojCommentEt.getTag();
                    presenter.submitComment(token, id, uid, zaojCommentEt.getText().toString(), null);
                    return true;
                }
                return false;
            }
        });

        zaojVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                zaojVideoPlayer.startWindowFullscreen(mActivity, true, true);
            }
        });

    }

    private void addHead(){
        View view =  getLayoutInflater().inflate(R.layout.head_zaojiao_details_layout, (ViewGroup) zaojCommentListRv.getParent(), false);
        zaojVideoPlayer = view.findViewById(R.id.zaoj_video_player);
        zaojDTitle = view.findViewById(R.id.zaoj_d_title);
        zaojDHeadIv = view.findViewById(R.id.zaoj_d_head_iv);
        zaojTimeTv = view.findViewById(R.id.zaoj_time_tv);
        zaojZanIv = view.findViewById(R.id.zaoj_zan_iv);
        zaojZanCount = view.findViewById(R.id.zaoj_zan_count);
        zaoJZanLl = view.findViewById(R.id.zaoj_zan_ll);
        zaojNickTv = view.findViewById(R.id.zaoj_nick_tv);
        zaojCommentCountTv = view.findViewById(R.id.zaoj_comment_count_tv);
        adapter.addHeaderView(view);

        zaojDHeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHead();
            }
        });
        zaojNickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHead();
            }
        });
        zaojTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHead();
            }
        });
        zaoJZanLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickZan();
            }
        });
    }

//    @OnClick({R.id.zaoj_d_head_iv, R.id.zaoj_nick_tv, R.id.zaoj_time_tv})
    public void clickHead() {
        if ("Y".equals(oneself)) {
            Utils.showMToast(mActivity, "不能查看自己主页");
        } else {
            Intent intent = new Intent(mActivity, MasterActivity.class);
            intent.putExtra("user_id", current_user_id);
            startActivityForResult(intent, 100);
        }
    }

    @OnClick(R.id.zaoj_sc_iv)
    public void clickCollect() {
        if (TextUtils.isEmpty(collect)) {
            toast("未获取到数据");
        } else if (collect.equals("Y")) {
            zaojScIv.setEnabled(false);
            presenter.submitCollect(token, id, "2");
        } else {
            zaojScIv.setEnabled(false);
            presenter.submitCollect(token, id, "1");
        }
    }

    @OnClick(R.id.zaoj_follow_tv)
    public void clickFollow() {
        String str = (String) zaojFollowTv.getTag();
        String user_id = (String) SPUtils.get(mActivity, "user_id", "");
        // L.e("tag",user_id + "------------->"+current_user_id);
        if (user_id.equals(current_user_id)) {
            toast("不能关注自己");
        } else {
            if (TextUtils.isEmpty(str)) {
                toast("未获取到数据");
            } else if (str.equals("Y")) {
                zaojFollowTv.setEnabled(false);
                presenter.submitFollow(token, current_user_id, "2");
            } else {
                zaojFollowTv.setEnabled(false);
                presenter.submitFollow(token, current_user_id, "1");
            }
        }
    }

  //  @OnClick(R.id.zaoj_zan_ll)
    public void clickZan() {
        String str = (String) zaojZanCount.getTag();
        if (TextUtils.isEmpty(str)) {
            toast("未获取到数据");
        } else if (str.equals("Y")) {
            zaojFollowTv.setEnabled(false);
            presenter.submitZan(token, id, "2");
        } else {
            zaojFollowTv.setEnabled(false);
            presenter.submitZan(token, id, "1");
        }
    }

    @OnClick(R.id.zaoj_pic_iv)
    public void clickCommentPic() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(480, 480)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void successData(Map<String, String> data) {
        if (data != null) {
            oneself = data.get("oneself");
            current_user_id = data.get("user_id");
            zaojDTitle.setText(data.get("intro"));
            zaojNickTv.setText(data.get("nick_name"));
            zaojTimeTv.setText(data.get("create_time") + " 播放" + data.get("look_count"));
            zaojZanCount.setText(data.get("like"));
            zaojCommentCountTv.setText("评论（" + data.get("comment_count") + "）");

            if (data.get("is_attention").equalsIgnoreCase("N")) {
                zaojFollowTv.setText("+关注");
                zaojFollowTv.setTag("N");
            } else {
                zaojFollowTv.setText("已关注");
                zaojFollowTv.setTag("Y");
            }

            if (data.get("is_collect").equalsIgnoreCase("N")) {
                zaojScIv.setImageResource(R.mipmap.ic_study_sc_1);
                collect = "N";
            } else {
                zaojScIv.setImageResource(R.mipmap.ic_sc_1);
                collect = "Y";
            }

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideCircleTransform(mActivity));
            Glide.with(mActivity).load(data.get("images")).apply(options).into(zaojDHeadIv);

            if (data.get("is_like").equalsIgnoreCase("N")) {
                zaojZanIv.setImageResource(R.mipmap.ic_follow);
                zaojZanCount.setTextColor(Color.parseColor("#999999"));
                zaojZanCount.setTag("N");
            } else {
                zaojZanIv.setImageResource(R.mipmap.ic_zan_i);
                zaojZanCount.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
                zaojZanCount.setTag("Y");
            }

            //播放视频
            playVideo(data.get("video_url"));
            // L.e("tag","-------------------->"+data.get("video_url"));
        }
    }

    @Override
    public void successCommentList(BasePageBean<Map<String, String>> data, int type) {
        if (data != null) {
            commentData = data;
            if (data.getList_data() != null && data.getList_data().size() > 0) {
                if (type == 1) {
                    list.clear();
                    list.addAll(data.getList_data());
                } else {
                    list.addAll(data.getList_data());
                    zaojCommentListRv.completeLoadMore();
                }
                adapter.notifyDataSetChanged();
            } else {
                if (type == 1) {
                    list.clear();
                } else {
                    zaojCommentListRv.completeLoadMore();
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            if (type == 1) {
                list.clear();
            } else {
                zaojCommentListRv.completeLoadMore();
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void submitCollectResult(int type) {
        zaojScIv.setEnabled(true);
        zaojFollowTv.setEnabled(true);
        if (type == 1) {
            presenter.getData(token, id, str);
        }
    }

    @Override
    public void successComment() {
        str = study.equalsIgnoreCase("N") ? "0" : "1";
        presenter.getData(token,id,str);
        presenter.getCommentList(token, id, 1, 1);
        zaojCommentEt.setText("");
        zaojCommentEt.setTag("");
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (commentData != null) {
                if (commentData.getPage() < commentData.getTotle_page()) {
                    presenter.getCommentList(token, id, commentData.getPage() + 1, 2);
                } else {
                    if (commentData.getTotle_page() == commentData.getPage()) {
                        zaojCommentListRv.setNoMore(true);
                    } else {
                        zaojCommentListRv.completeLoadMore();
                    }
                }
            }
        } else {
            zaojCommentListRv.completeLoadMore();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(mActivity);
        relaseVideo();
    }


    /**
     * 播放视频设置数据
     */
    private void playVideo(String url) {
        resolveNormalVideoUI();
        //增加封面
        ImageView imageView = new ImageView(this);
        Glide.with(mActivity).load(url + UrlUtils.VIDEO_6_6_PIC).into(imageView);
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, zaojVideoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
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
                }).build(zaojVideoPlayer);
        //播放视频
        // zaojVideoPlayer.startPlayLogic();
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
            zaojVideoPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    private void resolveNormalVideoUI() {
        //增加title
        zaojVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        zaojVideoPlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (zaojVideoPlayer.getFullWindowPlayer() != null) {
            return zaojVideoPlayer.getFullWindowPlayer();
        }
        return zaojVideoPlayer;
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
                            //  Log.e(media.getCompressPath());
                            path = media.getCompressPath();
                        } else {
                            // Log.e(media.getPath());
                            path = media.getPath();
                        }
                        if (TextUtils.isEmpty(path)) {
                            toast("获取图片路径失败");
                        } else {
                            File file = new File(path);
                            Log.e("tag", path);
                            String uid = (String) zaojCommentEt.getTag();

                            presenter.submitComment(token, id, uid, "", file);
                        }
                    }
                    break;
            }
        }
    }

}
