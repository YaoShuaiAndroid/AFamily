package com.family.afamily.adapters;

import android.content.Context;

import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/15.
 */

public class MyFinanceAdapter extends SuperBaseAdapter<Map<String, String>> {
    public MyFinanceAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        holder.setText(R.id.finance_name, item.get("store_name"));
        holder.setText(R.id.finance_count, "会员数：" + item.get("count_user"));
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_finance_layout;
    }
}
