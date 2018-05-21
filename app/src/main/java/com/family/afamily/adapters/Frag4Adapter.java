package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.Frag4DetailsActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Frag4Adapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public Frag4Adapter(Context context, List<Map<String, String>> list) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_frag4_list_layout, null);
        }
        ImageView item_frag4_img = BaseViewHolder.get(view, R.id.item_frag4_img);
        TextView item_frag4_name = BaseViewHolder.get(view, R.id.item_frag4_name);
        TextView item_price_tv = BaseViewHolder.get(view, R.id.item_price_tv);
        TextView item_frag4_good_comm = BaseViewHolder.get(view, R.id.item_frag4_good_comm);
        TextView item_frag4_good_hpl = BaseViewHolder.get(view, R.id.item_frag4_good_hpl);
        LinearLayout item_frag4_root = BaseViewHolder.get(view, R.id.item_frag4_root);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("thumb")).apply(options).into(item_frag4_img);
        item_frag4_name.setText(list.get(position).get("name"));
        item_price_tv.setText(list.get(position).get("shop_price"));
        item_frag4_good_comm.setText(list.get(position).get("goods_comment_count") + "条好评");
        item_frag4_good_hpl.setText(list.get(position).get("goods_comment_rate") + "好评");

        item_frag4_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, Frag4DetailsActivity.class);
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("goods_id", list.get(position).get("id"));
                context.startActivity(intent);
            }
        });
        return view;
    }
}
