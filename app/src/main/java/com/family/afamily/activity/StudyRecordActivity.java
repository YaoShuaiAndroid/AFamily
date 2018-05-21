package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.StudyRecordView;
import com.family.afamily.activity.mvp.presents.StudyRecordPresenter;
import com.family.afamily.adapters.StudyRecordAdapter;
import com.family.afamily.entity.StudyRecordData;
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
 * Created by hp2015-7 on 2017/12/14.
 */

public class StudyRecordActivity extends BaseActivity<StudyRecordPresenter> implements StudyRecordView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.study_record_day_tv)
    TextView studyRecordDayTv;
    @BindView(R.id.study_record_day_tab)
    LinearLayout studyRecordDayTab;
    @BindView(R.id.study_record_pm_tv)
    TextView studyRecordPmTv;
    @BindView(R.id.study_record_pm_tab)
    LinearLayout studyRecordPmTab;
    @BindView(R.id.study_record_yd_tv)
    TextView studyRecordYdTv;
    @BindView(R.id.study_record_yd_tab)
    LinearLayout studyRecordYdTab;
    @BindView(R.id.study_record_list_rv)
    SuperRecyclerView studyRecordListRv;
    private StudyRecordAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    StudyRecordData recordData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_study_record);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public StudyRecordPresenter initPresenter() {
        return new StudyRecordPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "学习记录");

        studyRecordPmTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FriendRankingActivity.class);
            }
        });

        studyRecordListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        studyRecordListRv.addItemDecoration(divider);
        studyRecordListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        studyRecordListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        studyRecordListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        studyRecordListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        studyRecordListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        studyRecordListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new StudyRecordAdapter(mActivity, mList);
        studyRecordListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, studyRecordListRv, adapter);
        }

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                intent.putExtra("id", mList.get(position-1).get("id"));
                intent.putExtra("study", "Y");
                startActivity(intent);
            }
        });
    }


    @Override
    public void successData(StudyRecordData data) {
        if (data != null) {
            recordData = data;
            studyRecordDayTv.setText(data.getStudy_day());
            studyRecordPmTv.setText(data.getRank());
            studyRecordYdTv.setText(data.getStudy_count());
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 2, mList, studyRecordListRv, adapter);
        } else {
            studyRecordListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (recordData != null) {
                if (recordData.getPage() < recordData.getTotle_page()) {
                    presenter.getData(token, recordData.getPage() + 1, 3, mList, studyRecordListRv, adapter);
                } else {
                    if (recordData.getTotle_page() == recordData.getPage()) {
                        studyRecordListRv.setNoMore(true);
                    } else {
                        studyRecordListRv.completeLoadMore();
                    }
                }
            }
        } else {
            studyRecordListRv.completeLoadMore();
        }
    }
}
