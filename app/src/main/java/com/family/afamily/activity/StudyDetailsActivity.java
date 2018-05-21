package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.StudyDetailsView;
import com.family.afamily.activity.mvp.presents.StudyDetailsPresenter;
import com.family.afamily.adapters.GoodsInfoCommAdapter;
import com.family.afamily.adapters.GoodsListCommAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.GoodsInfoData;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyListView;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 书房详情
 * Created by hp2015-7 on 2017/12/11.
 */

public class StudyDetailsActivity extends BaseActivity<StudyDetailsPresenter> implements StudyDetailsView, SuperRecyclerView.LoadingListener, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.study_details_tab1)
    TextView studyDetailsTab1;
    @BindView(R.id.study_details_tab1_v)
    View studyDetailsTab1V;
    @BindView(R.id.study_details_tab2)
    TextView studyDetailsTab2;
    @BindView(R.id.study_details_tab2_v)
    View studyDetailsTab2V;
    @BindView(R.id.study_details_tab3)
    TextView studyDetailsTab3;
    @BindView(R.id.study_details_tab3_v)
    View studyDetailsTab3V;
    @BindView(R.id.study_d_title_rl)
    RelativeLayout studyDTitleRl;
    @BindView(R.id.study_details_banner)
    Banner studyDetailsBanner;
    @BindView(R.id.study_details_name)
    TextView studyDetailsName;
    @BindView(R.id.study_details_decs)
    TextView studyDetailsDecs;
    @BindView(R.id.study_details_price)
    TextView studyDetailsPrice;
    @BindView(R.id.study_details_zk)
    TextView studyDetailsZk;
    @BindView(R.id.study_details_dj)
    TextView studyDetailsDj;
    @BindView(R.id.study_details_yj)
    TextView studyDetailsYj;
    @BindView(R.id.study_details_jf)
    TextView studyDetailsJf;
    @BindView(R.id.study_details_audio)
    TextView studyDetailsAudio;
    @BindView(R.id.study_details_comm_title)
    TextView studyDetailsCommTitle;
    @BindView(R.id.study_comment_list_lv)
    MyListView studyCommentListLv;
    @BindView(R.id.study_details_collection_iv)
    ImageView studyDetailsCollectionIv;
    @BindView(R.id.study_details_collection_tv)
    TextView studyDetailsCollectionTv;
    @BindView(R.id.study_details_collection)
    LinearLayout studyDetailsCollection;
    @BindView(R.id.study_car_ic)
    ImageView studyCarIc;
    @BindView(R.id.study_details_car_number)
    TextView studyDetailsCarNumber;
    @BindView(R.id.shopping_car_rl)
    RelativeLayout shoppingCarRl;
    @BindView(R.id.study_details_jie_yue)
    TextView studyDetailsJieYue;
    @BindView(R.id.study_details_buy)
    TextView studyDetailsBuy;
    @BindView(R.id.study_details_add_car)
    TextView studyDetailsAddCar;
    @BindView(R.id.study_details_bottom)
    LinearLayout studyDetailsBottom;
    @BindView(R.id.study_d_root_rl)
    RelativeLayout studyDRootRl;
    @BindView(R.id.study_details_web)
    WebView studyDetailsWeb;
    @BindView(R.id.study_comment_list_lv2)
    SuperRecyclerView studyCommentListLv2;
    private String goods_id;
    private String token;
    private GoodsInfoData infoData;
    private int index = 1;

    private String total_time;
    private GoodsInfoCommAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private BasePageBean pageBean;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_study_details);
        goods_id = getIntent().getStringExtra("goods_id");
        token = (String) SPUtils.get(mActivity, "token", "");
        verifyStoragePermissions();
    }

    @Override
    public void netWorkConnected() {

    }

    private boolean verifyStoragePermissions() {
        if (!EasyPermissions.hasPermissions(this, PERMISSIONS_STORAGE)) {
            EasyPermissions.requestPermissions(this, "需要读写SD卡权限", REQUEST_EXTERNAL_STORAGE, PERMISSIONS_STORAGE);
            return false;
        }
        return true;
    }

    @OnClick(R.id.study_details_tab1)
    public void clickGoodsTitle() {
        if (index != 1) {
            index = 1;
            studyDetailsTab1V.setVisibility(View.VISIBLE);
            studyDetailsTab2V.setVisibility(View.INVISIBLE);
            studyDetailsTab3V.setVisibility(View.INVISIBLE);
            studyDRootRl.setVisibility(View.VISIBLE);
            studyDetailsWeb.setVisibility(View.GONE);
            studyCommentListLv2.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.study_details_tab2)
    public void clickDetailsTitle() {
        if (index != 2) {
            index = 2;
            studyDetailsTab1V.setVisibility(View.INVISIBLE);
            studyDetailsTab2V.setVisibility(View.VISIBLE);
            studyDetailsTab3V.setVisibility(View.INVISIBLE);
            studyDRootRl.setVisibility(View.GONE);
            studyDetailsWeb.setVisibility(View.VISIBLE);
            studyCommentListLv2.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.study_details_tab3)
    public void clickCommTitle() {
        if (index != 3) {
            index = 3;
            studyDetailsTab1V.setVisibility(View.INVISIBLE);
            studyDetailsTab2V.setVisibility(View.INVISIBLE);
            studyDetailsTab3V.setVisibility(View.VISIBLE);
            studyDRootRl.setVisibility(View.GONE);
            studyDetailsWeb.setVisibility(View.GONE);
            studyCommentListLv2.setVisibility(View.VISIBLE);

            if (list.size() == 0) {
                presenter.getCommentLsit(token, goods_id, 1, 1, list, studyCommentListLv2, adapter);
            }
        }
    }

    /**
     * 点击购物车
     */
    @OnClick(R.id.shopping_car_rl)
    public void clickShoppingCar() {
        startActivityForResult(ShoppingCarActivity.class, 10);
    }

    /**
     * 点击借阅
     */
    @OnClick(R.id.study_details_jie_yue)
    public void clickJieYue() {
        if (infoData != null) {
            presenter.showJieYueDialog(mActivity, token, infoData);
        } else {
            toast("未获取到数据");
        }
    }

    @Override
    public StudyDetailsPresenter initPresenter() {
        return new StudyDetailsPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
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
//        studyDetailsWeb.getSettings().setJavaScriptEnabled(true);
//        studyDetailsWeb.getSettings().setSupportZoom(true);
//        studyDetailsWeb.getSettings().setDomStorageEnabled(true);
//        studyDetailsWeb.requestFocus();
//        studyDetailsWeb.getSettings().setUseWideViewPort(true);
//        studyDetailsWeb.getSettings().setLoadWithOverviewMode(true);
//        studyDetailsWeb.getSettings().setBuiltInZoomControls(true);
        studyDetailsWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });


        studyCommentListLv2.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        studyCommentListLv2.addItemDecoration(divider);
        studyCommentListLv2.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        studyCommentListLv2.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        studyCommentListLv2.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        studyCommentListLv2.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        studyCommentListLv2.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        studyCommentListLv2.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new GoodsInfoCommAdapter(mActivity, list);
        studyCommentListLv2.setAdapter(adapter);

    }

    /**
     * 点击立即购买
     */
    @OnClick(R.id.study_details_buy)
    public void clickBuy() {
        if (Utils.isConnected(mActivity)) {
            presenter.addShoppingCar(token, goods_id, 3);
        }
    }

    /**
     * 点击添加购物车
     */
    @OnClick(R.id.study_details_add_car)
    public void clickAddCar() {
        if (Utils.isConnected(mActivity)) {
            presenter.addShoppingCar(token, goods_id, 1);
        }
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

    @OnClick(R.id.study_details_audio)
    public void clickAudio() {
        String path = (String) studyDetailsAudio.getTag();
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
    public void successData(GoodsInfoData goodsInfoData) {
        if (goodsInfoData != null) {
            infoData = goodsInfoData;
            if (infoData.getPicture() != null && infoData.getPicture().size() > 0) {
                setBannerData(infoData.getPicture());
            }
            studyDetailsName.setText(infoData.getGoods_name());
            studyDetailsDecs.setText(infoData.getGoods_brief());
            studyDetailsPrice.setText("￥" + infoData.getShop_price());
            studyDetailsZk.setText(infoData.getZhekou() + "折");
            studyDetailsYj.setText(infoData.getMarket_price());
            studyDetailsYj.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            studyDetailsJf.setText("购买返" + infoData.getGive_integral() + "积分");
            studyDetailsCommTitle.setText("商品评论（" + infoData.getComment_count() + "）");

            String path = infoData.getAudio();
            if (TextUtils.isEmpty(path)) {
                studyDetailsAudio.setVisibility(View.GONE);
            } else {
                studyDetailsAudio.setVisibility(View.VISIBLE);
                studyDetailsAudio.setTag(path);
                total_time = infoData.getAudio_time();
            }

            Integer car_number = infoData.getCart_count();
            if (car_number != null && car_number > 0) {
                studyDetailsCarNumber.setVisibility(View.VISIBLE);
                studyDetailsCarNumber.setText(car_number + "");
            } else {
                studyDetailsCarNumber.setVisibility(View.GONE);
            }
            studyDetailsWeb.loadData(infoData.getGoods_desc(), "text/html; charset=UTF-8", null);
            if (infoData.getComment_list() != null && infoData.getComment_list().size() > 0) {
                studyCommentListLv.setAdapter(new GoodsListCommAdapter(infoData.getComment_list(), mActivity));
            }

            studyDetailsBanner.setFocusable(true);
            studyDetailsBanner.setFocusableInTouchMode(true);
            studyDetailsBanner.requestFocus();

            Integer collect = infoData.getGoods_be_collected();
            if (collect != null && collect == 1) {
                studyDetailsCollectionTv.setTag("1");
                studyDetailsCollectionIv.setImageResource(R.mipmap.ic_study_sc_2);
                studyDetailsCollectionTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            } else {
                studyDetailsCollectionTv.setTag("0");
                studyDetailsCollectionTv.setTextColor(Color.parseColor("#333333"));
                studyDetailsCollectionIv.setImageResource(R.mipmap.ic_study_sc_1);
            }

        }
    }
    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }

    @Override
    public void submitCollectResult(int type) {
        if (type == 1) {
            presenter.getData(token, goods_id);
        }
        studyDetailsCollection.setEnabled(true);
        if (type == 3) {
            startActivityForResult(ShoppingCarActivity.class, 10);
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
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getCommentLsit(token, goods_id, 1, 2, list, studyCommentListLv2, adapter);
        } else {
            studyCommentListLv2.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getCommentLsit(token, goods_id, pageBean.getPage() + 1, 3, list, studyCommentListLv2, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        studyCommentListLv2.setNoMore(true);
                    } else {
                        studyCommentListLv2.completeLoadMore();
                    }
                }
            }
        } else {
            studyCommentListLv2.completeLoadMore();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除缓存文件
        deleteAllFiles(mActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
    }


    static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            presenter.getData(token, goods_id);
        }
    }
}
