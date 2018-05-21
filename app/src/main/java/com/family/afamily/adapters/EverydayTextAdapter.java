package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/21.
 */

public class EverydayTextAdapter extends SuperBaseAdapter<Map<String, Object>> {
    public EverydayTextAdapter(Context context, List<Map<String, Object>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, Object> item, int position) {
        /**
         * {
         "id": "10",
         "title": "你好",
         "picture": "http://win2.qbt8.com/yjlx/Uploads/Picture/2017-12-14/5a31fca2bba33.jpg",
         "add_time": "2017-12-13",
         "content": "<p>哈哈哈哈哈哈</p>"
         }
         */

        holder.setText(R.id.item_everyday_title, item.get("title").toString());
        holder.setText(R.id.item_everyday_time, item.get("add_time").toString());
        ImageView item_everyday_img = holder.getView(R.id.item_everyday_img);
        Glide.with(item_everyday_img.getContext()).load(item.get("picture").toString()).into(item_everyday_img);

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, Object> item) {
        return R.layout.item_everyday_layout;
    }

}
