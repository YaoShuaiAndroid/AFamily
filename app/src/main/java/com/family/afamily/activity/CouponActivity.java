package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.CouponView;
import com.family.afamily.activity.mvp.presents.CouponPresenter;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class CouponActivity extends BaseActivity<CouponPresenter> implements CouponView {

    @BindView(R.id.coupon_list_rv)
    RecyclerView couponListRv;
    private CommonAdapter<Map<String, String>> commonAdapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon);
        token = (String) SPUtils.get(mActivity, "token", "");
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token);
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public CouponPresenter initPresenter() {
        return new CouponPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "免费体验");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponListRv.setLayoutManager(linearLayoutManager);

        commonAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_coupon_layout, mList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> s, int position) {
                /**
                 *"type_id": "9",
                 "type_name": "泳池免费体验券",
                 "type_money": "0.00",
                 "send_type": "3",
                 "min_amount": "0.00",
                 "max_amount": "0.00",
                 "send_start_date": "1514736000",
                 "send_end_date": "1516896000",
                 "use_start_date": "1514736000",
                 "use_end_date": "1519747200",
                 "min_goods_amount": "0.00",
                 "intro": "限0-8岁宝宝体验一次",
                 "number": "30",
                 "get": "Y"
                 */

                TextView item_voucher_tv = holder.getView(R.id.item_voucher_tv);
                TextView item_voucher_tv2 = holder.getView(R.id.item_voucher_tv2);

                item_voucher_tv.setText(s.get("type_name"));
                item_voucher_tv2.setText(s.get("intro"));

                // ImageView item_coupon_img = holder.getView(R.id.item_coupon_img);
                holder.setText(R.id.item_coupon_title, s.get("type_name"));
                holder.setText(R.id.item_coupon_count, "数量：" + s.get("number"));
                TextView item_coupon_btn = holder.getView(R.id.item_coupon_btn);
                String type = s.get("get");
                if (s.get("number").equals("0")) {
                    item_coupon_btn.setText("已领完");
                    item_coupon_btn.setBackgroundResource(R.drawable.fillet_25_huis_line_bg);
                    item_coupon_btn.setTextColor(Color.parseColor("#cccccc"));
                } else {
                    if (type.equalsIgnoreCase("N")) {
                        item_coupon_btn.setText("已领取");
                        item_coupon_btn.setBackgroundResource(R.drawable.btn_login_25_yellow);
                        item_coupon_btn.setTextColor(Color.WHITE);
                    } else {
                        item_coupon_btn.setText("立即领取");
                        item_coupon_btn.setBackgroundResource(R.drawable.fillet_15_yellow_bg);
                        item_coupon_btn.setTextColor(ContextCompat.getColor(mContext, R.color.color_yellow));
                    }
                }

                item_coupon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (s.get("get").equalsIgnoreCase("Y")) {
                            presenter.collarCoupon(mActivity, token, s.get("type_id"));
                        }
                    }
                });
            }
        };
        couponListRv.setAdapter(commonAdapter);

    }

    @Override
    public void successData(List<Map<String, String>> data) {
        if (data != null && data.size() > 0) {
            mList.clear();
            mList.addAll(data);
            commonAdapter.notifyDataSetChanged();
        } else {
            mList.clear();
            commonAdapter.notifyDataSetChanged();
        }
    }
}
