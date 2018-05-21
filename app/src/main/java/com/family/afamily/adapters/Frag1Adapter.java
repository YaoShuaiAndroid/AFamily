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
 * Created by hp2015-7 on 2017/11/30.
 */

public class Frag1Adapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private OnclickItem onclickItem;

    public Frag1Adapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    public OnclickItem getOnclickItem() {
        return onclickItem;
    }

    public void setOnclickItem(OnclickItem onclickItem) {
        this.onclickItem = onclickItem;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_yc_frag1_layout, null);
        }
        TextView item_frag1_btn = BaseViewHolder.get(view, R.id.item_frag1_btn);
        ImageView item_frag1_pool_img = BaseViewHolder.get(view, R.id.item_frag1_pool_img);
        TextView item_frag1_title = BaseViewHolder.get(view, R.id.item_frag1_title);
        TextView item_frag1_decs = BaseViewHolder.get(view, R.id.item_frag1_decs);
        /**
         *   "id": "3",
         "title": "婴儿游泳池（小）",
         "intro": "适合3-12个月的小北鼻，需要家长陪护",
         "integral": "360",
         "picture": "http:\/\/win2.qbt8.com\/yjlx\/Uploads\/Picture\/2017-12-07\/5a28a70798696.jpg",
         "info": "<p>水电费了开始的分类<\/p>",
         "type": "1",
         "project": "1",
         "start_time": "9:00",
         "end_time": "17:00",
         "pool_type": "小"
         */
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("picture")).apply(options).into(item_frag1_pool_img);

        item_frag1_title.setText(list.get(position).get("title"));
        item_frag1_decs.setText(list.get(position).get("intro"));
        String type = list.get(position).get("type");
        if (type.equals("1")) {
            item_frag1_btn.setVisibility(View.VISIBLE);
        } else {
            item_frag1_btn.setVisibility(View.INVISIBLE);
        }

        item_frag1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickItem != null) {
                    onclickItem.onClickItem();
                }
            }
        });
        return view;
    }

    public interface OnclickItem {
        void onClickItem();
    }

}
