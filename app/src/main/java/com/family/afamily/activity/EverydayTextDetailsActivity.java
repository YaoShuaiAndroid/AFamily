package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.EveyDayTextDetailsView;
import com.family.afamily.activity.mvp.presents.EveyDayTextDetailsPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/21.
 */

public class EverydayTextDetailsActivity extends BaseActivity<EveyDayTextDetailsPresenter> implements EveyDayTextDetailsView {
    @BindView(R.id.everyday_head_title_img)
    ImageView everydayHeadTitleImg;
//    @BindView(R.id.everyday_title_sc_iv)
//    ImageView everydayTitleScIv;
//    @BindView(R.id.everyday_title_ll)
//    LinearLayout everydayTitleLl;
    @BindView(R.id.base_title_right_iv)
    ImageView rightIv;
    @BindView(R.id.everyday_title_tv)
    TextView everydayTitleTv;
    @BindView(R.id.text_detials_web)
    WebView textDetailsWeb;
    private String id;
    private String isCollect;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_everyday_text_details);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = mActivity.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            Utils.getStatusHeight(mActivity, findViewById(R.id.everyday_title_ll));
//        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        if (TextUtils.isEmpty(id)) {
            toast("数据错误");
            finish();
        } else {
            String token = (String) SPUtils.get(mActivity, "token", "");
            presenter.getData(token, id);
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this,"美文详情");
        WebSettings webSettings = textDetailsWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        rightIv.setImageResource(R.mipmap.ic_bs_sc);
       // webSettings.setTextZoom();
//        textDetailsWeb.getSettings().setJavaScriptEnabled(true);
//        textDetailsWeb.getSettings().setSupportZoom(false);
//        textDetailsWeb.getSettings().setDomStorageEnabled(true);
//        textDetailsWeb.requestFocus();
//        textDetailsWeb.getSettings().setUseWideViewPort(true);
//        textDetailsWeb.getSettings().setLoadWithOverviewMode(true);
       // textDetailsWeb.getSettings().setTextZoom(200);
    }

    @OnClick(R.id.base_title_right_iv)
    public void clickSc() {
        String token = (String) SPUtils.get(mActivity, "token", "");
        presenter.getData(token, id);
        if (isCollect.equals("Y")) {
            rightIv.setEnabled(false);
            presenter.submitCollect(token, id, "2");
        } else {
            rightIv.setEnabled(false);
            presenter.submitCollect(token, id, "1");
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public EveyDayTextDetailsPresenter initPresenter() {
        return new EveyDayTextDetailsPresenter(this);
    }

    @Override
    public void dataCallback(Map<String, Object> dataBean) {
        if (dataBean != null) {
            Glide.with(mActivity).load(dataBean.get("picture")).into(everydayHeadTitleImg);
            everydayTitleTv.setText(dataBean.get("title") + "");
            isCollect = (String) dataBean.get("is_collect");
            if (isCollect.equals("Y")) {
                rightIv.setImageResource(R.mipmap.ic_bs_ysc);
            } else {
                rightIv.setImageResource(R.mipmap.ic_bs_sc);
            }
            textDetailsWeb.loadData(dataBean.get("content") + "", "text/html; charset=UTF-8", null);
        }
    }

    @Override
    public void submitCollectResult(int type) {
        if (type == 1) {
            String token = (String) SPUtils.get(mActivity, "token", "");
            presenter.getData(token, id);
        }
        rightIv.setEnabled(true);
    }
}
