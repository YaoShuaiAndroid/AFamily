package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class IntegralRecordAdapter extends SuperBaseAdapter<Map<String, String>> {

    public IntegralRecordAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_intg_record_img = holder.getView(R.id.item_intg_record_img);
        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(item_intg_record_img.getContext()).load(item.get("goods_thumb")).apply(options2).into(item_intg_record_img);

        holder.setText(R.id.item_intg_record_title, item.get("goods_name"));
        holder.setText(R.id.item_intg_record_number, item.get("integral") + "积分");
        holder.setText(R.id.item_intg_record_date, item.get("time"));
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_integral_record_layout;
    }
}
