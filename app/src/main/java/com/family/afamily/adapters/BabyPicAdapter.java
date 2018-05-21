package com.family.afamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;

/**
 * Created by hp2015-7 on 2017/12/14.
 */

public class BabyPicAdapter extends BaseAdapter {
    private List<String> pics;
    private Context context;

    public BabyPicAdapter(List<String> pics, Context context) {
        this.pics = pics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pics.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_baby_pic_list, null);
        }
        ImageView item_pic_iv = BaseViewHolder.get(view, R.id.item_pic_iv);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(pics.get(position)).apply(options).into(item_pic_iv);


        return view;
    }
}
