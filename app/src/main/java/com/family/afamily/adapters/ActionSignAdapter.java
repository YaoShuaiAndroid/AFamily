package com.family.afamily.adapters;

import android.content.Context;

import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class ActionSignAdapter extends SuperBaseAdapter<Map<String, String>> {

    public ActionSignAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        holder.setText(R.id.item_name_tv, item.get("username"));
        holder.setText(R.id.item_mobile_tv, item.get("phone"));
        holder.setText(R.id.item_number_tv, item.get("number"));
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_action_sign_layout;
    }
}
