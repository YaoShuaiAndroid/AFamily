package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
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
import com.family.afamily.activity.StudyDetailsActivity;
import com.family.afamily.utils.BaseViewHolder;
import com.family.afamily.view.StarBar;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Frag3Adapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public Frag3Adapter(Context context, List<Map<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_frag3_list_layout, null);
        }
        ImageView item_frag3_img = BaseViewHolder.get(view, R.id.item_frag3_img);
        TextView item_frag3_name = BaseViewHolder.get(view, R.id.item_frag3_name);
        TextView item_frag3_bs = BaseViewHolder.get(view, R.id.item_frag3_bs);
        TextView item_price_tv = BaseViewHolder.get(view, R.id.item_price_tv);
        TextView item_yj_price_tv = BaseViewHolder.get(view, R.id.item_yj_price_tv);
        TextView item_frag3_comm = BaseViewHolder.get(view, R.id.item_frag3_comm);
        StarBar item_frag3_star = BaseViewHolder.get(view, R.id.item_frag3_star);
        LinearLayout item_frag3_root = BaseViewHolder.get(view, R.id.item_frag3_root);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("thumb")).apply(options).into(item_frag3_img);

        item_frag3_name.setText(list.get(position).get("name"));
        item_frag3_bs.setText(list.get(position).get("press"));
        item_price_tv.setText(list.get(position).get("shop_price"));
        item_yj_price_tv.setText(list.get(position).get("market_price"));
        item_yj_price_tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        item_frag3_comm.setText(list.get(position).get("goods_comment_count") + "条好评");
        String str = TextUtils.isEmpty(list.get(position).get("average_comment")) ? "0" : list.get(position).get("average_comment");
        item_frag3_star.setStarMark(Float.parseFloat(str), 3);
        item_frag3_star.setTouch(false);

        item_frag3_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudyDetailsActivity.class);
                intent.putExtra("goods_id", list.get(position).get("id"));
                context.startActivity(intent);
            }
        });

        return view;
    }
}
