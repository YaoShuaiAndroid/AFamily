package com.family.afamily.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.view.StarBar;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class GoodsInfoCommAdapter extends SuperBaseAdapter<Map<String, String>> {
    private Context context;

    public GoodsInfoCommAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_head_iv = holder.getView(R.id.item_head_iv);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new GlideCircleTransform(context));
        Glide.with(context).load(item.get("avatar")).apply(options).into(item_head_iv);
        StarBar item_frag3_star = holder.getView(R.id.item_frag3_star);

        holder.setText(R.id.item_user_nick, item.get("username"));
        holder.setText(R.id.item_time, item.get("add_time"));
        holder.setText(R.id.item_comm_content, item.get("content"));
        String str = TextUtils.isEmpty(item.get("rank")) ? "0" : item.get("rank");
        item_frag3_star.setStarMark(Float.parseFloat(str), 3);
        item_frag3_star.setTouch(false);

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_goods_comment_layout;
    }
}
