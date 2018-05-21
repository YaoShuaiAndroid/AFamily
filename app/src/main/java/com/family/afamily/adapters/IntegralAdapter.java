package com.family.afamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public IntegralAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_integral_layout, null);
        }
        ImageView item_integral_img = BaseViewHolder.get(view, R.id.item_integral_img);
        TextView item_integral_name = BaseViewHolder.get(view, R.id.item_integral_name);
        TextView item_integral_tv = BaseViewHolder.get(view, R.id.item_integral_tv);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("goods_thumb")).apply(options2).into(item_integral_img);

        item_integral_name.setText(list.get(position).get("goods_name"));
        item_integral_tv.setText(list.get(position).get("exchange_integral") + "积分");
        return view;
    }
}
