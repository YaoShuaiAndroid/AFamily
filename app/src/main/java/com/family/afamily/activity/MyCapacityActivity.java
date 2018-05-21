package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MyCapacityView;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.activity.mvp.presents.MyCapacityPresenter;
import com.family.afamily.adapters.MyCapacityAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.entity.PdfModel;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
import com.superrecycleview.superlibrary.utils.SuperDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCapacityActivity extends BaseActivity<MyCapacityPresenter>
        implements SuperRecyclerView.LoadingListener,MyCapacityView {
    @BindView(R.id.capacity_list_rv)
    SuperRecyclerView capacityListRv;

    private MyCapacityAdapter myCapacityAdapter;

    private List<PdfModel> mList = new ArrayList<>();

    private int page=1;
    private int pages=1;

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MyCapacityPresenter initPresenter() {
        return new MyCapacityPresenter(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_capacity);

        setTitle(mActivity, "我的先天智能");

        getData(1);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // RecyclerViewDivider myDecoration = new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        capacityListRv.addItemDecoration(SuperDivider.newShapeDivider().setColor(R.color.divider_yu_yue).setStartSkipCount(1).setEndSkipCount(0).setSizeDp(10));
        capacityListRv.setLayoutManager(linearLayoutManager);
        capacityListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        capacityListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        capacityListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        capacityListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        capacityListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        capacityListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        myCapacityAdapter = new MyCapacityAdapter(mActivity, mList);
        capacityListRv.setAdapter(myCapacityAdapter);

        myCapacityAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyCapacityDetailActivity.start(mActivity,
                        mList.get(position-1).getPdf_name(),
                        mList.get(position-1).getUrls(),
                        mList.get(position-1).getId());
            }
        });
    }

    public void getData(int type) {
        String token= (String) SPUtils.get(mActivity,"token","");

        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mActivity,LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getData(token,type);
        } else {
            toast("网络异常");
        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            page=1;
            getData(2);
        } else {
            capacityListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (mList != null) {
                if (page <pages) {
                    page++;
                    getData(3);
                } else {
                    if (pages== page) {
                        capacityListRv.setNoMore(true);
                    } else {
                        capacityListRv.completeLoadMore();
                    }
                }
            }
        } else {
            capacityListRv.completeLoadMore();
        }
    }

    @Override
    public void successData(BasePageBean<PdfModel> data, int type) {
        if (data != null) {
            if(type==1){
                this.mList.addAll(data.getList_data());
            }else if (type == 2) {
                this.mList.clear();
                this.mList.addAll(data.getList_data());
                capacityListRv.completeRefresh();
            }else if (type == 3) {
                this.mList.addAll(data.getList_data());
                capacityListRv.completeRefresh();
            }

            pages=data.getPages();
            page=data.getPage();

            myCapacityAdapter.notifyDataSetChanged();
        }
    }
}
