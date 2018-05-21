package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.UrlUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class MasterAdapter extends SuperBaseAdapter<Map<String, String>> {
    public MasterAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_zaoj_img = holder.getView(R.id.item_zaoj_img);
        ImageView item_follow_zan_iv = holder.getView(R.id.item_follow_zan_iv);
        TextView item_follow_zan_tv = holder.getView(R.id.item_follow_zan_tv);
        TextView item_follow_pl_tv = holder.getView(R.id.item_follow_pl_tv);
        holder.setText(R.id.item_zaoj_title, item.get("intro"));
        item_follow_zan_tv.setText(item.get("liek_count"));
        item_follow_pl_tv.setText(item.get("comment_count"));
        if (TextUtils.isEmpty(item.get("is_like")) || item.get("is_like").equalsIgnoreCase("N")) {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_follow);
            item_follow_zan_tv.setTextColor(Color.parseColor("#999999"));
        } else {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_zan_i);
            item_follow_zan_tv.setTextColor(ContextCompat.getColor(item_zaoj_img.getContext(), R.color.color_yellow));
        }

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(item_zaoj_img.getContext()).load(item.get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options).into(item_zaoj_img);
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_master_layout;
    }
}
