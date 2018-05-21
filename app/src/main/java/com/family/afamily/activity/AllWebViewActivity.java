package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.utils.L;
import com.family.afamily.utils.Utils;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class AllWebViewActivity extends BaseActivity {
    private String title;
    private String link;
    private WebView all_webview_vb;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_all_webview);
        all_webview_vb = (WebView) findViewById(R.id.all_webview_vb);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link = bundle.getString("link");
            title = bundle.getString("title");
            L.e("tag",title +"------------->"+link);
            if (TextUtils.isEmpty(link)) {
                Utils.showMToast(mActivity, "未设置链接");
                finish();
            }
        }else{
            finish();
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        String t = TextUtils.isEmpty(title) ? getString(R.string.app_name) : title;
        setTitle(this, t);

        WebSettings webSettings = all_webview_vb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


//        all_webview_vb.getSettings().setJavaScriptEnabled(true);
//        all_webview_vb.getSettings().setSupportZoom(true);
//        all_webview_vb.getSettings().setDomStorageEnabled(true);
//        all_webview_vb.requestFocus();
//        all_webview_vb.getSettings().setUseWideViewPort(true);
//        all_webview_vb.getSettings().setLoadWithOverviewMode(true);
       // all_webview_vb.getSettings().setBuiltInZoomControls(true);
        all_webview_vb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        if (TextUtils.isEmpty(link))
            return;
        all_webview_vb.loadUrl(link);
        all_webview_vb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }
}
