package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BindBankView;
import com.family.afamily.activity.mvp.presents.BindBankPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class BindBankActivity extends BaseActivity<BindBankPresenter> implements BindBankView {
    @BindView(R.id.bind_bank_name)
    EditText bindBankName;
    @BindView(R.id.bind_bank_car_number)
    EditText bindBankCarNumber;
    @BindView(R.id.bind_bank_car_name)
    EditText bindBankCarName;
    @BindView(R.id.bind_bank_car_zh)
    EditText bindBankCarZh;
    @BindView(R.id.bind_bank_city)
    EditText bindBankCity;
    @BindView(R.id.bind_bank_pw)
    EditText bindBankPw;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_bank);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String name = bindBankName.getText().toString();
        String car_number = bindBankCarNumber.getText().toString();
        String car_name = bindBankCarName.getText().toString();
        String car_zh = bindBankCarZh.getText().toString();
        String car_city = bindBankCity.getText().toString();
        String car_pw = bindBankPw.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请输入持卡人姓名");
        } else if (TextUtils.isEmpty(car_number)) {
            toast("请输入银行卡号");
        } else if (!Utils.isBankCar(car_number)) {
            toast("请输入正确银行卡号");
        } else if (TextUtils.isEmpty(car_name)) {
            toast("请输入银行卡所属银行");
        } else if (TextUtils.isEmpty(car_zh)) {
            toast("请输入银行卡的开户行");
        } else if (TextUtils.isEmpty(car_city)) {
            toast("请输入银行卡的开卡城市");
        } else if (TextUtils.isEmpty(car_pw) || car_pw.length() < 6) {
            toast("请设置6位提现密码");
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.submitData(token, name, car_number, car_name, car_zh, car_city, car_pw);
            }
        }

    }

    @Override
    public BindBankPresenter initPresenter() {
        return new BindBankPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "绑定银行卡");
    }

    @Override
    public void successData() {
        setResult(100);
        finish();
    }
}
