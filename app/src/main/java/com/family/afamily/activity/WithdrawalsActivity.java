package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BindBankView;
import com.family.afamily.activity.mvp.presents.WithdrawalsPresenter;
import com.family.afamily.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class WithdrawalsActivity extends BaseActivity<WithdrawalsPresenter> implements BindBankView {
    @BindView(R.id.withdrawals_money_et)
    EditText withdrawalsMoneyEt;
    @BindView(R.id.withdrawals_pw_et)
    EditText withdrawalsPwEt;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_withdrawals);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String money = withdrawalsMoneyEt.getText().toString();
        String pw = withdrawalsPwEt.getText().toString();
        if (TextUtils.isEmpty(money)) {
            toast("请输入取款金额");
        } else if (TextUtils.isEmpty(pw)) {
            toast("请输入提现密码");
        } else {
            presenter.submitData(token, money, pw);
        }

    }

    @Override
    public WithdrawalsPresenter initPresenter() {
        return new WithdrawalsPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "提现");
    }

    @Override
    public void successData() {
        setResult(100);
        finish();
    }
}
