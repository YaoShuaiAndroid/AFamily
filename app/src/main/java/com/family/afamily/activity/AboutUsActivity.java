package com.family.afamily.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2017/10/27.
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.about_logo_iv)
    ImageView aboutLogoIv;
    @BindView(R.id.about_title_tv)
    TextView aboutTitleTv;
    @BindView(R.id.about_version_tv)
    TextView aboutVersionTv;
    @BindView(R.id.about_content_tv)
    WebView aboutContentTv;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_us);
        token = (String) SPUtils.get(mActivity, "token", "");

    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "关于我们");
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            aboutVersionTv.setText("版本V" + info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebSettings webSettings = aboutContentTv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        aboutContentTv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        getData();
    }

    private void getData() {
        showProgress(3);
        Map<String, String> params = new HashMap<>();
        // params.put("token",token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ABOUT_US_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                hideProgress();
                toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                hideProgress();
                if (response == null || response.getRet_code() != 1 || response.getData() == null) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    Utils.showMToast(mActivity, msg);
                } else {
                    // aboutTitleTv.setText(response.getData().get("title"));
                    aboutContentTv.loadData(response.getData().get("content"), "text/html; charset=UTF-8", null);
                }
            }
        }, params);
    }

}
