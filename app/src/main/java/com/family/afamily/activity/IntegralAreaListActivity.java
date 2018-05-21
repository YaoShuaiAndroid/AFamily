package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.IntegralAreaListPresenter;
import com.family.afamily.adapters.IntegralAreaListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.GridSpacingItemDecoration;
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
 * Created by hp2015-7 on 2017/12/13.
 */

public class IntegralAreaListActivity extends BaseActivity<IntegralAreaListPresenter> implements PageSuccessView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.integral_list_rv)
    SuperRecyclerView integralListRv;
    @BindView(R.id.intgl_all_tv)
    TextView intglAllTv;
    @BindView(R.id.intgl_all_v)
    View intglAllV;
    @BindView(R.id.intgl_hb_tv)
    TextView intglHbTv;
    @BindView(R.id.intgl_hb_v)
    View intglHbV;
    @BindView(R.id.intgl_coupon_tv)
    TextView intglCouponTv;
    @BindView(R.id.intgl_coupon_v)
    View intglCouponV;
    @BindView(R.id.intgl_jj_tv)
    TextView intglJjTv;
    @BindView(R.id.intgl_jj_v)
    View intglJjV;
    private IntegralAreaListAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private String token;
    private String cat_id;
    private int index_tab = 1;
    private BasePageBean pageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_integral_area_list);
        cat_id = getIntent().getStringExtra("cat_id");
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @OnClick(R.id.intgl_all_tv)
    public void clickAll() {
        if (index_tab != 1) {
            cat_id = "0";
            index_tab = 1;
            initView(intglAllTv, intglAllV);
            integralListRv.completeRefresh();
            list.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, cat_id, 1, 1, list, integralListRv, adapter);
        }
    }

    @OnClick(R.id.intgl_hb_tv)
    public void clickBuiBen() {
        if (index_tab != 2) {
            cat_id = "1";
            index_tab = 2;
            initView(intglHbTv, intglHbV);
            integralListRv.completeRefresh();
            list.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, cat_id, 1, 1, list, integralListRv, adapter);
        }
    }

    @OnClick(R.id.intgl_coupon_tv)
    public void clickCoupon() {
        if (index_tab != 3) {
            cat_id = "10";
            index_tab = 3;
            initView(intglCouponTv, intglCouponV);
            integralListRv.completeRefresh();
            list.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, cat_id, 1, 1, list, integralListRv, adapter);
        }
    }

    @OnClick(R.id.intgl_jj_tv)
    public void clickJiaoJu() {
        if (index_tab != 4) {
            cat_id = "41";
            index_tab = 4;
            initView(intglJjTv, intglJjV);
            integralListRv.completeRefresh();
            list.clear();
            adapter.notifyDataSetChanged();
            presenter.getData(token, cat_id, 1, 1, list, integralListRv, adapter);
        }
    }

    public void initView(TextView tv, View v) {
        intglAllTv.setTextColor(Color.parseColor("#333333"));
        intglHbTv.setTextColor(Color.parseColor("#333333"));
        intglCouponTv.setTextColor(Color.parseColor("#333333"));
        intglJjTv.setTextColor(Color.parseColor("#333333"));
        intglAllV.setVisibility(View.INVISIBLE);
        intglHbV.setVisibility(View.INVISIBLE);
        intglCouponV.setVisibility(View.INVISIBLE);
        intglJjV.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public IntegralAreaListPresenter initPresenter() {
        return new IntegralAreaListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "积分分类");

        if (cat_id.equals("0")) {
            cat_id = "0";
            index_tab = 1;
            initView(intglAllTv, intglAllV);
        } else if (cat_id.equals("1")) {
            cat_id = "1";
            index_tab = 2;
            initView(intglHbTv, intglHbV);
        } else if (cat_id.equals("10")) {
            cat_id = "10";
            index_tab = 3;
            initView(intglCouponTv, intglCouponV);
        } else {
            cat_id = "41";
            index_tab = 4;
            initView(intglJjTv, intglJjV);
        }


        GridLayoutManager mgr = new GridLayoutManager(mActivity, 2);
        integralListRv.setLayoutManager(mgr);
        integralListRv.addItemDecoration(new GridSpacingItemDecoration(1, 2, false));
        integralListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        integralListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        integralListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        integralListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        integralListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        integralListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new IntegralAreaListAdapter(mActivity, list);
        integralListRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, IntegralDetailsActivity.class);
                String type = index_tab == 3 ? "2" : "1";
                intent.putExtra("goods_id", list.get(position - 1).get("goods_id"));
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, cat_id, 1, 1, list, integralListRv, adapter);
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
            presenter.getData(token, cat_id, 1, 2, list, integralListRv, adapter);
        } else {
            integralListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, cat_id, pageBean.getPage() + 1, 3, list, integralListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        integralListRv.setNoMore(true);
                    } else {
                        integralListRv.completeLoadMore();
                    }
                }
            }
        } else {
            integralListRv.completeLoadMore();
        }
    }
}
