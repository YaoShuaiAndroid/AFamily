package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.Frag4DetailsView;
import com.family.afamily.activity.mvp.presents.Frag4DetailsPresenter;
import com.family.afamily.adapters.Frag4GoodsAttrAdapter;
import com.family.afamily.adapters.GoodsInfoCommAdapter;
import com.family.afamily.adapters.GoodsListCommAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.GoodsAttr;
import com.family.afamily.entity.GoodsAttrValue;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/11.
 */

public class Frag4DetailsActivity extends BaseActivity<Frag4DetailsPresenter> implements Frag4DetailsView, SuperRecyclerView.LoadingListener {
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
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.study_details_car_number)
    TextView studyDetailsCarNumber;
    @BindView(R.id.shopping_car_rl)
    RelativeLayout shoppingCarRl;
    @BindView(R.id.study_details_buy)
    TextView studyDetailsBuy;
    @BindView(R.id.study_details_add_car)
    TextView studyDetailsAddCar;
    @BindView(R.id.study_details_bottom)
    LinearLayout studyDetailsBottom;
    @BindView(R.id.study_details_web)
    WebView studyDetailsWeb;
    @BindView(R.id.study_comment_list_lv2)
    SuperRecyclerView studyCommentListLv2;
    @BindView(R.id.study_d_root_rl)
    RelativeLayout studyDRootRl;
    @BindView(R.id.frag4_d_name_tv)
    TextView frag4DNmaeTv;
    @BindView(R.id.frag4_d_comm_title_tv)
    TextView frag4DCommTitleTv;
    @BindView(R.id.frag4_d_attr_list)
    MyListView frag4DAttrList;
    @BindView(R.id.frag4_d_attr_ll)
    LinearLayout frag4DAttrLl;

    private BasePageBean pageBean;
    private String goods_id;
    private String token;
    private GoodsInfoData infoData;
    private int index = 1;
    private GoodsInfoCommAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private List<GoodsAttr> goodsList;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_frag4_details);
        token = (String) SPUtils.get(mActivity, "token", "");
        goods_id = getIntent().getStringExtra("goods_id");
    }

    @Override
    public void netWorkConnected() {
    }

    /**
     * 点击购物车
     */
    @OnClick(R.id.shopping_car_rl)
    public void clickShoppingCar() {
        startActivityForResult(ShoppingCarActivity.class, 10);
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
     * 点击立即购买
     */
    @OnClick(R.id.study_details_buy)
    public void clickBuy() {
        if (goodsList != null && goodsList.size() > 0) {
            String id = "";
            for (int i = 0; i < goodsList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < goodsList.get(i).getValues().size(); j++) {
                    if (goodsList.get(i).getValues().get(j).isCheck()) {
                        flag = true;
                        id += goodsList.get(i).getValues().get(j).getId() + ",";
                    }
                }
                if (!flag) {
                    toast("请选择全部商品属性");
                    break;
                }
            }
            id = id.substring(0, id.length());
            if (Utils.isConnected(mActivity)) {
                presenter.addShoppingCar(token, goods_id, id, 3);
            }
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.addShoppingCar(token, goods_id, "", 3);
            }
        }
    }

    /**
     * 点击添加购物车
     */
    @OnClick(R.id.study_details_add_car)
    public void clickAddCar() {
        if (goodsList != null && goodsList.size() > 0) {
            String id = "";
            for (int i = 0; i < goodsList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < goodsList.get(i).getValues().size(); j++) {
                    if (goodsList.get(i).getValues().get(j).isCheck()) {
                        flag = true;
                        id += goodsList.get(i).getValues().get(j).getId() + ",";
                    }
                }
                if (!flag) {
                    toast("请选择全部商品属性");
                    break;
                }
            }
            id = id.substring(0, id.length());
            if (Utils.isConnected(mActivity)) {
                presenter.addShoppingCar(token, goods_id, id, 1);
            }
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.addShoppingCar(token, goods_id, "", 1);
            }
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

    @OnClick(R.id.frag4_tip_iv)
    public void clickTip() {
        if (infoData != null) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "说明");
            bundle.putString("link", infoData.getAdditional_desc());
            startActivity(AllWebViewActivity.class, bundle);
        } else {
            toast("未获取到数据");
        }
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
//        WebSettings webSettings = studyDetailsWeb.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        studyDetailsWeb.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }
//        });

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

    @Override
    public Frag4DetailsPresenter initPresenter() {
        return new Frag4DetailsPresenter(this);
    }


    private double price;

    @Override
    public void successData(GoodsInfoData goodsInfoData) {
        if (goodsInfoData != null) {
            infoData = goodsInfoData;
            if (infoData.getPicture() != null && infoData.getPicture().size() > 0) {
                setBannerData(infoData.getPicture());
            }
            frag4DNmaeTv.setText(infoData.getGoods_name());
            //studyDetailsDecs.setText(infoData.getGoods_brief());

            studyDetailsPrice.setText("￥" + infoData.getShop_price());
            studyDetailsZk.setText(infoData.getZhekou() + "折");
            studyDetailsYj.setText(infoData.getMarket_price());
            studyDetailsYj.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            studyDetailsJf.setText("购买返" + infoData.getGive_integral() + "积分");
            frag4DCommTitleTv.setText("商品评论（" + infoData.getComment_count() + "）");

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

            goodsList = infoData.getGoods_attr();
            if (goodsList != null && goodsList.size() > 0) {
                frag4DAttrLl.setVisibility(View.VISIBLE);
                frag4DAttrList.setAdapter(new Frag4GoodsAttrAdapter(mActivity, goodsList, new Frag4GoodsAttrAdapter.SelectTag() {
                    @Override
                    public void clickTag() {
                        price = Double.parseDouble(infoData.getShop_price());
                        for (int i = 0; i < goodsList.size(); i++) {
                            for (int j = 0; j < goodsList.get(i).getValues().size(); j++) {
                                if (goodsList.get(i).getValues().get(j).isCheck()) {
                                    GoodsAttrValue v = goodsList.get(i).getValues().get(j);
                                    double money = v.getPrice();
                                    price += money;
                                }
                            }
                        }
                        studyDetailsPrice.setText("￥" + price);
                    }
                }));
            } else {
                frag4DAttrLl.setVisibility(View.GONE);
            }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            presenter.getData(token, goods_id);
        }
    }
}
