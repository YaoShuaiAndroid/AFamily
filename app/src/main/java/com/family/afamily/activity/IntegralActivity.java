package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.IntegralView;
import com.family.afamily.activity.mvp.presents.IntegralPresenter;
import com.family.afamily.adapters.IntegralAdapter;
import com.family.afamily.entity.IntegralData;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralActivity extends BaseActivity<IntegralPresenter> implements IntegralView {
    @BindView(R.id.integral_banner)
    Banner integralBanner;
    @BindView(R.id.integral_tab1_tv)
    TextView integralTab1Tv;
    @BindView(R.id.integral_tab1)
    LinearLayout integralTab1;
    @BindView(R.id.integral_tab2)
    LinearLayout integralTab2;
    @BindView(R.id.integral_tab3)
    LinearLayout integralTab3;
    @BindView(R.id.integral_all_tab)
    ImageView integralAllTab;
    @BindView(R.id.integral_hb_tab)
    ImageView integralHbTab;
    @BindView(R.id.integral_wj_tab)
    ImageView integralWjTab;
    @BindView(R.id.integral_yhj_tab)
    ImageView integralYhjTab;
    @BindView(R.id.integral_list_rv)
    MyGridView integralListRv;
    private IntegralAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_integral);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public IntegralPresenter initPresenter() {
        return new IntegralPresenter(this);
    }

    @OnClick({R.id.integral_all_tab, R.id.integral_hb_tab, R.id.integral_wj_tab, R.id.integral_yhj_tab})
    public void clickAllTab(View view) {
        Intent intent = new Intent(mActivity, IntegralAreaListActivity.class);
        if (view.getId() == R.id.integral_all_tab) {
            intent.putExtra("cat_id", "0");
        } else if (view.getId() == R.id.integral_hb_tab) {
            intent.putExtra("cat_id", "1");
        } else if (view.getId() == R.id.integral_wj_tab) {
            intent.putExtra("cat_id", "41");
        } else {
            intent.putExtra("cat_id", "10");
        }
        startActivity(intent);
    }

    @OnClick(R.id.integral_tab3)
    public void clickTab3() {
        startActivity(IntegralListActivity.class);
    }

    @OnClick(R.id.integral_tab2)
    public void clickTab2() {
        startActivity(IntegralRecordActivity.class);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "积分专区");
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token);
        }
        integralBanner.start();
        integralBanner.setFocusable(true);
        integralBanner.setFocusableInTouchMode(true);
        integralBanner.requestFocus();

        adapter = new IntegralAdapter(mActivity, list);
        integralListRv.setAdapter(adapter);

        integralListRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, IntegralDetailsActivity.class);
                String type = "1";
                intent.putExtra("goods_id", list.get(position).get("goods_id"));
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });


    }

    @Override
    public void successData(IntegralData integralData) {
        if (integralData != null) {
            List<Map<String, String>> banner = integralData.getBanner();
            List<Map<String, String>> goods_list = integralData.getGoods_list();
            //banner图
            if (banner != null && banner.size() > 0) {
                images.clear();
                for (int i = 0; i < banner.size(); i++) {
                    images.add(banner.get(i).get("picture"));
                }
                setBannerData(banner);
            }
            integralTab1Tv.setText("积分" + integralData.getPay_points());
            if (goods_list != null && goods_list.size() > 0) {
                list.clear();
                list.addAll(goods_list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setBannerData(final List<Map<String, String>> data) {
        //设置banner样式
        integralBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        integralBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        integralBanner.setImages(images);
        //设置banner动画效果
        integralBanner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        integralBanner.isAutoPlay(true);

        //设置轮播时间
        integralBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        integralBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        integralBanner.start();
        integralBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(mActivity, AllWebViewActivity.class);
                intent.putExtra("title", data.get(position).get("name"));
                intent.putExtra("link", data.get(position).get("url"));
                startActivity(intent);
            }
        });
    }
}
