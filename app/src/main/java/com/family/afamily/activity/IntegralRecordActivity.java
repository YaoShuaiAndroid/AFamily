package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.IntegralRecordPrecenter;
import com.family.afamily.adapters.IntegralRecordAdapter;
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
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralRecordActivity extends BaseActivity<IntegralRecordPrecenter> implements SuperRecyclerView.LoadingListener, PageSuccessView {
    @BindView(R.id.integral_record_list_rv)
    SuperRecyclerView integralRecordListRv;
    private IntegralRecordAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_integral_record);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public IntegralRecordPrecenter initPresenter() {
        return new IntegralRecordPrecenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "兑换记录");

        integralRecordListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        integralRecordListRv.addItemDecoration(divider);
        integralRecordListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        integralRecordListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        integralRecordListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        integralRecordListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        integralRecordListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        integralRecordListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new IntegralRecordAdapter(mActivity, mList);
        integralRecordListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, integralRecordListRv, adapter);
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
            presenter.getData(token, 1, 2, mList, integralRecordListRv, adapter);
        } else {
            integralRecordListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, pageBean.getPage() + 1, 3, mList, integralRecordListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        integralRecordListRv.setNoMore(true);
                    } else {
                        integralRecordListRv.completeLoadMore();
                    }
                }
            }
        } else {
            integralRecordListRv.completeLoadMore();
        }
    }
}
