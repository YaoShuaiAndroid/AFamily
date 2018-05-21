package com.family.afamily.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ToExtensionView;
import com.family.afamily.activity.mvp.presents.ToExtensionPresenter;
import com.family.afamily.entity.ToExtensionData;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/1/17.
 */

public class ToExtensionActivity extends BaseActivity<ToExtensionPresenter> implements ToExtensionView {
    @BindView(R.id.to_exte_web)
    WebView toExteWeb;
    @BindView(R.id.to_exte_btn)
    TextView toExteBtn;

    private String token;
    private ToExtensionData data;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_to_extension);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.to_exte_btn)
    public void clickExte() {
        if (data != null) {
            if (data.getShare() != null && data.getShare().size() > 0) {
                presenter.showShare(this, data.getShare());
            } else {
                toast("后台未配置推广内容");
            }
        } else {
            toast("未获取到数据");
        }

    }

    @Override
    public ToExtensionPresenter initPresenter() {
        return new ToExtensionPresenter(this);
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        setTitle(this, "我要推广");
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token);
        }

        WebSettings webSettings = toExteWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);

//        toExteWeb.getSettings().setJavaScriptEnabled(true);
//        toExteWeb.getSettings().setSupportZoom(false);
//        toExteWeb.getSettings().setDomStorageEnabled(true);
//        toExteWeb.requestFocus();
//        toExteWeb.getSettings().setUseWideViewPort(true);
//        toExteWeb.getSettings().setLoadWithOverviewMode(true);
//        toExteWeb.getSettings().setBuiltInZoomControls(true);
//        toExteWeb.getSettings().setTextZoom(200);
    }

    @Override
    public void successData(ToExtensionData data) {
        if (data != null) {
            this.data = data;
            toExteWeb.loadData(data.getInfo(), "text/html; charset=UTF-8", null);
        }
    }

}
