package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ProductDetailsView;
import com.family.afamily.activity.mvp.presents.ProductDetailsPresenter;
import com.family.afamily.entity.GoodsInfoData;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 临时商品详情页
 * Created by hp2015-7 on 2018/3/2.
 */

public class ProductDetailsActivity extends BaseActivity<ProductDetailsPresenter> implements ProductDetailsView, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.study_details_banner)
    Banner studyDetailsBanner;
    @BindView(R.id.study_details_name)
    TextView studyDetailsName;
    @BindView(R.id.study_details_web)
    WebView studyDetailsWeb;
    @BindView(R.id.study_details_collection_iv)
    ImageView studyDetailsCollectionIv;
    @BindView(R.id.study_details_collection_tv)
    TextView studyDetailsCollectionTv;
    @BindView(R.id.study_details_collection)
    LinearLayout studyDetailsCollection;
    @BindView(R.id.study_zan_iv)
    ImageView studyZanIv;
    @BindView(R.id.study_zan_tv)
    TextView studyZanTv;
    @BindView(R.id.study_zan)
    LinearLayout studyZan;
    @BindView(R.id.study_pay_btn)
    TextView studyPayBtn;
    @BindView(R.id.study_details_bottom)
    LinearLayout studyDetailsBottom;
    private String goods_id;
    private String token;
    private String total_time;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_details);
        goods_id = getIntent().getStringExtra("goods_id");
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        setTitle(this, "详情");
        if (TextUtils.isEmpty(goods_id)) {
            toast("找不到该商品");
            finish();
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, goods_id);
            }
        }
        studyDetailsBanner.setFocusable(true);
        studyDetailsBanner.setFocusableInTouchMode(true);
        studyDetailsBanner.requestFocus();
        WebSettings webSettings = studyDetailsWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        studyDetailsWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    @Override
    public ProductDetailsPresenter initPresenter() {
        return new ProductDetailsPresenter(this);
    }

    private boolean verifyStoragePermissions() {
        if (!EasyPermissions.hasPermissions(this, PERMISSIONS_STORAGE)) {
            EasyPermissions.requestPermissions(this, "需要读写SD卡权限", REQUEST_EXTERNAL_STORAGE, PERMISSIONS_STORAGE);
            return false;
        }
        return true;
    }

    @OnClick(R.id.study_details_collection)
    public void clickCollection() {
        String flag = (String) studyDetailsCollectionTv.getTag();
        if (TextUtils.isEmpty(flag)) {
            toast("未获取到数据");
        } else if (flag.equals("1")) {
            presenter.submitCollect(token, goods_id, "1");
            studyDetailsCollection.setEnabled(false);
        } else {
            presenter.submitCollect(token, goods_id, "");
            studyDetailsCollection.setEnabled(false);
        }
    }

    @OnClick(R.id.study_zan)
    public void clickZan(){
        String flag = (String) studyZanTv.getTag();
        if (TextUtils.isEmpty(flag)) {
            toast("未获取到数据");
        } else if (flag.equals("1")) {
            presenter.submitCancelLove(token, goods_id);
            studyZanTv.setEnabled(false);
        } else {
            presenter.submitLove(token, goods_id);
            studyZanTv.setEnabled(false);
        }
    }

    @OnClick(R.id.study_pay_btn)
    public void clickAudio() {
        String path = (String) studyPayBtn.getTag();
        if (TextUtils.isEmpty(path)) {
            toast("音频文件错误");
        } else {
            if (verifyStoragePermissions()) {
                ///storage/emulated/0/Android/data/com.family.afamily/files/Download
                File files = mActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                presenter.downloadAudio(path, files.getAbsolutePath(), total_time, mActivity);
            }else{
                toast("没有获取到SD卡读写权限");
            }
        }
    }
    @Override
    public void submitCollectResult(int type) {
        if (type == 1) {
            presenter.getData(token, goods_id);
        }
        studyDetailsCollection.setEnabled(true);
        studyZanTv.setEnabled(true);
        if (type == 3) {
            startActivityForResult(ShoppingCarActivity.class, 10);
        }
    }
    @Override
    public void successData(GoodsInfoData goodsInfoData) {
        if (goodsInfoData != null) {
            if (goodsInfoData.getPicture() != null && goodsInfoData.getPicture().size() > 0) {
                setBannerData(goodsInfoData.getPicture());
            }
            String path = goodsInfoData.getAudio();
            if (TextUtils.isEmpty(path)) {
                studyPayBtn.setVisibility(View.GONE);
            } else {
                studyPayBtn.setVisibility(View.VISIBLE);
                studyPayBtn.setTag(path);
                total_time = goodsInfoData.getAudio_time();
            }
            Integer collect = goodsInfoData.getGoods_be_collected();
            if (collect != null && collect == 1) {
                studyDetailsCollectionTv.setTag("1");
                studyDetailsCollectionIv.setImageResource(R.mipmap.ic_study_sc_2);
                studyDetailsCollectionTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            } else {
                studyDetailsCollectionTv.setTag("0");
                studyDetailsCollectionTv.setTextColor(Color.parseColor("#333333"));
                studyDetailsCollectionIv.setImageResource(R.mipmap.ic_study_sc_1);
            }

            studyZanTv.setText("点赞("+goodsInfoData.getLove_number()+")");
            Integer love = goodsInfoData.getLove_goods_status();
            if (love != null && love == 1) {
                studyZanTv.setTag("1");
                studyZanIv.setImageResource(R.mipmap.ic_zan_i);
                studyZanTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            } else {
                studyZanTv.setTag("0");
                studyZanTv.setTextColor(Color.parseColor("#333333"));
                studyZanIv.setImageResource(R.mipmap.ic_follow);
            }


            studyDetailsName.setText(goodsInfoData.getGoods_name());
            studyDetailsWeb.loadData(goodsInfoData.getGoods_desc(), "text/html; charset=UTF-8", null);
        }
    }

    private void setBannerData(List<String> imager) {
        //设置banner样式
        studyDetailsBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        studyDetailsBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        studyDetailsBanner.setImages(imager);
        //设置banner动画效果
        studyDetailsBanner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        studyDetailsBanner.isAutoPlay(true);
        //设置轮播时间
        studyDetailsBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        studyDetailsBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        studyDetailsBanner.start();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

}
