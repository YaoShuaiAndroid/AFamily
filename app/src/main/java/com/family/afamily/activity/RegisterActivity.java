package com.family.afamily.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.RegisterView;
import com.family.afamily.activity.mvp.presents.RegisterPresenter;
import com.family.afamily.utils.Countdown;
import com.family.afamily.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterView {
    @BindView(R.id.edit_phone_register)
    EditText editPhoneRegister;
    @BindView(R.id.edit_code_register)
    EditText editCodeRegister;
    @BindView(R.id.text_get_code)
    TextView textGetCode;
    @BindView(R.id.edit_tgr_phone)
    EditText editTgrPhone;
    @BindView(R.id.btn_next)
    TextView btnNext;

    private Countdown countdown;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.title_forget_pw_ll));
        }
    }
    //17776181746
    @Override
    public void netWorkConnected() {
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @OnClick(R.id.text_get_code)
    public void clickGode() {
        String mobile = editPhoneRegister.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            toast("请输入手机号码");
        } else if (!Utils.isMobile(mobile)) {
            toast("请输入正确的手机号码");
        } else {
            countdown = new Countdown(textGetCode, "重新发送(%s)", 60);
            countdown.start();
            presenter.getCode(mobile, 1);
        }

    }

    @OnClick(R.id.btn_next)
    public void clickNext() {
        String mobile = editPhoneRegister.getText().toString();
        String code = editCodeRegister.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            toast("请输入手机号码");
        } else if (!Utils.isMobile(mobile)) {
            toast("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(code)) {
            toast("请输入验证码码");
        } else {
            presenter.submitVerify(mobile, code);
        }
        // startActivity(RegisterNextActivity.class);
    }

    @Override
    public void getCodeFail() {
        countdown.stop();
    }

    @Override
    public void verifySuccess() {
        String mobile = editPhoneRegister.getText().toString();
        //String code = editCodeRegister.getText().toString();
        String tgr = editTgrPhone.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        bundle.putString("tgr", tgr);
        startActivityForResult(RegisterNextActivity.class, 10, bundle);
    }

    @Override
    public void registerSuccess(String token) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            setResult(10);
            finish();
        }
    }
}
