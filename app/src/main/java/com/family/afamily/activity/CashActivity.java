package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.CashView;
import com.family.afamily.activity.mvp.presents.CashPresenter;
import com.family.afamily.adapters.CashAdapter;
import com.family.afamily.entity.CashData;
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
 * Created by hp2015-7 on 2017/12/13.
 */

public class CashActivity extends BaseActivity<CashPresenter> implements CashView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.cash_money)
    TextView cashMoney;
    @BindView(R.id.cash_list_rv)
    SuperRecyclerView cashListRv;
    @BindView(R.id.cash_withdrawals_btn)
    TextView cashWithdrawalsBtn;

    private CashAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    private CashData cashData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.cash_withdrawals_btn)
    public void clickCashBtn() {
        if (cashData != null) {
            Integer bind = cashData.getBind_bank();
            if (bind != null && bind == 1) {
                Integer isWith = cashData.getAudit_status();
                if (isWith == 0) {
                    startActivityForResult(WithdrawalsActivity.class, 100);
                } else {
                    toast("您有一笔提现在审核中，暂时不可提现");
                }
            } else {
                startActivityForResult(BindBankActivity.class, 100);
            }
        } else {
            toast("未获取到用户信息");
        }
    }

    @Override
    public CashPresenter initPresenter() {
        return new CashPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的现金");

        cashListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        cashListRv.addItemDecoration(divider);
        cashListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        cashListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        cashListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        cashListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        cashListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        cashListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new CashAdapter(mActivity, mList);
        cashListRv.setAdapter(adapter);
//        cashListRv.setAdapter(adapter);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1);
        }
    }


    @Override
    public void successData(CashData cashData, int getType, boolean isOK) {
        if (isOK) {
            if (cashData != null) {
                this.cashData = cashData;

                cashMoney.setText(cashData.getUser_money() + "");

                if (cashData.getList_data() != null && cashData.getList_data().size() > 0) {
                    if (getType == 1) {
                        mList.clear();
                    } else if (getType == 2) {
                        cashListRv.completeRefresh();
                        mList.clear();
                    } else {
                        cashListRv.completeLoadMore();
                    }
                    mList.addAll(cashData.getList_data());
                    adapter.notifyDataSetChanged();
                } else {
                    if (getType == 2) {
                        cashListRv.completeRefresh();
                    } else {
                        cashListRv.completeLoadMore();
                    }
                    mList.clear();
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (getType == 2) {
                    cashListRv.completeRefresh();
                } else {
                    cashListRv.completeLoadMore();
                }
                mList.clear();
                adapter.notifyDataSetChanged();
            }
        } else {
            if (getType == 2) {
                cashListRv.completeRefresh();
            } else {
                cashListRv.completeLoadMore();
            }
            mList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 2);
        } else {
            cashListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (cashData != null) {
                if (cashData.getPage() < cashData.getTotle_page()) {
                    presenter.getData(token, cashData.getPage() + 1, 3);
                } else {
                    if (cashData.getTotle_page() == cashData.getPage()) {
                        cashListRv.setNoMore(true);
                    } else {
                        cashListRv.completeLoadMore();
                    }
                }
            }
        } else {
            cashListRv.completeLoadMore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            presenter.getData(token, 1, 1);
        }
    }
}
