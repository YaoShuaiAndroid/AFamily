package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class CouponListAdapter extends SuperBaseAdapter<Map<String, String>> {

    private int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CouponListAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        TextView item_voucher_tv = holder.getView(R.id.item_voucher_tv);
        TextView item_voucher_tv2 = holder.getView(R.id.item_voucher_tv2);
        TextView item_coupon_btn = holder.getView(R.id.item_coupon_btn);
        if (type == 1) {
            item_coupon_btn.setText("未使用");
            item_coupon_btn.setBackgroundResource(R.drawable.fillet_15_yellow_bg);
            item_coupon_btn.setTextColor(Color.parseColor("#fb9927"));
        } else if (type == 2) {
            item_coupon_btn.setText("已使用");
            item_coupon_btn.setBackgroundResource(R.drawable.btn_login_25_yellow);
            item_coupon_btn.setTextColor(Color.parseColor("#ffffff"));
        } else {
            item_coupon_btn.setText("已过期");
            item_coupon_btn.setBackgroundResource(R.drawable.fillet_25_huis__bg);
            item_coupon_btn.setTextColor(Color.parseColor("#999999"));
        }

        String send_type = item.get("send_type");
        if ("3".equals(send_type)) {
            item_voucher_tv.setText(item.get("type_name"));
            item_voucher_tv2.setText(item.get("intro"));
            holder.setText(R.id.item_coupon_count, "编号：" + item.get("bonus_sn"));
        } else {
            item_voucher_tv.setText("满" + item.get("min_goods_amount") + "元");
            item_voucher_tv2.setText("减" + item.get("type_money") + "元");
        holder.setText(R.id.item_coupon_count, "数量：" + item.get("number"));
        }
        //bonus_sn
        holder.setText(R.id.item_coupon_title, item.get("type_name"));
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_my_coupon_layout;
    }
}
