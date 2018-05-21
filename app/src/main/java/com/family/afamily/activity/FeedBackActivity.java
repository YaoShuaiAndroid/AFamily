package com.family.afamily.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.FeedBackView;
import com.family.afamily.activity.mvp.presents.FeedBackPresenter;
import com.family.afamily.utils.SPUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackView, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.title_number_tv)
    TextView titleNumberTv;
    @BindView(R.id.feedback_content_et)
    EditText feedbackContentEt;
    @BindView(R.id.feedback_submit_tv)
    TextView feedbackSubmitTv;
    @BindView(R.id.feedback_call_tv)
    TextView feedbackCallTv;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public FeedBackPresenter initPresenter() {
        return new FeedBackPresenter(this);
    }

    @OnClick(R.id.feedback_call_tv)
    public void callPhome() {
        String call_tv = feedbackCallTv.getText().toString();
        if (TextUtils.isEmpty(call_tv)) {
            toast("服务电话错误");
        } else {
            if (verifyCallPermissions()) {
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call_tv));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getData(token);
    }

    @OnClick(R.id.base_title_right_tv)
    public void clickReply() {
        startActivity(ReplyActivity.class);
        // finish();
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "意见反馈");

      //  presenter.getData(token);

        feedbackSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = feedbackContentEt.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    toast("请输入您要反馈的内容");
                } else {
                    presenter.submitData(token, str);
                }
            }
        });
    }

    @Override
    public void submitSuccess() {
        finish();
    }

    @Override
    public void getData(Map<String, String> data) {
        if (data != null) {
            String count = data.get("reply_count");
            int i = Integer.parseInt(count);
            if (i > 0) {
                titleNumberTv.setVisibility(View.VISIBLE);
                titleNumberTv.setText(i + "");
            } else {
                titleNumberTv.setVisibility(View.GONE);
            }
            String phone = data.get("phone");
            feedbackCallTv.setText(phone);
        }
    }

    private boolean verifyCallPermissions() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(mActivity, perms)) {
            EasyPermissions.requestPermissions(this, "本次操作需要打电话权限", 1, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
