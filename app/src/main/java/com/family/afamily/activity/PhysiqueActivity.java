package com.family.afamily.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PhysiqueView;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.activity.mvp.presents.PhysiquePresenter;
import com.family.afamily.adapters.MyCapacityAdapter;
import com.family.afamily.adapters.PhysiqueAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
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

public class PhysiqueActivity extends BaseActivity<PhysiquePresenter>
        implements SuperRecyclerView.LoadingListener, PhysiqueView {
    @BindView(R.id.physique_list_rv)
    SuperRecyclerView physiqueListRv;

    private List<InnateIntelligenceModel> mList = new ArrayList<>();

    private PhysiqueAdapter physiqueAdapter;
    //行为id
    private String id;

    private int page = 1;
    private int pages = 1;

    public static void start(Context context, String id,String title) {
        Intent intent = new Intent(context, PhysiqueActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public PhysiquePresenter initPresenter() {
        return new PhysiquePresenter(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_physique);

        id = getIntent().getStringExtra("id");
        setTitle(mActivity, getIntent().getStringExtra("title"));

        getBehaviorInfo(1);
    }

    /**
     * 行为检测信息列表
     *
     * @param type
     */
    public void getBehaviorInfo(int type) {
        String user_id = (String) SPUtils.get(mActivity, "user_id", "");

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getBehaviorInfo(id, "" + page, user_id, type);
        } else {
            toast("网络异常");
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // RecyclerViewDivider myDecoration = new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        physiqueListRv.addItemDecoration(SuperDivider.newShapeDivider().setColor(R.color.divider_yu_yue).setStartSkipCount(1).setEndSkipCount(0).setSizeDp(10));
        physiqueListRv.setLayoutManager(linearLayoutManager);
        physiqueListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        physiqueListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        physiqueListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        physiqueListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        physiqueListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        physiqueListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        physiqueAdapter = new PhysiqueAdapter(mActivity, mList);
        physiqueListRv.setAdapter(physiqueAdapter);

        physiqueAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mActivity, ConductDetailActivity.class);
                intent.putExtra("id",mList.get(position-1).getId());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            page = 1;
            getBehaviorInfo(2);
        } else {
            physiqueListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (mList != null) {
                if (page < pages) {
                    page++;
                    getBehaviorInfo(3);
                } else {
                    if (pages == page) {
                        physiqueListRv.setNoMore(true);
                    } else {
                        physiqueListRv.completeLoadMore();
                    }
                }
            }
        } else {
            physiqueListRv.completeLoadMore();
        }
    }

    @Override
    public void successData(BasePageBean<InnateIntelligenceModel> data, int type) {
        if (type == 2) {
            this.mList.clear();
            physiqueListRv.completeRefresh();
        } else if (type == 3) {
            physiqueListRv.completeRefresh();
        }

        if (data != null) {
            this.mList.addAll(data.getList_data());

            pages = data.getPages();
            page = data.getPage();

            physiqueAdapter.notifyDataSetChanged();
        }
    }
}
