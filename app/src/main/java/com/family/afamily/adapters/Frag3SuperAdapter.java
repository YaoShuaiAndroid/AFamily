package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.activity.StudyDetailsActivity;
import com.family.afamily.view.StarBar;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/31.
 */

public class Frag3SuperAdapter extends SuperBaseAdapter<Map<String, String>> {
    public Frag3SuperAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder,final Map<String, String> item, int position) {
        final ImageView item_frag3_img = holder.getView(R.id.item_frag3_img);
        TextView item_frag3_name =  holder.getView(R.id.item_frag3_name);
        TextView item_frag3_bs =  holder.getView(R.id.item_frag3_bs);
        TextView item_price_tv =  holder.getView(R.id.item_price_tv);
        TextView item_yj_price_tv =  holder.getView(R.id.item_yj_price_tv);
        TextView item_frag3_comm =  holder.getView(R.id.item_frag3_comm);
        StarBar item_frag3_star =  holder.getView(R.id.item_frag3_star);
        LinearLayout item_frag3_root =  holder.getView(R.id.item_frag3_root);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(item_frag3_img.getContext()).load(item.get("thumb")).apply(options).into(item_frag3_img);

        item_frag3_name.setText(item.get("name"));
        item_frag3_bs.setText(item.get("press"));
        item_price_tv.setText(item.get("shop_price"));
        item_yj_price_tv.setText(item.get("market_price"));
        item_yj_price_tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        item_frag3_comm.setText(item.get("goods_comment_count") + "条好评");
        String str = TextUtils.isEmpty(item.get("average_comment")) ? "0" : item.get("average_comment");
        item_frag3_star.setStarMark(Float.parseFloat(str), 3);
        item_frag3_star.setTouch(false);

        item_frag3_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(item_frag3_img.getContext(), StudyDetailsActivity.class);
                Intent intent = new Intent(item_frag3_img.getContext(), ProductDetailsActivity.class);
                intent.putExtra("goods_id", item.get("id"));
                item_frag3_img.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_frag3_list_layout;
    }
}
