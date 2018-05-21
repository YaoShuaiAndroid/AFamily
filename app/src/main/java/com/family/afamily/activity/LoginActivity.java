package com.family.afamily.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.LoginView;
import com.family.afamily.activity.mvp.presents.LoginPresenter;
import com.family.afamily.utils.SPUtils;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.edit_phone_login)
    EditText editPhoneLogin;
    @BindView(R.id.edit_pw_login)
    EditText editPwLogin;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.text_register_login)
    TextView textRegisterLogin;
    @BindView(R.id.text_forget_pw_login)
    TextView textForgetPwLogin;


    @Override
    public void netWorkConnected() {
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.text_forget_pw_login)
    public void clickForgetPW() {
        startActivity(ForgetPWActivity.class);
    }

    @OnClick(R.id.text_register_login)
    public void clickRegister() {
        startActivityForResult(RegisterActivity.class, 10);
    }

    @OnClick(R.id.btn_login)
    public void clickLogin() {
        presenter.loginSubmit(editPhoneLogin.getText().toString(), editPwLogin.getText().toString());
    }
    @OnClick(R.id.text_look_btn)
    public void clickLookBtn(){
        SPUtils.remove(mActivity, "token");
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginFail(String msg) {
        toast(msg);
    }

    @Override
    public void loginSuccess() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            finish();
        }
    }
}
