package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class Frag4ListAdapter extends SuperBaseAdapter<Map<String, String>> {
    private Context context;

    public Frag4ListAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_frag4_img = holder.getView(R.id.item_frag4_img);
        TextView item_frag4_name = holder.getView(R.id.item_frag4_name);
        TextView item_price_tv = holder.getView(R.id.item_price_tv);
        TextView item_frag4_good_comm = holder.getView(R.id.item_frag4_good_comm);
        TextView item_frag4_good_hpl = holder.getView(R.id.item_frag4_good_hpl);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(item.get("goods_thumb")).apply(options).into(item_frag4_img);
        item_frag4_name.setText(item.get("goods_name"));
        item_price_tv.setText(item.get("shop_price"));
        item_frag4_good_comm.setText(item.get("goods_comment_count") + "条好评");
        item_frag4_good_hpl.setText(item.get("goods_comment_rate") + "好评");
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_frag4_list_layout;
    }
}
