package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.GlideCircleTransform;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class FriendRankingAdapter extends SuperBaseAdapter<Map<String, String>> {
    public FriendRankingAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_head_img = holder.getView(R.id.item_head_img);
        holder.setText(R.id.item_pm_tv, (position + 1) + "");
        holder.setText(R.id.item_nick_tv, item.get("nick_name"));
        holder.setText(R.id.item_look_tv, item.get("look_count"));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new GlideCircleTransform(item_head_img.getContext()));
        Glide.with(item_head_img.getContext()).load(item.get("images")).apply(options).into(item_head_img);
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_friend_ranking_layout;
    }
}
