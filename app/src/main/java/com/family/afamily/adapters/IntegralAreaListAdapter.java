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
 * Created by hp2015-7 on 2018/1/9.
 */

public class IntegralAreaListAdapter extends SuperBaseAdapter<Map<String, String>> {

    public IntegralAreaListAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_integral_img = holder.getView(R.id.item_integral_img);
        TextView item_integral_name = holder.getView(R.id.item_integral_name);
        TextView item_integral_tv = holder.getView(R.id.item_integral_tv);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(item_integral_img.getContext()).load(item.get("goods_thumb")).apply(options2).into(item_integral_img);

        item_integral_name.setText(item.get("goods_name"));
        item_integral_tv.setText(item.get("exchange_integral") + "积分");
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_integral_layout;
    }
}
