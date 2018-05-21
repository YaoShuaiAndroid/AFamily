package com.family.afamily.adapters;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.GlideCircleImageLoader;
import com.family.afamily.utils.SampleListener;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.view.LandLayoutVideo;
import com.family.afamily.view.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class ConductAdapter extends SuperBaseAdapter<InnateIntelligenceModel> {
    GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private Context context;

    public ConductAdapter(Context context, List<InnateIntelligenceModel> data) {
        super(context, data);
        this.context = context;
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, InnateIntelligenceModel item, int position) {
        TextView conductTextTitle=baseViewHolder.getView(R.id.conduct_text_title);
        WebView conductTextContent=baseViewHolder.getView(R.id.conduct_text_content);
        Banner mBanner=baseViewHolder.getView(R.id.mBanner);
        RelativeLayout testFragmentBanner=baseViewHolder.getView(R.id.test_fragment_banner);
        //LandLayoutVideo zaojVideoPlayer=baseViewHolder.getView(R.id.zaoj_video_player);
        final SampleCoverVideo video_item_player = baseViewHolder.getView(R.id.zaoj_video_player);

        conductTextTitle.setText(item.getTitle());
        //conductTextContent.setText(Html.fromHtml(item.getContent()));

        conductTextContent.loadDataWithBaseURL(null,
                AppUtil.css(item.getContent()), "text/html", "utf-8", null);

        if(!TextUtils.isEmpty(item.getVideo_url())){
            mBanner.setVisibility(View.GONE);
            video_item_player.setVisibility(View.VISIBLE);

            String url="";
            if(item.getImgs()!=null&&item.getImgs().size()>0){
                url=item.getImgs().get(0);
            }

            ImageView imageView = new ImageView(context);
            RequestOptions options2 = new RequestOptions();
            options2.placeholder(R.mipmap.no_img);
            Glide.with(context).load(url).apply(options2).into(imageView);

            gsyVideoOptionBuilder
                    .setIsTouchWiget(false)
                    .setThumbImageView(imageView)
                    .setUrl(item.getVideo_url())
                    .setCacheWithPlay(true)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setShowFullAnimation(true)
                    .setNeedLockFull(true)
                    .setPlayPosition(position)
                    .setStandardVideoAllCallBack(new SampleListener() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            if (!video_item_player.isIfCurrentIsFullscreen()) {
                                //静音
                                GSYVideoManager.instance().setNeedMute(true);
                            }
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            //全屏不静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
                            GSYVideoManager.instance().setNeedMute(false);
                        }
                    }).build(video_item_player);

            //增加title
            video_item_player.getTitleTextView().setVisibility(View.GONE);

            //设置返回键
            video_item_player.getBackButton().setVisibility(View.GONE);

            //设置全屏按键功能
            video_item_player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolveFullBtn(video_item_player);
                }
            });
        }else{
            video_item_player.setVisibility(View.GONE);

            if(item.getImgs()!=null&&
                    item.getImgs().size()>0){
                setBanner(item.getImgs(),mBanner);
                mBanner.setVisibility(View.VISIBLE);
            }else{
                mBanner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getItemViewLayoutId(int position, InnateIntelligenceModel item) {
        return R.layout.list_conduct_test_item;
    }

    public void setBanner(final List<String> listBanner,Banner mBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i <listBanner.size(); i++) {
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

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }
}
