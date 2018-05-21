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
import com.family.afamily.activity.mvp.interfaces.ForgetPwView;
import com.family.afamily.activity.mvp.presents.ForgetPwPresenter;
import com.family.afamily.utils.Countdown;
import com.family.afamily.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class ForgetPWActivity extends BaseActivity<ForgetPwPresenter> implements ForgetPwView {
    @BindView(R.id.edit_phone_forget)
    EditText editPhoneForget;
    @BindView(R.id.edit_code_forget)
    EditText editCodeForget;
    @BindView(R.id.text_get_code)
    TextView textGetCode;
    @BindView(R.id.btn_next)
    TextView btnNext;
    private Countdown countdown;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_pw);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.title_forget_pw_ll));
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public ForgetPwPresenter initPresenter() {
        return new ForgetPwPresenter(this);
    }

    @OnClick(R.id.text_get_code)
    public void clickGode() {
        String mobile = editPhoneForget.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            toast("请输入手机号码");
        } else if (!Utils.isMobile(mobile)) {
            toast("请输入正确的手机号码");
        } else {
            countdown = new Countdown(textGetCode, "重新发送(%s)", 60);
            countdown.start();
            presenter.getCode(mobile, 2);
        }

    }


    @OnClick(R.id.btn_next)
    public void clickNext() {
        String mobile = editPhoneForget.getText().toString();
        String code = editCodeForget.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            toast("请输入手机号码");
        } else if (!Utils.isMobile(mobile)) {
            toast("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(code)) {
            toast("请输入验证码码");
        } else {
            presenter.verifyForgetPw(mobile, code);
        }
    }

    @Override
    public void getCodeFail() {
        countdown.stop();
    }

    @Override
    public void verifyForgetPwSuccess() {
        String mobile = editPhoneForget.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        startActivityForResult(ForgetPWNextActivity.class, 20, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == 20) {
            finish();
        }
    }
}
