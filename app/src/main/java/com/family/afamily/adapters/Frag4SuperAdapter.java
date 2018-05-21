package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.Frag4DetailsActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/31.
 */

public class Frag4SuperAdapter extends SuperBaseAdapter<Map<String,String>> {
    public Frag4SuperAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, final int position) {
        final ImageView item_frag4_img = holder.getView(R.id.item_frag4_img);
        TextView item_frag4_name =  holder.getView( R.id.item_frag4_name);
        TextView item_price_tv =  holder.getView( R.id.item_price_tv);
        TextView item_frag4_good_comm =  holder.getView( R.id.item_frag4_good_comm);
        TextView item_frag4_good_hpl =  holder.getView( R.id.item_frag4_good_hpl);
        LinearLayout item_frag4_root =  holder.getView( R.id.item_frag4_root);

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(item_frag4_img.getContext()).load(item.get("thumb")).apply(options).into(item_frag4_img);
        item_frag4_name.setText(item.get("name"));
        item_price_tv.setText(item.get("shop_price"));
        item_frag4_good_comm.setText(item.get("goods_comment_count") + "条好评");
        item_frag4_good_hpl.setText(item.get("goods_comment_rate") + "好评");

        item_frag4_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item_frag4_img.getContext(), ProductDetailsActivity.class);
                //Intent intent = new Intent(item_frag4_img.getContext(), Frag4DetailsActivity.class);
                intent.putExtra("goods_id", item.get("id"));
                item_frag4_img.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_frag4_list_layout;
    }
}
