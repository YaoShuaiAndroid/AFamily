package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.IntegralListPresenter;
import com.family.afamily.adapters.IntegralListAdapter;
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
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralListActivity extends BaseActivity<IntegralListPresenter> implements SuperRecyclerView.LoadingListener, PageSuccessView {
    @BindView(R.id.tab_integral_in)
    TextView tabIntegralIn;
    @BindView(R.id.tab_integral_out)
    TextView tabIntegralOut;
    @BindView(R.id.integral_list_rv)
    SuperRecyclerView integralListRv;
    private IntegralListAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private BasePageBean pageBean;
    private String token;
    private int inedx_tab = 1;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_integral_list);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.tab_integral_in)
    public void clickIntegralIn() {
        if (inedx_tab != 1) {
            inedx_tab = 1;
            tabIntegralIn.setSelected(true);
            tabIntegralOut.setSelected(false);
            mList.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, inedx_tab + "", 1, 1, mList, integralListRv, adapter);
        }
    }

    @OnClick(R.id.tab_integral_out)
    public void clickIntegralOut() {
        if (inedx_tab != 2) {
            inedx_tab = 2;
            tabIntegralIn.setSelected(false);
            tabIntegralOut.setSelected(true);
            mList.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, inedx_tab + "", 1, 1, mList, integralListRv, adapter);
        }
    }


    @Override
    public IntegralListPresenter initPresenter() {
        return new IntegralListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "积分明细");

        tabIntegralIn.setSelected(true);
        integralListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        integralListRv.addItemDecoration(divider);
        integralListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        integralListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        integralListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        integralListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        integralListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        integralListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new IntegralListAdapter(mActivity, mList);
        integralListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, inedx_tab + "", 1, 1, mList, integralListRv, adapter);
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
            presenter.getData(token, inedx_tab + "", 1, 2, mList, integralListRv, adapter);
        } else {
            integralListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, inedx_tab + "", pageBean.getPage() + 1, 3, mList, integralListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        integralListRv.setNoMore(true);
                    } else {
                        integralListRv.completeLoadMore();
                    }
                }
            }
        } else {
            integralListRv.completeLoadMore();
        }
    }
}
