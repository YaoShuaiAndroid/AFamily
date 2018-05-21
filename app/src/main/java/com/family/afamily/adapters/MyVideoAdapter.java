package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.mvp.presents.MyVideoPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class MyVideoAdapter extends SuperBaseAdapter<Map<String, String>> {
    private MyVideoPresenter presenter;
    private Context context;
    private String token;

    public MyVideoAdapter(Context context, List<Map<String, String>> data, MyVideoPresenter presenter) {
        super(context, data);
        this.context = context;
        this.presenter = presenter;
        token = (String) SPUtils.get(context, "token", "");
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, int position) {
        holder.setText(R.id.item_content_tv, item.get("intro"));
        holder.setText(R.id.item_follow_time, item.get("create_time"));
        holder.setText(R.id.item_follow_zan_tv, item.get("like"));
        holder.setText(R.id.item_follow_pl_tv, item.get("comment"));

        ImageView item_follow_zan_iv = holder.getView(R.id.item_follow_zan_iv);
        ImageView item_img_tv = holder.getView(R.id.item_img_tv);
        TextView item_follow_zan_tv = holder.getView(R.id.item_follow_zan_tv);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(context).load(item.get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options2).into(item_img_tv);

        if (TextUtils.isEmpty(item.get("is_like")) || item.get("is_like").equalsIgnoreCase("N")) {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_follow);
            item_follow_zan_tv.setTextColor(Color.parseColor("#999999"));
        } else {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_zan_i);
            item_follow_zan_tv.setTextColor(ContextCompat.getColor(context, R.color.color_yellow));
        }

        holder.setOnClickListener(R.id.item_del_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showDialog(context, token, item.get("id"));
            }
        });
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_my_video_layout;
    }
}
