package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.CollectionPresenter;
import com.family.afamily.adapters.CollectionAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.L;
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
 * Created by hp2015-7 on 2017/12/12.
 */

public class CollectionActivity extends BaseActivity<CollectionPresenter> implements SuperRecyclerView.LoadingListener, PageSuccessView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.collection_text_tv)
    TextView collectionTextTv;
    @BindView(R.id.collection_text_v)
    View collectionTextV;
    @BindView(R.id.collection_video_tv)
    TextView collectionVideoTv;
    @BindView(R.id.collection_video_v)
    View collectionVideoV;
    @BindView(R.id.collection_book_tv)
    TextView collectionBookTv;
    @BindView(R.id.collection_book_v)
    View collectionBookV;
    @BindView(R.id.collection_play_tv)
    TextView collectionPlayTv;
    @BindView(R.id.collection_play_v)
    View collectionPlayV;
    @BindView(R.id.collection_list_rv)
    SuperRecyclerView collectionListRv;
    @BindView(R.id.collection_del_tv)
    TextView collectionDelTv;

    private BasePageBean pageBean;
    private CollectionAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;
    private int type = 1;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.collection_text_tv)
    public void clickText() {
        int flag = (int) baseTitleRightTv.getTag();
        if (flag == 1) return;
        if (type != 1) {
            type = 1;
            initView(collectionTextTv, collectionTextV);
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
            }
        }
    }

    @OnClick(R.id.collection_video_tv)
    public void clickVideo() {
        int flag = (int) baseTitleRightTv.getTag();
        if (flag == 1) return;
        if (type != 2) {
            type = 2;
            initView(collectionVideoTv, collectionVideoV);
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
            }
        }
    }

    @OnClick(R.id.collection_book_tv)
    public void clickBook() {
        int flag = (int) baseTitleRightTv.getTag();
        if (flag == 1) return;
        if (type != 3) {
            type = 3;
            initView(collectionBookTv, collectionBookV);
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
            }
        }
    }

    @OnClick(R.id.collection_play_tv)
    public void clickPlay() {
        int flag = (int) baseTitleRightTv.getTag();
        if (flag == 1) return;
        if (type != 4) {
            type = 4;
            initView(collectionPlayTv, collectionPlayV);
            mList.clear();
            adapter.notifyDataSetChanged();
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
            }
        }
    }

    @OnClick(R.id.base_title_right_tv)
    public void clickEdit() {
        // if(mList!=null&&mList.size()>0){
        int flag = (int) baseTitleRightTv.getTag();
        if (flag == 0) {
            adapter.setShowCheck(true);
            adapter.notifyDataSetChanged();
            baseTitleRightTv.setText("完成");
            baseTitleRightTv.setTag(1);
            collectionDelTv.setVisibility(View.VISIBLE);
        } else {
            adapter.setShowCheck(false);
            adapter.notifyDataSetChanged();
            baseTitleRightTv.setText("编辑");
            baseTitleRightTv.setTag(0);
            collectionDelTv.setVisibility(View.GONE);
        }
        // }else{
        //    toast("您没有可编辑藏品");
        //  }

    }

    @OnClick(R.id.collection_del_tv)
    public void clickDel() {
        if (mList.size() > 0) {
            String str_id = "";
            for (int i = 0; i < mList.size(); i++) {
                Map<String, String> data = mList.get(i);
                if (!TextUtils.isEmpty(data.get("isCheck")) && data.get("isCheck").equals("1")) {
                    str_id += data.get("id") + ",";
                }
            }
            if (TextUtils.isEmpty(str_id)) {
                toast("请选择您要删除的收藏");
            } else {
                L.e("tag", "---------1------>" + str_id);
                str_id = str_id.substring(0, str_id.length() - 1);
                L.e("tag", "-----2---------->" + str_id);
                if (Utils.isConnected(mActivity)) {
                    presenter.cancelCollect(token, str_id, type + "", this);
                }
            }
        }
    }

    private void initView(TextView tv, View v) {
        collectionTextTv.setTextColor(Color.parseColor("#333333"));
        collectionVideoTv.setTextColor(Color.parseColor("#333333"));
        collectionBookTv.setTextColor(Color.parseColor("#333333"));
        collectionPlayTv.setTextColor(Color.parseColor("#333333"));
        collectionTextV.setVisibility(View.INVISIBLE);
        collectionVideoV.setVisibility(View.INVISIBLE);
        collectionBookV.setVisibility(View.INVISIBLE);
        collectionPlayV.setVisibility(View.INVISIBLE);

        v.setVisibility(View.VISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));
    }

    public void updataData() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
        }
    }

    @Override
    public CollectionPresenter initPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的收藏");
        baseTitleRightTv.setText("编辑");
        baseTitleRightTv.setTag(0);
        collectionListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        collectionListRv.addItemDecoration(divider);
        collectionListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        collectionListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        collectionListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        collectionListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        collectionListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        collectionListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new CollectionAdapter(mActivity, mList, type);
        collectionListRv.setAdapter(adapter);

        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, type, 1, 1, mList, collectionListRv, adapter);
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
            presenter.getData(token, type, 1, 2, mList, collectionListRv, adapter);
        } else {
            collectionListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, type, pageBean.getPage() + 1, 3, mList, collectionListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        collectionListRv.setNoMore(true);
                    } else {
                        collectionListRv.completeLoadMore();
                    }
                }
            }
        } else {
            collectionListRv.completeLoadMore();
        }
    }
}
