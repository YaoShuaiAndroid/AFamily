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
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.FollowPresenter;
import com.family.afamily.adapters.FollowAdapter;
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
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/12.
 */

public class FollowActivity extends BaseActivity<FollowPresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.follow_my_tv)
    TextView followMyTv;
    @BindView(R.id.follow_my_v)
    View followMyV;
    @BindView(R.id.follow_friend_tv)
    TextView followFriendTv;
    @BindView(R.id.follow_friend_v)
    View followFriendV;
    @BindView(R.id.follow_list_rv)
    SuperRecyclerView followListRv;
    private FollowAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    private String type = "1";
    private BasePageBean pageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_follow);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.follow_my_tv)
    public void clickFollow() {
        if (!type.equals("1")) {
            type = "1";
            followMyTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            followMyV.setVisibility(View.VISIBLE);
            followFriendTv.setTextColor(Color.parseColor("#333333"));
            followFriendV.setVisibility(View.INVISIBLE);
            mList.clear();
            adapter.notifyDataSetChanged();
            followListRv.completeRefresh();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, 1, 1, type, mList, followListRv, adapter);
            }
        }
    }

    @OnClick(R.id.follow_friend_tv)
    public void clickFriend() {
        if (!type.equals("2")) {
            type = "2";
            followFriendTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            followFriendV.setVisibility(View.VISIBLE);
            followMyTv.setTextColor(Color.parseColor("#333333"));
            followMyV.setVisibility(View.INVISIBLE);
            mList.clear();
            adapter.notifyDataSetChanged();
            followListRv.completeRefresh();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, 1, 1, type, mList, followListRv, adapter);
            }
        }
    }

    @Override
    public FollowPresenter initPresenter() {
        return new FollowPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的关注");

        followListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        followListRv.addItemDecoration(divider);
        followListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        followListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        followListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        followListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        followListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        followListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new FollowAdapter(FollowActivity.this, mList, presenter);
        followListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, type, mList, followListRv, adapter);
        }

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, MasterActivity.class);
                intent.putExtra("user_id", mList.get(position - 1).get("user_id"));
                startActivityForResult(intent, 100);
            }
        });

    }

    public void updateData() {
        presenter.getData(token, 1, 1, type, mList, followListRv, adapter);
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
            presenter.getData(token, 1, 2, type, mList, followListRv, adapter);
        } else {
            followListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, pageBean.getPage() + 1, 3, type, mList, followListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        followListRv.setNoMore(true);
                    } else {
                        followListRv.completeLoadMore();
                    }
                }
            }
        } else {
            followListRv.completeLoadMore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            updateData();
        }
    }
}
