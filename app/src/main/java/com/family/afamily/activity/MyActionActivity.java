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
import com.family.afamily.activity.mvp.interfaces.MyActionView;
import com.family.afamily.activity.mvp.presents.MyActionPresenter;
import com.family.afamily.adapters.MyActionAdapter;
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

public class MyActionActivity extends BaseActivity<MyActionPresenter> implements MyActionView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.my_action_list_rv)
    SuperRecyclerView myActionListRv;
    @BindView(R.id.my_action_tab1_tv)
    TextView myActionTab1Tv;
    @BindView(R.id.my_action_tab1_v)
    View myActionTab1V;
    @BindView(R.id.my_action_tab2_tv)
    TextView myActionTab2Tv;
    @BindView(R.id.my_action_tab2_v)
    View myActionTab2V;
    private MyActionAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private int index = 1;
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_action);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @OnClick(R.id.my_action_tab1_tv)
    public void clickTab1() {
        if (index != 1) {
            index = 1;
            myActionTab1Tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            myActionTab2Tv.setTextColor(Color.parseColor("#333333"));
            myActionTab1V.setVisibility(View.VISIBLE);
            myActionTab2V.setVisibility(View.INVISIBLE);
            myActionListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myActionListRv, adapter);
            }
        }
    }

    @OnClick(R.id.my_action_tab2_tv)
    public void clickTab2() {
        if (index != 2) {
            index = 2;
            myActionTab1Tv.setTextColor(Color.parseColor("#333333"));
            myActionTab2Tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            myActionTab1V.setVisibility(View.INVISIBLE);
            myActionTab2V.setVisibility(View.VISIBLE);
            myActionListRv.completeRefresh();
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myActionListRv, adapter);
            }
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MyActionPresenter initPresenter() {
        return new MyActionPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的活动");
        myActionListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        myActionListRv.addItemDecoration(divider);
        myActionListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        myActionListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        myActionListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        myActionListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        myActionListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        myActionListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MyActionAdapter(mActivity, mList, presenter);
        myActionListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 1, mList, myActionListRv, adapter);
        }
    }

    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }

    @Override
    public void submitSuccess() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 1, mList, myActionListRv, adapter);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 2, mList, myActionListRv, adapter);
        } else {
            myActionListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, index, pageBean.getPage() + 1, 3, mList, myActionListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        myActionListRv.setNoMore(true);
                    } else {
                        myActionListRv.completeLoadMore();
                    }
                }
            }
        } else {
            myActionListRv.completeLoadMore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 100){
            presenter.getData(token, index, 1, 1, mList, myActionListRv, adapter);
        }
    }
}
