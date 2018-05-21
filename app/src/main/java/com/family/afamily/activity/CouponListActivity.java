package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.CouponListPresenter;
import com.family.afamily.adapters.CouponListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class CouponListActivity extends BaseActivity<CouponListPresenter> implements SuperRecyclerView.LoadingListener, PageSuccessView {

    @BindView(R.id.coupon_list_rv)
    SuperRecyclerView couponListRv;
    @BindView(R.id.coupon_wsy_tv)
    TextView couponWsyTv;
    @BindView(R.id.coupon_wsy_v)
    View couponWsyV;
    @BindView(R.id.coupon_ysy_tv)
    TextView couponYsyTv;
    @BindView(R.id.coupon_ysy_v)
    View couponYsyV;
    @BindView(R.id.coupon_ygq_tv)
    TextView couponYgqTv;
    @BindView(R.id.coupon_ygq_v)
    View couponYgqV;
    private CouponListAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    private int index_tab = 1;
    private BasePageBean pageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon_list);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.coupon_wsy_tv)
    public void clickWSY() {
        if (index_tab != 1) {
            index_tab = 1;
            initView(couponWsyTv, couponWsyV);
            couponListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index_tab, 1, 1, mList, couponListRv, adapter);
            }
        }
    }

    @OnClick(R.id.coupon_ysy_tv)
    public void clickYSY() {
        if (index_tab != 2) {
            index_tab = 2;
            initView(couponYsyTv, couponYsyV);
            couponListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index_tab, 1, 1, mList, couponListRv, adapter);
            }
        }
    }

    @OnClick(R.id.coupon_ygq_tv)
    public void clickYGQ() {
        if (index_tab != 3) {
            index_tab = 3;
            initView(couponYgqTv, couponYgqV);
            couponListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index_tab, 1, 1, mList, couponListRv, adapter);
            }
        }
    }

    public void initView(TextView tv, View v) {
        couponWsyTv.setTextColor(Color.parseColor("#333333"));
        couponYsyTv.setTextColor(Color.parseColor("#333333"));
        couponYgqTv.setTextColor(Color.parseColor("#333333"));
        couponWsyV.setVisibility(View.INVISIBLE);
        couponYsyV.setVisibility(View.INVISIBLE);
        couponYgqV.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
    }

    @Override
    public CouponListPresenter initPresenter() {
        return new CouponListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的优惠劵");

        couponListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        // RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL,2, Color.parseColor("#e8e8e8"));
        //  couponListRv.addItemDecoration(divider);
        couponListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        couponListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        couponListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        couponListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        couponListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        couponListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new CouponListAdapter(mActivity, mList);
        couponListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index_tab, 1, 1, mList, couponListRv, adapter);
        }
    }

    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index_tab, 1, 2, mList, couponListRv, adapter);
        } else {
            couponListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, index_tab, pageBean.getPage() + 1, 3, mList, couponListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        couponListRv.setNoMore(true);
                    } else {
                        couponListRv.completeLoadMore();
                    }
                }
            }
        } else {
            couponListRv.completeLoadMore();
        }
    }
}
