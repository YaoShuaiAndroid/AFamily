package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MyYYueView;
import com.family.afamily.activity.mvp.presents.MyYYuePresenter;
import com.family.afamily.adapters.MyYYueAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
import com.superrecycleview.superlibrary.utils.SuperDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class MyYYueActivity extends BaseActivity<MyYYuePresenter> implements MyYYueView ,SuperRecyclerView.LoadingListener{
    @BindView(R.id.yyue_list_rv)
    SuperRecyclerView yyueListRv;
    private MyYYueAdapter commonAdapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    private BasePageBean<Map<String, String>> basePageBean;
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_yyue);
        token = (String) SPUtils.get(mActivity, "token", "");

    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MyYYuePresenter initPresenter() {
        return new MyYYuePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的预约");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       // RecyclerViewDivider myDecoration = new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        yyueListRv.addItemDecoration(SuperDivider.newShapeDivider().setColor(R.color.divider_yu_yue).setStartSkipCount(1).setEndSkipCount(0).setSizeDp(10));
        yyueListRv.setLayoutManager(linearLayoutManager);
        yyueListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        yyueListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        yyueListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        yyueListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        yyueListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        yyueListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        commonAdapter = new MyYYueAdapter(mActivity,token,mList);
        commonAdapter.setPresenter(presenter);
        yyueListRv.setAdapter(commonAdapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token,1,1);
        }
    }

    @Override
    public void successData(BasePageBean<Map<String, String>> data, int getType ) {
        if(data!=null){
            this.basePageBean = data;
            if(basePageBean.getList_data()!=null&&!basePageBean.getList_data().isEmpty()){
                if(getType == 1){
                    mList.clear();
                }else if(getType == 2){
                    mList.clear();
                    yyueListRv.completeRefresh();
                }else{
                    yyueListRv.completeLoadMore();
                }
                mList.addAll(basePageBean.getList_data());
                commonAdapter.notifyDataSetChanged();
            }else{
                if(getType == 2){
                    yyueListRv.completeRefresh();
                }else if(getType == 3){
                    yyueListRv.completeLoadMore();
                }
            }
        }else{
            if(getType == 2){
                yyueListRv.completeRefresh();
            }else if(getType == 3){
                yyueListRv.completeLoadMore();
            }
        }
        
    }

    @Override
    public void updateData() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token,1,1);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token,1,2);
        } else {
            yyueListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (basePageBean != null) {
                if (basePageBean.getPage() < basePageBean.getTotle_page()) {
                    presenter.getData(token, basePageBean.getPage() + 1, 3);
                } else {
                    if (basePageBean.getTotle_page() == basePageBean.getPage()) {
                        yyueListRv.setNoMore(true);
                    } else {
                        yyueListRv.completeLoadMore();
                    }
                }
            }
        } else {
            yyueListRv.completeLoadMore();
        }
    }
}
