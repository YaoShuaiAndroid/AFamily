package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 使用满减券
 * Created by hp2015-7 on 2018/1/6.
 */

public class VoucherActivity extends BaseActivity {
    @BindView(R.id.voucher_list_rv)
    RecyclerView voucherListRv;
    private List<Map<String, String>> list = new ArrayList<>();
    private CommonAdapter<Map<String, String>> adapter;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_voucher);

    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "满减券使用");
        if (myApplication.getUser_bonus() != null) {
            list.addAll(myApplication.getUser_bonus());
        } else {
            toast("后台数据异常");
            finish();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        voucherListRv.setLayoutManager(linearLayoutManager);

        adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_voucher_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> s, final int position) {
                holder.setText(R.id.item_voucher_tv, "满" + s.get("min_goods_amount") + "元\n减" + s.get("type_money") + "元");
                holder.setText(R.id.item_coupon_title, s.get("type_name"));
                TextView item_coupon_btn = holder.getView(R.id.item_coupon_btn);
                item_coupon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("position", position);
                        setResult(90, data);
                        finish();
                    }
                });
            }
        };
        voucherListRv.setAdapter(adapter);


    }
}
