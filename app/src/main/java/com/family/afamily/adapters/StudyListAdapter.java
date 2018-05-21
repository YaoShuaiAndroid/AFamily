package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.view.StarBar;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class StudyListAdapter extends SuperBaseAdapter<Map<String, String>> {
    private Context context;

    public StudyListAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_frag3_img = holder.getView(R.id.item_frag3_img);
        TextView item_yj_price_tv = holder.getView(R.id.item_yj_price_tv);
        StarBar item_frag3_star = holder.getView(R.id.item_frag3_star);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(item.get("goods_thumb")).apply(options).into(item_frag3_img);

        holder.setText(R.id.item_frag3_name, item.get("goods_name"));
        holder.setText(R.id.item_frag3_bs, item.get("press"));
        holder.setText(R.id.item_price_tv, item.get("shop_price"));
        holder.setText(R.id.item_yj_price_tv, item.get("market_price"));
        item_yj_price_tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.item_frag3_comm, item.get("goods_comment_count") + "条好评");
        String str = TextUtils.isEmpty(item.get("average_comment")) ? "0" : item.get("average_comment");
        item_frag3_star.setStarMark(Float.parseFloat(str), 3);
        item_frag3_star.setTouch(false);
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_frag3_list_layout;
    }
}
