package com.family.afamily.adapters;

import android.content.Context;
import android.text.TextUtils;
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
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.view.StarBar;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class GoodsListCommAdapter extends BaseAdapter {
    private List<Map<String, String>> list;
    private Context context;

    public GoodsListCommAdapter(List<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_goods_comment_layout, null);
        }
        ImageView item_head_iv = BaseViewHolder.get(view, R.id.item_head_iv);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new GlideCircleTransform(context));
        Glide.with(context).load(list.get(position).get("avatar")).apply(options).into(item_head_iv);
        StarBar item_frag3_star = BaseViewHolder.get(view, R.id.item_frag3_star);
        TextView item_user_nick = BaseViewHolder.get(view, R.id.item_user_nick);
        TextView item_time = BaseViewHolder.get(view, R.id.item_time);
        TextView item_comm_content = BaseViewHolder.get(view, R.id.item_comm_content);
        item_user_nick.setText(list.get(position).get("username"));
        item_time.setText(list.get(position).get("add_time"));
        item_comm_content.setText(list.get(position).get("content"));

        String str = TextUtils.isEmpty(list.get(position).get("rank")) ? "0" : list.get(position).get("rank");
        item_frag3_star.setStarMark(Float.parseFloat(str), 3);
        item_frag3_star.setTouch(false);

        return view;
    }
}
