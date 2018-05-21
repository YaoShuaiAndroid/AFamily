package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.MyExtensionPresenter;
import com.family.afamily.adapters.MyExtensionAdapter;
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
 * Created by hp2015-7 on 2017/12/14.
 */

public class MyExtensionActivity extends BaseActivity<MyExtensionPresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.my_ext_list_rv)
    SuperRecyclerView myExtListRv;
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.ext_tab1_tv)
    TextView extTab1Tv;
    @BindView(R.id.ext_tab1_v)
    View extTab1V;
    @BindView(R.id.ext_tab2_tv)
    TextView extTab2Tv;
    @BindView(R.id.ext_tab2_v)
    View extTab2V;
    private MyExtensionAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();

    private int index = 1;
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_extension);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.ext_tab1_tv)
    public void clickTab1() {
        if (index != 1) {
            index = 1;
            extTab1Tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            extTab2Tv.setTextColor(Color.parseColor("#333333"));
            extTab1V.setVisibility(View.VISIBLE);
            extTab2V.setVisibility(View.INVISIBLE);
            myExtListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myExtListRv, adapter);
            }
        }
    }

    @OnClick(R.id.ext_tab2_tv)
    public void clickTab2() {
        if (index != 2) {
            index = 2;
            extTab2Tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            extTab1Tv.setTextColor(Color.parseColor("#333333"));
            extTab2V.setVisibility(View.VISIBLE);
            extTab1V.setVisibility(View.INVISIBLE);
            myExtListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myExtListRv, adapter);
            }
        }
    }

    @Override
    public MyExtensionPresenter initPresenter() {
        return new MyExtensionPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的推广");
        baseTitleRightTv.setText("去推广");

        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ToExtensionActivity.class);
            }
        });

        myExtListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        myExtListRv.addItemDecoration(divider);
        myExtListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        myExtListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        myExtListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        myExtListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        myExtListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        myExtListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MyExtensionAdapter(mActivity, mList);
        myExtListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 1, mList, myExtListRv, adapter);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 2, mList, myExtListRv, adapter);
        } else {
            myExtListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, index, pageBean.getPage() + 1, 3, mList, myExtListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        myExtListRv.setNoMore(true);
                    } else {
                        myExtListRv.completeLoadMore();
                    }
                }
            }
        } else {
            myExtListRv.completeLoadMore();
        }
    }

    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }
}
