package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.MyFinancePresenter;
import com.family.afamily.adapters.MyFinanceAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2017/12/14.
 */

public class MyFinanceActivity extends BaseActivity<MyFinancePresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.finance_list_rv)
    SuperRecyclerView financeListRv;
    private MyFinanceAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_finance);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MyFinancePresenter initPresenter() {
        return new MyFinancePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的财务");

        financeListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        financeListRv.addItemDecoration(divider);
        financeListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        financeListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        financeListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        financeListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        financeListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        financeListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MyFinanceAdapter(mActivity, mList);
        financeListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, financeListRv, adapter);
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
            presenter.getData(token, 1, 2, mList, financeListRv, adapter);
        } else {
            financeListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, pageBean.getPage() + 1, 3, mList, financeListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        financeListRv.setNoMore(true);
                    } else {
                        financeListRv.completeLoadMore();
                    }
                }
            }
        } else {
            financeListRv.completeLoadMore();
        }
    }
}
