package com.family.afamily.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.InnateDetailView;
import com.family.afamily.activity.mvp.presents.ConductPresenter;
import com.family.afamily.activity.mvp.presents.InnateDetailPresenter;
import com.family.afamily.adapters.DetectCommentAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.CommentModel;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.GlideCircleImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.SampleListener;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.LandLayoutVideo;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bt on 2018/4/14.
 */

public class ConductDetailActivity extends BaseActivity<ConductPresenter> implements SuperRecyclerView.LoadingListener,InnateDetailView {
    @BindView(R.id.innate_comment_recy)
    SuperRecyclerView superRecyclerView;
    @BindView(R.id.comment_commit)
    EditText commentCommit;

    private DetectCommentAdapter adapter;

    private List<CommentModel> mList = new ArrayList<>();

    private String id;

    private int page=1;
    private int pages=1;

    private boolean isLike=false;
    //被评论人id
    private String commentId;
    protected OrientationUtils orientationUtils;

    @Override
    public void netWorkConnected() {

    }

    @Override
    public ConductPresenter initPresenter() {
        return new ConductPresenter(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_innate_detail);

        id=getIntent().getStringExtra("id");

        detailsFacultys();

        getData(1);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        superRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        superRecyclerView.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        superRecyclerView.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        superRecyclerView.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        superRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        superRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        superRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new DetectCommentAdapter(mActivity, mList);
        superRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                commentId=mList.get(position-1).getId();
                commentCommit.setHint("@"+mList.get(position-1).getNick_name()+":");
            }
        });

        addHeadView();

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

    private LinearLayout innateDetailLikeLin;
    private TextView innateDetailLikeNum,innateDetailCommentNum;
    private ImageView innateDetailLikeImg,conductPlayImg;
    private WebView innateDetailWebview;
    private Banner mBanner;
    private LandLayoutVideo zaojVideoPlayer;

    private void addHeadView() {
        View headView = getLayoutInflater()
                .inflate(R.layout.header_innate_detail_item, (ViewGroup) superRecyclerView.getParent(), false);
        zaojVideoPlayer = headView.findViewById(R.id.zaoj_video_player);
        innateDetailLikeLin=headView.findViewById(R.id.innate_detail_like_lin);
        innateDetailLikeNum=headView.findViewById(R.id.innate_detail_like_num);
        innateDetailLikeImg=headView.findViewById(R.id.innate_detail_like_img);
        innateDetailWebview=headView.findViewById(R.id.innate_detail_webview);
        conductPlayImg=headView.findViewById(R.id.conduct_play_img);
        innateDetailCommentNum=headView.findViewById(R.id.innate_detail_comment_num);
        mBanner=headView.findViewById(R.id.mBanner);

        innateDetailLikeLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLike();

                innateDetailLikeLin.setEnabled(false);
            }
        });

        adapter.addHeaderView(headView);
    }

    /**
     * 评论列表
     * @param type
     */
    public void getData(int type) {
        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getData(id,page,type);
        } else {
            toast("网络异常");
        }
    }

    /**
     * 点赞
     */
    public void submitLike() {
        String token= (String) SPUtils.get(mActivity,"token","");

        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mActivity,LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.submitLike(id,token);
        } else {
            toast("网络异常");
        }
    }

    /**
     * 先天智能文章详情
     */
    public void detailsFacultys() {
        String user_id= (String) SPUtils.get(mActivity,"user_id","");

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.detailsFacultys(id,user_id);
        } else {
            toast("网络异常");
        }
    }

    /**
     * 评论
     */
    public void earlyFacultyComment(String content) {
        String token= (String) SPUtils.get(mActivity,"token","");

        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mActivity,LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.earlyFacultyComment(id,token,content,commentId);
        } else {
            toast("网络异常");
        }
    }

    @OnClick({R.id.comment_commit_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.comment_commit_input:
                String text=commentCommit.getText().toString();

                if(TextUtils.isEmpty(text)){
                    toast("请填写评论内容");
                    return;
                }

                earlyFacultyComment(text);
                break;
        }
    }

    @Override
    public void successData(BasePageBean<CommentModel> data, int type) {
        if (data != null) {
            if(type==1){
                this.mList.addAll(data.getReview());
            }else if (type == 2) {
                this.mList.clear();
                this.mList.addAll(data.getReview());
                superRecyclerView.completeRefresh();
            }else if (type == 3) {
                this.mList.addAll(data.getReview());
                superRecyclerView.completeRefresh();
            }

            pages=data.getPages();
            page=data.getPage();

            adapter.notifyDataSetChanged();
        }
    }

    private int comments=0;
    private InnateIntelligenceModel data;
    @Override
    public void successDetailData(InnateIntelligenceModel data) {
        this.data=data;
        if(!TextUtils.isEmpty(data.getComments())){
            innateDetailCommentNum.setText("评论("+data.getComments()+")");
            comments=Integer.parseInt(data.getComments());
        }
        innateDetailWebview.loadDataWithBaseURL(null,
                AppUtil.css(data.getContent()), "text/html", "utf-8", null);
        setTitle(mActivity,data.getTitle());

        if(!TextUtils.isEmpty(data.getVideo_url())){
            if(data.getImgs()!=null&&data.getImgs().size()>0){
                setBanner(data.getImgs().subList(0,1));
            }

            conductPlayImg.setVisibility(View.VISIBLE);
        }else if(data.getCarousels()!=null){
            setBanner(data.getCarousels());
        }else if(data.getImgs()!=null){
            setBanner(data.getImgs());
        }

        if(data.getLike()!=null&&data.getLike().equals("1")){
            isLike=true;

            innateDetailLikeImg.setBackgroundResource(R.mipmap.like_true);
            innateDetailLikeNum.setText(data.getLike());
            innateDetailLikeNum.setTextColor(Color.parseColor("#F99830"));
        }else{
            isLike=false;

            innateDetailLikeImg.setBackgroundResource(R.mipmap.like);
            innateDetailLikeNum.setText(data.getLike());
            innateDetailLikeNum.setTextColor(Color.parseColor("#8a8a8a"));
        }
    }

    @Override
    public void submitLike(int result) {
        if(result==1){
            toast("操作成功");

            int num=Integer.parseInt(innateDetailLikeNum.getText().toString());

            if(isLike){
                isLike=false;

                innateDetailLikeImg.setBackgroundResource(R.mipmap.like);
                innateDetailLikeNum.setText(""+(num-1));
                innateDetailLikeNum.setTextColor(Color.parseColor("#8a8a8a"));
            }else{
                isLike=true;

                innateDetailLikeImg.setBackgroundResource(R.mipmap.like_true);
                innateDetailLikeNum.setText(""+(num+1));
                innateDetailLikeNum.setTextColor(Color.parseColor("#F99830"));
            }
        }

        innateDetailLikeLin.setEnabled(true);
    }

    @Override
    public void submitComment(CommentModel commentModel) {
        commentId="";
        commentCommit.setHint("");
        commentCommit.setText("");

        this.mList.add(commentModel);
        adapter.notifyDataSetChanged();

        comments++;
        innateDetailCommentNum.setText("评论("+comments+")");

        toast("评论成功");
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            page=1;
            getData(2);
        } else {
            superRecyclerView.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (mList != null) {
                if (page <pages) {
                    page++;
                    getData(3);
                } else {
                    if (pages== page) {
                        superRecyclerView.setNoMore(true);
                    } else {
                        superRecyclerView.completeLoadMore();
                    }
                }
            }
        } else {
            superRecyclerView.completeLoadMore();
        }
    }

    public void setBanner(final List<String> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(listBanner.get(i));
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.i("tag","点击");
                if(!TextUtils.isEmpty(data.getVideo_url())){
                    Log.i("tag","点击1");
                    playVideo(data.getVideo_url());

                    conductPlayImg.setVisibility(View.GONE);
                    mBanner.setVisibility(View.GONE);
                    zaojVideoPlayer.setVisibility(View.VISIBLE);
                }
            }
        });

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    protected boolean isPlay;
    protected boolean isPause;

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
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
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
}
