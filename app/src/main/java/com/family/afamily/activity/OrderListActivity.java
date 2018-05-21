package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.OrderListView;
import com.family.afamily.activity.mvp.presents.OrderListPresenter;
import com.family.afamily.adapters.OrderListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.OrderListData;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class OrderListActivity extends BaseActivity<OrderListPresenter> implements SuperRecyclerView.LoadingListener, OrderListView {
    @BindView(R.id.order_all_tv)
    TextView orderAllTv;
    @BindView(R.id.order_all_v)
    View orderAllV;
    @BindView(R.id.order_dfk_tv)
    TextView orderDfkTv;
    @BindView(R.id.order_dfk_v)
    View orderDfkV;
    @BindView(R.id.order_dfh_tv)
    TextView orderDfhTv;
    @BindView(R.id.order_dfh_v)
    View orderDfhV;
    @BindView(R.id.order_ok_tv)
    TextView orderOkTv;
    @BindView(R.id.order_ok_v)
    View orderOkV;
    @BindView(R.id.order_list_lv)
    SuperRecyclerView orderListLv;
    private OrderListAdapter adapter;
    private List<OrderListData> mList = new ArrayList<>();
    private String token;
    private int index_tab = 1;
    private BasePageBean pageBean;
    private String status = "";

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_list);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.order_all_tv)
    public void clickAll() {
        if (index_tab != 1) {
            index_tab = 1;
            status = "";
            initView(orderAllTv, orderAllV);
            orderListLv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
            }
        }
    }

    @OnClick(R.id.order_dfk_tv)
    public void clickDFK() {
        if (index_tab != 2) {
            index_tab = 2;
            status = "100";
            initView(orderDfkTv, orderDfkV);
            orderListLv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
            }
        }
    }

    @OnClick(R.id.order_dfh_tv)
    public void clickDFH() {
        if (index_tab != 3) {
            index_tab = 3;
            status = "101";
            initView(orderDfhTv, orderDfhV);
            orderListLv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
            }
        }
    }

    @OnClick(R.id.order_ok_tv)
    public void clickOk() {
        if (index_tab != 4) {
            index_tab = 4;
            status = "102";
            initView(orderOkTv, orderOkV);
            orderListLv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
            }
        }
    }

    public void initView(TextView tv, View v) {
        orderAllTv.setTextColor(Color.parseColor("#333333"));
        orderDfkTv.setTextColor(Color.parseColor("#333333"));
        orderDfhTv.setTextColor(Color.parseColor("#333333"));
        orderOkTv.setTextColor(Color.parseColor("#333333"));
        orderAllV.setVisibility(View.INVISIBLE);
        orderDfkV.setVisibility(View.INVISIBLE);
        orderDfhV.setVisibility(View.INVISIBLE);
        orderOkV.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
    }

    @Override
    public OrderListPresenter initPresenter() {
        return new OrderListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的订单");
        orderListLv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        orderListLv.addItemDecoration(divider);
        orderListLv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        orderListLv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        orderListLv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        orderListLv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        orderListLv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        orderListLv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new OrderListAdapter(mActivity, mList);
        adapter.setPresenter(presenter);
        orderListLv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
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
            presenter.getData(token, status, 1, 2, mList, orderListLv, adapter);
        } else {
            orderListLv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, status, pageBean.getPage() + 1, 3, mList, orderListLv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        orderListLv.setNoMore(true);
                    } else {
                        orderListLv.completeLoadMore();
                    }
                }
            }
        } else {
            orderListLv.completeLoadMore();
        }
    }

    @Override
    public void submitSuccess(boolean isRefresh) {
        if (isRefresh) {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
            } else {
                orderListLv.completeRefresh();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("tag", requestCode + "---------------------->" + resultCode);
        if (requestCode == 100 && resultCode == 100) {
            presenter.getData(token, status, 1, 1, mList, orderListLv, adapter);
        }
    }
}
