package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.EverydayTextDetailsActivity;
import com.family.afamily.activity.Frag4DetailsActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.activity.StudyDetailsActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.utils.UrlUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class CollectionAdapter extends SuperBaseAdapter<Map<String, String>> {
    private int type;
    private Context context;

    private boolean isShowCheck = false;

    public boolean isShowCheck() {
        return isShowCheck;
    }

    public void setShowCheck(boolean showCheck) {
        isShowCheck = showCheck;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CollectionAdapter(Context context, List<Map<String, String>> data, int type) {
        super(context, data);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, final int position) {
        final CheckBox item_clt_text_check = holder.getView(R.id.item_clt_text_check);
        ImageView item_clt_text_img = holder.getView(R.id.item_clt_text_img);
        TextView item_clt_text_title = holder.getView(R.id.item_clt_text_title);
        TextView item_clt_text_time = holder.getView(R.id.item_clt_text_time);
        TextView item_clt_price_tv = holder.getView(R.id.item_clt_price_tv);

        item_clt_text_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flag = item_clt_text_check.isChecked() ? "1" : "0";
                item.put("isCheck", flag);
                mData.set(position, item);
            }
        });

        String str = item.get("isCheck");
        if ("1".equals(str)) {
            item_clt_text_check.setChecked(true);
        } else {
            item_clt_text_check.setChecked(false);
        }


        if (isShowCheck) {
            item_clt_text_check.setVisibility(View.VISIBLE);
        } else {
            item_clt_text_check.setVisibility(View.GONE);
        }

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        if (type == 1) {
            Glide.with(context).load(item.get("picture")).apply(options).into(item_clt_text_img);
            item_clt_text_title.setText(item.get("title"));
            item_clt_text_time.setText(item.get("add_time"));
            item_clt_price_tv.setVisibility(View.GONE);
        } else if (type == 2) {
            Glide.with(context).load(item.get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options).into(item_clt_text_img);
            item_clt_text_title.setText(item.get("intro"));
            item_clt_text_time.setText(item.get("create_time"));
            item_clt_price_tv.setVisibility(View.GONE);
        } else if (type == 3) {
            item_clt_price_tv.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.get("picture")).apply(options).into(item_clt_text_img);
            item_clt_text_title.setText(item.get("title"));
            item_clt_text_time.setText(item.get("add_time"));
            item_clt_price_tv.setText(item.get("shop_price"));
        } else {
            item_clt_price_tv.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.get("picture")).apply(options).into(item_clt_text_img);
            item_clt_text_title.setText(item.get("title"));
            item_clt_text_time.setText(item.get("add_time"));
            item_clt_price_tv.setText(item.get("shop_price"));
        }

        holder.setOnClickListener(R.id.item_clt_root_rl, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowCheck) return;
                Intent intent = new Intent();
                if (type == 1) {
                    intent.setClass(context, EverydayTextDetailsActivity.class);
                    intent.putExtra("id", item.get("id"));
                } else if (type == 2) {
                    intent.setClass(context, ZaoJaoDetailsActivity.class);
                    intent.putExtra("id", item.get("id"));
                    intent.putExtra("study", item.get("look"));
                } else if (type == 3) {
                    intent.setClass(context, ProductDetailsActivity.class);
                   // intent.setClass(context, StudyDetailsActivity.class);
                    intent.putExtra("goods_id", item.get("goods_id"));
                } else {
                    intent.setClass(context, ProductDetailsActivity.class);
                    //intent.setClass(context, Frag4DetailsActivity.class);
                    intent.putExtra("goods_id", item.get("goods_id"));
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_collection_text;
    }
}
