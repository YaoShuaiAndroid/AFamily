package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ReleaseActionView;
import com.family.afamily.activity.mvp.presents.ReleaseActionPresenter;
import com.family.afamily.utils.SPUtils;
import com.luck.picture.lib.tools.PictureFileUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/7.
 */

public class ReleaseActionActivity extends BaseActivity<ReleaseActionPresenter> implements ReleaseActionView {
    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.base_title_right_iv)
    ImageView baseTitleRightIv;
    @BindView(R.id.release_act_title)
    EditText releaseActTitle;
    @BindView(R.id.release_act_obj)
    EditText releaseActObj;
    @BindView(R.id.release_act_count)
    EditText releaseActCount;
    @BindView(R.id.action_dw_tv)
    TextView actionDwTv;
    @BindView(R.id.release_act_date)
    TextView releaseActDate;
    @BindView(R.id.release_act_address)
    EditText releaseActAddress;
    @BindView(R.id.release_act_more_tv)
    TextView releaseActMoreTv;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.release_act_submit_btn)
    TextView releaseActSubmitBtn;

    private String path, decs;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_action);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.release_act_submit_btn)
    public void submitData() {
        String title = releaseActTitle.getText().toString();
        String obj = releaseActObj.getText().toString();
        String number = releaseActCount.getText().toString();
        String date = releaseActDate.getText().toString();
        String address = releaseActAddress.getText().toString();

        if (TextUtils.isEmpty(title)) {
            toast("请输入活动标题");
        } else if (TextUtils.isEmpty(obj)) {
            toast("请输入活动对象");
        } else if (TextUtils.isEmpty(number)) {
            toast("请输入活动人数");
        } else if (TextUtils.isEmpty(date)) {
            toast("请选择活动时间");
        } else if (TextUtils.isEmpty(address)) {
            toast("请输入活动地址");
        } else if (TextUtils.isEmpty(path) || TextUtils.isEmpty(decs)) {
            toast("请完善封面图和活动说明");
        } else {
            releaseActSubmitBtn.setEnabled(false);
            presenter.submitData(token, title, obj, number, date, address, path, decs,releaseActSubmitBtn);
        }
    }


    @Override
    public ReleaseActionPresenter initPresenter() {
        return new ReleaseActionPresenter(this);
    }

    @OnClick(R.id.release_act_more_tv)
    public void clickMore() {
        startActivityForResult(ActionMoreActivity.class, 100);
    }

    @OnClick(R.id.release_act_date)
    public void clickDateItem() {
        presenter.initTimePicker1(releaseActDate);
        //presenter.showDateDialog(releaseActDate);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "发布活动");
    }

    @Override
    public void submitSuccess() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(mActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            path = data.getStringExtra("path");
            decs = data.getStringExtra("decs");
            releaseActMoreTv.setText("已完善");
        }
    }
}
