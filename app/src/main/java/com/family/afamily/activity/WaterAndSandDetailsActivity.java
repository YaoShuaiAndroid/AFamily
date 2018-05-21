package com.family.afamily.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.WaterAndSandDetailsView;
import com.family.afamily.activity.mvp.presents.WaterAndSandDetailsPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 水与沙详情
 * Created by hp2015-7 on 2017/12/1.
 */

public class WaterAndSandDetailsActivity extends BaseActivity<WaterAndSandDetailsPresenter> implements WaterAndSandDetailsView {
    @BindView(R.id.water_bg_img)
    ImageView waterBgImg;
    @BindView(R.id.water_title_tv)
    TextView waterTitleTv;
    @BindView(R.id.water_decs_tv)
    TextView waterDecsTv;
    @BindView(R.id.water_integral_tv)
    TextView waterIntegralTv;
    @BindView(R.id.water_content_web)
    WebView waterContentWeb;
    @BindView(R.id.water_sand_submit_btn)
    TextView waterSandSubmitBtn;
    @BindView(R.id.water_integral_rl)
    RelativeLayout waterIntegralRl;
    @BindView(R.id.water_and_sand_tv)
    TextView waterAndSandTv;

    private String token;
    private String id;
    private boolean sand;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water_and_sand_details);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            Utils.getStatusHeight(mActivity, findViewById(R.id.water_sand_title));
//        }
        id = getIntent().getStringExtra("id");
        sand = getIntent().getBooleanExtra("sand", false);

        if (TextUtils.isEmpty(id)) {
            toast("数据不存在");
            finish();
        } else {
            token = (String) SPUtils.get(mActivity, "token", "");
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, id, sand);
            }
            setResult(101);
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.water_sand_submit_btn)
    public void clickSubmit() {
        startActivity(YuYueActivity.class);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
//        waterContentWeb.getSettings().setJavaScriptEnabled(true);
//        waterContentWeb.getSettings().setSupportZoom(false);
//        waterContentWeb.getSettings().setDomStorageEnabled(true);
//        waterContentWeb.requestFocus();
//        waterContentWeb.getSettings().setUseWideViewPort(true);
//        waterContentWeb.getSettings().setLoadWithOverviewMode(true);
        WebSettings webSettings = waterContentWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        if (sand) {
            setTitle(this,"嗨玩沙滩详情");
            waterAndSandTv.setText("沙地详情");
        } else {
            setTitle(this,"水育乐园详情");
            waterAndSandTv.setText("游泳池详情");
        }
    }

    @Override
    public WaterAndSandDetailsPresenter initPresenter() {
        return new WaterAndSandDetailsPresenter(this);
    }

    @Override
    public void successData(Map<String, String> data) {
        if (data != null) {
            RequestOptions options2 = new RequestOptions();
            options2.error(R.drawable.error_pic);
            Glide.with(mActivity).load(data.get("picture")).apply(options2).into(waterBgImg);
            waterTitleTv.setText(data.get("title"));
            waterDecsTv.setText(data.get("intro"));
            waterIntegralTv.setText(data.get("integral"));
            waterContentWeb.loadData(data.get("info"), "text/html; charset=UTF-8", null);

//
            //  Glide.with(mActivity).load(data.get("picture")).into(waterBgImg);
            if (sand) {
                waterIntegralRl.setVisibility(View.GONE);
                waterSandSubmitBtn.setVisibility(View.GONE);
            } else {
                if (!data.get("type").equals("1")) {
                    waterIntegralRl.setVisibility(View.GONE);
                    waterSandSubmitBtn.setVisibility(View.GONE);
                } else {
                    waterIntegralRl.setVisibility(View.VISIBLE);
                    waterSandSubmitBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
