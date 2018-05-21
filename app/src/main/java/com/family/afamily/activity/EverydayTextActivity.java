package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.EveyDayTextView;
import com.family.afamily.activity.mvp.presents.EveyDayTextPresenter;
import com.family.afamily.adapters.EverydayTextAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 每日美文
 * Created by hp2015-7 on 2017/11/30.
 */

public class EverydayTextActivity extends BaseActivity<EveyDayTextPresenter> implements SuperRecyclerView.LoadingListener, EveyDayTextView {
    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    @BindView(R.id.every_list_rv)
    SuperRecyclerView everyListRv;

    private EverydayTextAdapter commonAdapter;
    private List<Map<String, Object>> mList = new ArrayList<>();
    private String token;
    private BasePageBean basePageBean;
    private int page = 1;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_everyday_text);
        token = (String) SPUtils.get(mActivity, "token", "");
        if (TextUtils.isEmpty(token)) {
            startActivityForResult(LoginActivity.class, 55);
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public EveyDayTextPresenter initPresenter() {
        return new EveyDayTextPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "每日美文");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        everyListRv.setLayoutManager(layoutManager);
        everyListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        everyListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        everyListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        everyListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        everyListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        everyListRv.setArrowImageView(R.mipmap.iconfont_downgrey);// 设置下拉箭头
        RecyclerViewLoadDivider myDecoration = new RecyclerViewLoadDivider(mActivity, LinearLayoutManager.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        everyListRv.addItemDecoration(myDecoration);

        commonAdapter = new EverydayTextAdapter(mActivity, mList);
        everyListRv.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String, Object> map = mList.get(position-1);
                Bundle bundle = new Bundle();
                bundle.putString("id", map.get("id") + "");
                startActivity(EverydayTextDetailsActivity.class, bundle);
            }
        });

        if (Utils.isConnected(mActivity)) {
            presenter.getListData(token, page, everyListRv, 1, mList, commonAdapter);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            page = 1;
            presenter.getListData(token, page, everyListRv, 2, mList, commonAdapter);
        } else {
            everyListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (basePageBean != null) {
                if (basePageBean.getPage() < basePageBean.getTotle_page()) {
                    presenter.getListData(token, basePageBean.getPage() + 1, everyListRv, 3, mList, commonAdapter);
                } else {
                    if (basePageBean.getTotle_page() == basePageBean.getPage()) {
                        everyListRv.setNoMore(true);
                    } else {
                        everyListRv.completeLoadMore();
                    }
                }
            }
        } else {
            everyListRv.completeLoadMore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void dataCallback(BasePageBean<Map<String, Object>> dataBean) {
        basePageBean = dataBean;
    }
}
