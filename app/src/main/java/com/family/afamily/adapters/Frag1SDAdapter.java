package com.family.afamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Frag1SDAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public Frag1SDAdapter(Context context, List<Map<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_sd_frag1_layout, null);
        }

        ImageView item_frag1_sand_img = BaseViewHolder.get(view, R.id.item_frag1_sand_img);
        TextView item_frag1_title = BaseViewHolder.get(view, R.id.item_frag1_title);
        TextView item_frag1_decs = BaseViewHolder.get(view, R.id.item_frag1_decs);
        /**
         * "id": "6",
         "title": "波波沙池",
         "intro": "舒适对宝宝有益",
         "integral": "0",
         "picture": "http:\/\/win2.qbt8.com\/yjlx\/Uploads\/Picture\/2017-12-07\/5a28ade943646.jpg",
         "info": "<p>了开始懂了<\/p>",
         "type": "",
         "project": "2",
         "start_time": "0",
         "end_time": ""
         */
        Glide.with(context).load(list.get(position).get("picture")).into(item_frag1_sand_img);
        item_frag1_title.setText(list.get(position).get("title"));
        item_frag1_decs.setText(list.get(position).get("intro"));

        return view;
    }
}
