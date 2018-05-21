package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.activity.mvp.presents.CreateAddressPresenter;
import com.family.afamily.utils.SPUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class CreateAddressActivity extends BaseActivity<CreateAddressPresenter> implements SubmitSuccessView {
    @BindView(R.id.address_name_et)
    EditText addressNameEt;
    @BindView(R.id.address_phone_et)
    EditText addressPhoneEt;
    @BindView(R.id.address_address_tv)
    TextView addressAddressTv;
    @BindView(R.id.address_address_decs)
    EditText addressAddressDecs;
    @BindView(R.id.submit_btn)
    TextView submitBtn;
    private String province_id, city_id, area_id;
    private String address_id = "";
    private String token;
    private String address_;
    private String decs_;
    private String name_;
    private String phone_;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_address);
        address_id = getIntent().getStringExtra("address_id");
        province_id = getIntent().getStringExtra("province_id");
        city_id = getIntent().getStringExtra("city_id");
        area_id = getIntent().getStringExtra("area_id");
        token = (String) SPUtils.get(mActivity, "token", "");
        address_ = getIntent().getStringExtra("address");
        decs_ = getIntent().getStringExtra("decs");
        name_ = getIntent().getStringExtra("name");
        phone_ = getIntent().getStringExtra("phone");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.submit_btn)
    public void clickSubmit() {
        String name = addressNameEt.getText().toString();
        String phone = addressPhoneEt.getText().toString();
        String address = addressAddressTv.getText().toString();
        String decs = addressAddressDecs.getText().toString();

        if (TextUtils.isEmpty(name)) {
            toast("请输入收货人姓名");
        } else if (TextUtils.isEmpty(phone)) {
            toast("请输入收货人手机号");
        } else if (TextUtils.isEmpty(address)) {
            toast("请选择收货地址");
        } else if (TextUtils.isEmpty(decs)) {
            toast("请输入详细地址");
        } else {
            if (TextUtils.isEmpty(address_id)) {
                address_id = "";
            }
            presenter.submitData(token, address_id, province_id, city_id, area_id, decs, name, phone);
        }
    }

    @Override
    public CreateAddressPresenter initPresenter() {
        return new CreateAddressPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "收货地址");

        addressNameEt.setText(name_);
        addressPhoneEt.setText(phone_);
        addressAddressTv.setText(address_);
        addressAddressDecs.setText(decs_);

        addressAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddressCityActivity.class, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            String address = data.getStringExtra("address");
            province_id = data.getStringExtra("province_id");
            city_id = data.getStringExtra("city_id");
            area_id = data.getStringExtra("area_id");
            addressAddressTv.setText(address);
        }


    }

    @Override
    public void submitSuccess(List<Map<String, String>> mapList) {
        setResult(100);
        finish();
    }
}
