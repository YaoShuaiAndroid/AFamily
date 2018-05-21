package com.family.afamily.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.entity.ItemBabyData;
import com.family.afamily.utils.SampleListener;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.view.MyGridView;
import com.family.afamily.view.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class BabyDetailsAdapter extends SuperBaseAdapter<ItemBabyData> {
    public final static String TAG = "RecyclerView2List";
    private Context context;
    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public BabyDetailsAdapter(Context context, List<ItemBabyData> data) {
        super(context, data);
        this.context = context;
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    @Override
    protected void convert(BaseViewHolder holder, ItemBabyData item, int position) {
        MyGridView item_baby_grid_list = holder.getView(R.id.item_baby_grid_list);
        RelativeLayout item_baby_video_rl = holder.getView(R.id.item_baby_video_rl);
        final SampleCoverVideo video_item_player = holder.getView(R.id.video_item_player);

        holder.setText(R.id.item_baby_date, item.getCreate_time());
        holder.setText(R.id.item_baby_address, item.getAddress());
        holder.setText(R.id.item_baby_text, item.getContent());

        if (item.getPicture() == null) {
            item_baby_grid_list.setVisibility(View.GONE);
            item_baby_video_rl.setVisibility(View.VISIBLE);

            ImageView imageView = new ImageView(context);
            RequestOptions options2 = new RequestOptions();
            options2.error(R.drawable.error_pic);
            Glide.with(context).load(item.getVideo() + UrlUtils.VIDEO_8_5_PIC).apply(options2).into(imageView);

            gsyVideoOptionBuilder
                    .setIsTouchWiget(false)
                    .setThumbImageView(imageView)
                    .setUrl(item.getVideo())
                    .setCacheWithPlay(true)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setPlayTag(TAG)
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
        } else {
            item_baby_grid_list.setVisibility(View.VISIBLE);
            item_baby_video_rl.setVisibility(View.GONE);

            item_baby_grid_list.setAdapter(new BabyPicAdapter(item.getPicture(), context));

        }

    }

    @Override
    protected int getItemViewLayoutId(int position, ItemBabyData item) {
        return R.layout.item_baby_details;
    }


    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }
}
