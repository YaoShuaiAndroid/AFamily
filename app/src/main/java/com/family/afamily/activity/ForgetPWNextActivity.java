package com.family.afamily.activity;

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
import com.family.afamily.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class ForgetPWNextActivity extends BaseActivity<ForgetPwPresenter> implements ForgetPwView {
    @BindView(R.id.edit_pw_forget)
    EditText editPwForget;
    @BindView(R.id.edit_repw_forget)
    EditText editRepwForget;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private String mobile;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_pw_next);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.title_forget_pw_ll));
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile = bundle.getString("mobile");
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String pw = editPwForget.getText().toString();
        String rpw = editRepwForget.getText().toString();
        if (TextUtils.isEmpty(pw)) {
            toast("请输入密码");
        } else if (!Utils.isPassWord(pw)) {
            toast("密码必须为数组字母组合，并且长度为6-20位");
        } else if (TextUtils.isEmpty(rpw)) {
            toast("请输入确认密码");
        } else if (!pw.equals(rpw)) {
            toast("两次密码输入不一致");
            editRepwForget.setText("");
        } else {
            presenter.submitForgetPw(mobile, pw);
        }
    }

    @Override
    public ForgetPwPresenter initPresenter() {
        return new ForgetPwPresenter(this);
    }

    @Override
    public void getCodeFail() {
    }

    @Override
    public void verifyForgetPwSuccess() {
        setResult(20);
        finish();
    }

}
