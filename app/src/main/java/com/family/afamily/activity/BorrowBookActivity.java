package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BorrowBookView;
import com.family.afamily.activity.mvp.presents.BorrowBookPresenter;
import com.family.afamily.adapters.BorrowBookAdapter;
import com.family.afamily.entity.BorrowBookData;
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

public class BorrowBookActivity extends BaseActivity<BorrowBookPresenter> implements BorrowBookView, SuperRecyclerView.LoadingListener {
    ImageView borrowImg;
    TextView borrowName;
    TextView borrowTime;
    RelativeLayout borrowMyRl;

    @BindView(R.id.borrow_list_rv)
    SuperRecyclerView borrowListRv;

    private List<Map<String, String>> mList = new ArrayList<>();
    private BorrowBookAdapter adapter;
    private String token;
    private BorrowBookData bookData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_borrow_book);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public BorrowBookPresenter initPresenter() {
        return new BorrowBookPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的借阅");
        borrowListRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        borrowListRv.addItemDecoration(divider);
        borrowListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        borrowListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        borrowListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        borrowListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        borrowListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        borrowListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new BorrowBookAdapter(mActivity, mList);
        borrowListRv.setAdapter(adapter);
        addHeadView();
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1);
        }
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_borrow_book, (ViewGroup) borrowListRv.getParent(), false);
        borrowImg = headView.findViewById(R.id.borrow_img);
        borrowName = headView.findViewById(R.id.borrow_name);
        borrowTime = headView.findViewById(R.id.borrow_time);
        borrowMyRl = headView.findViewById(R.id.borrow_my_rl);

        adapter.addHeaderView(headView);
    }

    @Override
    public void successData(BorrowBookData bookData, int getType, boolean isOk) {
        if (isOk) {
            if (bookData != null) {
                this.bookData = bookData;
                Map<String, String> mData = bookData.getUnreturn();
                if (mData != null && mData.size() > 0) {
                    borrowMyRl.setVisibility(View.VISIBLE);
                    borrowName.setText(mData.get("goods_name"));
                    borrowTime.setText("到期：" + mData.get("end_time"));

                    RequestOptions options2 = new RequestOptions();
                    options2.error(R.drawable.error_pic);
                    Glide.with(mActivity).load(mData.get("picture")).apply(options2).into(borrowImg);
                } else {
                    borrowMyRl.setVisibility(View.GONE);
                }
                List<Map<String, String>> listData = bookData.getList_data();
                if (listData != null && listData.size() > 0) {
                    if (getType == 2) {
                        mList.clear();
                        borrowListRv.completeRefresh();
                    } else if (getType == 3) {
                        borrowListRv.completeLoadMore();
                    }
                    mList.addAll(listData);
                    adapter.notifyDataSetChanged();
                } else {
                    if (getType == 2) {
                        mList.clear();
                        borrowListRv.completeRefresh();
                    } else if (getType == 3) {
                        borrowListRv.completeLoadMore();
                    }
                    mList.addAll(listData);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 2);
        } else {
            borrowListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (bookData != null) {
                if (bookData.getPage() < bookData.getTotle_page()) {
                    presenter.getData(token, bookData.getPage() + 1, 3);
                } else {
                    if (bookData.getTotle_page() == bookData.getPage()) {
                        borrowListRv.setNoMore(true);
                    } else {
                        borrowListRv.completeLoadMore();
                    }
                }
            }
        } else {
            borrowListRv.completeLoadMore();
        }
    }
}
