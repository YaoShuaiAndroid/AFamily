package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.IntegralDetailsView;
import com.family.afamily.activity.mvp.presents.IntegralDetailsPresenter;
import com.family.afamily.entity.IntegralDetailsData;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralDetailsActivity extends BaseActivity<IntegralDetailsPresenter> implements IntegralDetailsView {
    @BindView(R.id.study_details_banner)
    Banner studyDetailsBanner;
    @BindView(R.id.integral_d_name)
    TextView integralDName;
    @BindView(R.id.integral_d_inte)
    TextView integralDInte;
    @BindView(R.id.integral_d_decs)
    TextView integralDDecs;
    @BindView(R.id.integral_d_btn)
    TextView integralDBtn;

    private String token;
    private String goods_id;
    private String type;
    IntegralDetailsData detailsData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_integral_details);
        goods_id = getIntent().getStringExtra("goods_id");
        token = (String) SPUtils.get(mActivity, "token", "");
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.integral_d_btn)
    public void clickExchange() {
        if (detailsData != null) {
            if (type.equals("1")) {
                presenter.showDialog(mActivity, detailsData.getGoods_name(), detailsData.getGoods_thumb(),
                        detailsData.getExchange_integral(), detailsData.getPay_points(), detailsData.getGoods_id(), 1);
            } else {
                presenter.showDialog(mActivity, detailsData.getType_name(), detailsData.getGoods_thumb(),
                        detailsData.getIntegral_count(), detailsData.getPay_points(), detailsData.getType_id(), 2);
            }

        } else {
            toast("未获取到数据信息");
        }

    }

    @Override
    public IntegralDetailsPresenter initPresenter() {
        return new IntegralDetailsPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "详情");
        if (TextUtils.isEmpty(goods_id)) {
            toast("找不到该商品");
            finish();
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, goods_id, type);
            }
        }
    }

    @Override
    public void successData(IntegralDetailsData detailsData) {
        if (detailsData != null) {
            this.detailsData = detailsData;
            if (type.equals("1")) {
                setBannerData(detailsData.getPicture());
                integralDName.setText(detailsData.getGoods_name());
                integralDInte.setText(detailsData.getExchange_integral());
                integralDDecs.setText(detailsData.getGoods_brief());
            } else {
                List<String> imges = new ArrayList<>();
                imges.add(detailsData.getGoods_thumb());
                setBannerData(imges);
                integralDName.setText(detailsData.getType_name());
                integralDInte.setText(detailsData.getIntegral_count());
                integralDDecs.setText("满" + detailsData.getMin_goods_amount() + "减" + detailsData.getType_money());
            }
//            String isEx = detailsData.getIs_exchange();
//            if(!TextUtils.isEmpty(isEx)&&isEx.equals("0")){
//                integralDBtn.setText("已兑换");
//                integralDBtn.setEnabled(false);
//                integralDBtn.setBackgroundColor(Color.parseColor("#cccccc"));
//            }else{
//                integralDBtn.setText("立即兑换");
//                integralDBtn.setEnabled(true);
//                integralDBtn.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.color_yellow));
//            }

        }
    }

    @Override
    public void submitSuccess() {
        finish();
    }

    private void setBannerData(List<String> imager) {
        if (imager == null) return;
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
}
