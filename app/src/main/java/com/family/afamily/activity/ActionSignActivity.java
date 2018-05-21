package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.ActionSignPresenter;
import com.family.afamily.adapters.ActionSignAdapter;
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

public class ActionSignActivity extends BaseActivity<ActionSignPresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.action_sign_total)
    TextView actionSignTotal;
    @BindView(R.id.action_list_rv)
    SuperRecyclerView actionListRv;
    private ActionSignAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String id;
    private String token;
    private BasePageBean pageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_action_sign);
        id = getIntent().getStringExtra("id");
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public ActionSignPresenter initPresenter() {
        return new ActionSignPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "报名情况");
        actionListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        actionListRv.addItemDecoration(divider);
        actionListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        actionListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        actionListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        actionListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        actionListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        actionListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new ActionSignAdapter(mActivity, mList);
        actionListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, id, 1, 1, mList, actionListRv, adapter);
        }
    }


    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
            actionSignTotal.setText("报名总数：" + pageBean.getCount() + "人");
        } else {
            actionSignTotal.setText("报名总数：0" + "人");
        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, id, 1, 2, mList, actionListRv, adapter);
        } else {
            actionListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, id, pageBean.getPage() + 1, 3, mList, actionListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        actionListRv.setNoMore(true);
                    } else {
                        actionListRv.completeLoadMore();
                    }
                }
            }
        } else {
            actionListRv.completeLoadMore();
        }
    }
}
