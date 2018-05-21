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
import com.family.afamily.entity.ShoppingCarList;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;

/**
 * Created by hp2015-7 on 2017/12/11.
 */

public class OrderPayAdapter extends BaseAdapter {
    private Context context;
    private List<ShoppingCarList> list;

    public OrderPayAdapter(Context context, List<ShoppingCarList> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_order_pay_list, null);
        }
        ImageView item_car_img = BaseViewHolder.get(view, R.id.item_car_img);
        TextView item_product_title = BaseViewHolder.get(view, R.id.item_product_title);
        TextView item_price_tv = BaseViewHolder.get(view, R.id.item_price_tv);
        TextView car_item_number_tv = BaseViewHolder.get(view, R.id.car_item_number_tv);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).getGoods_thumb()).apply(options).into(item_car_img);
        item_product_title.setText(list.get(position).getGoods_name());
        item_price_tv.setText("¥" + list.get(position).getGoods_price());
        car_item_number_tv.setText("x" + list.get(position).getGoods_number());

        return view;
    }
}
