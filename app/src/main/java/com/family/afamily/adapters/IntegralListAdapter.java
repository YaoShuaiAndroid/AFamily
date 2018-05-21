package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class IntegralListAdapter extends SuperBaseAdapter<Map<String, String>> {

    public IntegralListAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        holder.setText(R.id.item_inte_name, item.get("change_type"));
        holder.setText(R.id.item_inte_date, item.get("change_time"));

        TextView item_inte_count = holder.getView(R.id.item_inte_count);
        String str = item.get("pay_points");
        if (!TextUtils.isEmpty(str) && str.contains("-")) {
            holder.setText(R.id.item_inte_count, item.get("pay_points"));
            item_inte_count.setTextColor(Color.parseColor("#ff0000"));
        } else {
            holder.setText(R.id.item_inte_count, "+" + item.get("pay_points"));
            item_inte_count.setTextColor(Color.parseColor("#fb9927"));
        }

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_integral_list_layout;
    }
}
