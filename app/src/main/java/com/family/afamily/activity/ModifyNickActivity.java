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

public class ModifyNickActivity extends BaseActivity<MyInfoPresenter> implements MyInfoView {
    @BindView(R.id.edit_phone_nick)
    EditText editPhoneNick;
    @BindView(R.id.btn_submit_nick)
    TextView btnSubmitNick;

    private String token;
    private String oldNick;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_nick);
        token = (String) SPUtils.get(mActivity, "token", "");
        oldNick = getIntent().getStringExtra("oldNick");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "修改昵称");

        editPhoneNick.setText(oldNick);

        btnSubmitNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = editPhoneNick.getText().toString().trim();
                if (TextUtils.isEmpty(nick)) {
                    toast("请输入昵称");
                } else if (nick.equals(oldNick)) {
                    toast("昵称未做任何改动");
                } else {
                    if (Utils.isConnected(mActivity)) {
                        presenter.submitUserData(token, "2", nick, "", "", null);
                    }
                }
            }
        });
    }

    @Override
    public MyInfoPresenter initPresenter() {
        return new MyInfoPresenter(this);
    }

    @Override
    public void successData(Map<String, String> data) {

    }

    @Override
    public void updateResult() {
        setResult(100);
        finish();
    }

}
