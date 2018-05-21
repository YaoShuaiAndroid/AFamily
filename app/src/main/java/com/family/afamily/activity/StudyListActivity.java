package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.StudyListView;
import com.family.afamily.activity.mvp.presents.StudyListPresenter;
import com.family.afamily.adapters.StudyListAdapter;
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
 * Created by hp2015-7 on 2017/12/11.
 */

public class StudyListActivity extends BaseActivity<StudyListPresenter> implements SuperRecyclerView.LoadingListener, StudyListView {
    @BindView(R.id.study_default_tv)
    TextView studyDefaultTv;
    @BindView(R.id.study_default_v)
    View studyDefaultV;
    @BindView(R.id.study_volume_tv)
    TextView studyVolumeTv;
    @BindView(R.id.study_volume_up_iv)
    ImageView studyVolumeUpIv;
    @BindView(R.id.study_volume_d_iv)
    ImageView studyVolumeDIv;
    @BindView(R.id.study_volume_v)
    View studyVolumeV;
    @BindView(R.id.study_volume_rl)
    RelativeLayout studyVolumeRl;
    @BindView(R.id.study_price_tv)
    TextView studyPriceTv;
    @BindView(R.id.study_price_up_iv)
    ImageView studyPriceUpIv;
    @BindView(R.id.study_price_d_iv)
    ImageView studyPriceDIv;
    @BindView(R.id.study_price_v)
    View studyPriceV;
    @BindView(R.id.study_price_rl)
    RelativeLayout studyPriceRl;
    @BindView(R.id.study_list_rv)
    SuperRecyclerView studyListRv;
    private List<Map<String, String>> list = new ArrayList<>();
    private StudyListAdapter adapter;
    private String cat_id;
    private String token;
    private String sort = "goods_id", order = "";
    private int index = 1;
    private BasePageBean pageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_study_list);
        cat_id = getIntent().getStringExtra("cat_id");
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.study_default_tv)
    public void clickDefault() {
        if (index != 1) {
            index = 1;
            initView();
            studyDefaultTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
            studyDefaultV.setVisibility(View.VISIBLE);
            sort = "goods_id";
            order = "";
            studyListRv.completeRefresh();
            list.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, cat_id, sort, order, 1, 1, list, studyListRv, adapter);
            }
        }
    }

    @OnClick(R.id.study_volume_rl)
    public void clickVolume() {
        index = 2;
        initView();
        studyVolumeTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
        studyVolumeV.setVisibility(View.VISIBLE);
        sort = "sales_count";

        if (TextUtils.isEmpty(order)) {
            order = "ASC";
            studyVolumeDIv.setImageResource(R.mipmap.ic_list_d_1);
            studyVolumeUpIv.setImageResource(R.mipmap.ic_list_up_2);
        } else if (order.equals("ASC")) {
            order = "DESC";
            studyVolumeDIv.setImageResource(R.mipmap.ic_list_d_2);
            studyVolumeUpIv.setImageResource(R.mipmap.ic_list_up_1);
        } else {
            order = "ASC";
            studyVolumeDIv.setImageResource(R.mipmap.ic_list_d_1);
            studyVolumeUpIv.setImageResource(R.mipmap.ic_list_up_2);
        }
        studyListRv.completeRefresh();
        list.clear();
        adapter.notifyDataSetChanged();
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, cat_id, sort, order, 1, 1, list, studyListRv, adapter);
        }
    }

    @OnClick(R.id.study_price_rl)
    public void clickPrice() {
        index = 3;
        initView();
        sort = "shop_price";
        studyPriceTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
        studyPriceV.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(order)) {
            order = "ASC";
            studyPriceDIv.setImageResource(R.mipmap.ic_list_d_1);
            studyPriceUpIv.setImageResource(R.mipmap.ic_list_up_2);
        } else if (order.equals("ASC")) {
            order = "DESC";
            studyPriceDIv.setImageResource(R.mipmap.ic_list_d_2);
            studyPriceUpIv.setImageResource(R.mipmap.ic_list_up_1);
        } else {
            order = "ASC";
            studyPriceDIv.setImageResource(R.mipmap.ic_list_d_1);
            studyPriceUpIv.setImageResource(R.mipmap.ic_list_up_2);
        }
        studyListRv.completeRefresh();
        list.clear();
        adapter.notifyDataSetChanged();
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, cat_id, sort, order, 1, 1, list, studyListRv, adapter);
        }
    }

    private void initView() {
        studyDefaultTv.setTextColor(Color.parseColor("#333333"));
        studyDefaultV.setVisibility(View.INVISIBLE);

        studyVolumeTv.setTextColor(Color.parseColor("#333333"));
        studyVolumeV.setVisibility(View.INVISIBLE);
        studyVolumeDIv.setImageResource(R.mipmap.ic_list_d_1);
        studyVolumeUpIv.setImageResource(R.mipmap.ic_list_up_1);

        studyPriceTv.setTextColor(Color.parseColor("#333333"));
        studyPriceV.setVisibility(View.INVISIBLE);
        studyPriceUpIv.setImageResource(R.mipmap.ic_list_up_1);
        studyPriceDIv.setImageResource(R.mipmap.ic_list_d_1);
    }


    @Override
    public StudyListPresenter initPresenter() {
        return new StudyListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        if (cat_id.equals("39")) {
            setTitle(this, "0-3岁");
        } else if (cat_id.equals("40")) {
            setTitle(this, "3-6岁");
        } else {
            setTitle(this, "6岁以上");
        }

        studyListRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        studyListRv.addItemDecoration(divider);
        studyListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        studyListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        studyListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        studyListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        studyListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        studyListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new StudyListAdapter(mActivity, list);
        studyListRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String, String> data = list.get(position - 1);
               // Intent intent = new Intent(mActivity, StudyDetailsActivity.class);
                Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                intent.putExtra("goods_id", data.get("goods_id"));
                startActivity(intent);
            }
        });

        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, cat_id, sort, order, 1, 1, list, studyListRv, adapter);
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
            presenter.getData(token, cat_id, sort, order, 1, 2, list, studyListRv, adapter);
        } else {
            studyListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, cat_id, sort, order, pageBean.getPage() + 1, 3, list, studyListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        studyListRv.setNoMore(true);
                    } else {
                        studyListRv.completeLoadMore();
                    }
                }
            }
        } else {
            studyListRv.completeLoadMore();
        }
    }
}
