package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.FriendRankingPresenter;
import com.family.afamily.adapters.FriendRankingAdapter;
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

public class FriendRankingActivity extends BaseActivity<FriendRankingPresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.friend_list_rv)
    SuperRecyclerView friendListRv;
    private FriendRankingAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend_ranking);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public FriendRankingPresenter initPresenter() {
        return new FriendRankingPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();

        friendListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        friendListRv.addItemDecoration(divider);
        friendListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        friendListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        friendListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        friendListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        friendListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        friendListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new FriendRankingAdapter(mActivity, mList);
        friendListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, friendListRv, adapter);
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
            presenter.getData(token, 1, 2, mList, friendListRv, adapter);
        } else {
            friendListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, pageBean.getPage() + 1, 3, mList, friendListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        friendListRv.setNoMore(true);
                    } else {
                        friendListRv.completeLoadMore();
                    }
                }
            }
        } else {
            friendListRv.completeLoadMore();
        }
    }
}
