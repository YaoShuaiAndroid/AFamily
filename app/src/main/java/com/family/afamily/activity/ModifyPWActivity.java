package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MyInfoView;
import com.family.afamily.activity.mvp.presents.MyInfoPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class ModifyPWActivity extends BaseActivity<MyInfoPresenter> implements MyInfoView {
    @BindView(R.id.edit_old_modify)
    EditText editOldModify;
    @BindView(R.id.edit_pw_modify)
    EditText editPwModify;
    @BindView(R.id.edit_rpw_modify)
    EditText editRpwModify;
    @BindView(R.id.btn_next)
    TextView btnNext;

    private String token;

    @Override
    public void successData(Map<String, String> data) {

    }

    @Override
    public void updateResult() {
        setResult(100);
        finish();
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_pw);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "修改密码");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = editOldModify.getText().toString();
                String pw = editPwModify.getText().toString();
                String rpw = editRpwModify.getText().toString();

                if (TextUtils.isEmpty(old)) {
                    toast("请输入旧密码");
                } else if (TextUtils.isEmpty(pw)) {
                    toast("请输入密码");
                } else if (!Utils.isPassWord(pw)) {
                    toast("密码必须为数组字母组合，并且长度为6-20位");
                } else if (TextUtils.isEmpty(rpw)) {
                    toast("请输入确认密码");
                } else if (!pw.equals(rpw)) {
                    toast("两次密码输入不一致");
                    editRpwModify.setText("");
                } else {
                    presenter.submitUserData(token, "3", "", pw, old, null);
                }
            }
        });

    }

    @Override
    public MyInfoPresenter initPresenter() {
        return new MyInfoPresenter(this);
    }


}
