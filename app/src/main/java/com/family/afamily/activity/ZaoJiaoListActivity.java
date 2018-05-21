package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ZaoJaoListView;
import com.family.afamily.activity.mvp.presents.ZaoJiaoListPresenter;
import com.family.afamily.adapters.ZaoJiaoListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.utils.MyPopUtils;
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
 * Created by hp2015-7 on 2017/12/6.
 */

public class ZaoJiaoListActivity extends BaseActivity<ZaoJiaoListPresenter> implements SuperRecyclerView.LoadingListener, ZaoJaoListView {
    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    @BindView(R.id.zaoj_list_rv)
    SuperRecyclerView zaojListRv;

    private ZaoJiaoListAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String token;

    private List<Map<String, String>> typeList;
    private String typeId;
    private String type;
    private BasePageBean<Map<String, String>> basePageBean;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zao_jiao_list);
        token = (String) SPUtils.get(mActivity, "token", "");
        typeId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.base_title_tv)
    public void clickTitle() {
        if (typeList == null) {
            toast("未获取到类型，正在重新获取...");
            presenter.getTypeList(token);
        } else {
            MyPopUtils.showZjTypeDialog(mActivity, typeList, findViewById(R.id.zj_title_rl), new MyPopUtils.ClickItem() {
                @Override
                public void clickItem(String str, int p) {
                    if (!type.equals(str)) {
                        type = str;
                        typeId = typeList.get(p).get("id");
                        baseTitleTv.setText(str);
                        mList.clear();
                        adapter.notifyDataSetChanged();
                        presenter.getDataList(token, 1, typeId, 1, mList, zaojListRv, adapter);
                    }
                }
            });
        }
    }


    @Override
    public ZaoJiaoListPresenter initPresenter() {
        return new ZaoJiaoListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();


        baseTitleTv.setText(type);

        zaojListRv.setLayoutManager(new LinearLayoutManager(this));
        zaojListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        zaojListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        zaojListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        zaojListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        zaojListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        zaojListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new ZaoJiaoListAdapter(mActivity, mList);
        zaojListRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String, String> map = mList.get(position - 1);
                Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                intent.putExtra("id", map.get("id"));
                intent.putExtra("study", map.get("look"));
                startActivity(intent);
            }
        });
        if (Utils.isConnected(mActivity)) {
            presenter.getTypeList(token);
            presenter.getDataList(token, 1, typeId, 1, mList, zaojListRv, adapter);
        }
    }

    @Override
    public void successTypeData(List<Map<String, String>> data) {
        if (data != null && data.size() > 0) {
            typeList = data;
        }
    }

    @Override
    public void successData(BasePageBean<Map<String, String>> data) {
        if (data != null) {
            basePageBean = data;
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getDataList(token, 1, typeId, 2, mList, zaojListRv, adapter);
        } else {
            zaojListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (basePageBean != null) {
                if (basePageBean.getPage() < basePageBean.getTotle_page()) {
                    presenter.getDataList(token, basePageBean.getPage() + 1, typeId, 3, mList, zaojListRv, adapter);
                } else {
                    if (basePageBean.getTotle_page() == basePageBean.getPage()) {
                        zaojListRv.setNoMore(true);
                    } else {
                        zaojListRv.completeLoadMore();
                    }
                }
            }
        } else {
            zaojListRv.completeLoadMore();
        }
    }
}
