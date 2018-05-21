package com.family.afamily.activity.detection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.family.afamily.R;
import com.family.afamily.activity.InnateDetailActivity;
import com.family.afamily.activity.LoginActivity;
import com.family.afamily.activity.mvp.interfaces.InnateIntelligenceView;
import com.family.afamily.activity.mvp.presents.InnateIntelligencePresenter;
import com.family.afamily.adapters.InnateIntelligenceAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.fragment.base.LazyFragment;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InnateIntelligenceFragment extends LazyFragment<InnateIntelligencePresenter>
        implements SuperRecyclerView.LoadingListener,InnateIntelligenceView {
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @BindView(R.id.innate_recy)
    SuperRecyclerView superRecyclerView;
    Unbinder unbinder;

    private Activity mActivity;

    private List<InnateIntelligenceModel> mList = new ArrayList<>();

    private int page=1;
    private int pages=1;

    private InnateIntelligenceAdapter adapter;

    private DetectListActivity detectListActivity;

    public static InnateIntelligenceFragment newInstance(String category, int multiType) {
        InnateIntelligenceFragment maintenanceFragment = new InnateIntelligenceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_innate_intelligence_fragment, container, false);

        mActivity=getActivity();

        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    @Override
    protected void loadData() {
        getData(1);
    }

    public void getData(int type) {
        String user_id= (String) SPUtils.get(mActivity,"user_id","");



        if (AppUtil.checkNetWork(mActivity)) {
                presenter.getData(user_id,page,type);
        } else {
            toast("网络异常");
        }
    }

    @Override
    public InnateIntelligencePresenter initPresenter() {
        return new InnateIntelligencePresenter(this);
    }

    public void init(){
        superRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        superRecyclerView.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        superRecyclerView.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        superRecyclerView.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        superRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        superRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        superRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new InnateIntelligenceAdapter(mActivity, mList,handler);
        superRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mActivity, InnateDetailActivity.class);
                intent.putExtra("id",mList.get(position-1).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            page=1;
            getData(2);
        } else {
            superRecyclerView.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (mList != null) {
                if (page <pages) {
                    page++;
                    getData(3);
                } else {
                    if (pages== page) {
                        superRecyclerView.setNoMore(true);
                    } else {
                        superRecyclerView.completeLoadMore();
                    }
                }
            }
        } else {
            superRecyclerView.completeLoadMore();
        }
    }

    @Override
    public void successData(BasePageBean<InnateIntelligenceModel> data, int type) {
        if (type == 2) {
            this.mList.clear();
            superRecyclerView.completeRefresh();
        } else if (type == 3) {
            superRecyclerView.completeRefresh();
        }

        if (data != null) {
            this.mList.addAll(data.getList_data());

            pages=data.getPages();
            page=data.getPage();

            adapter.notifyDataSetChanged();
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1002:
                    Intent intent=new Intent(mActivity, InnateDetailActivity.class);
                    intent.putExtra("id",""+msg.obj);
                    startActivity(intent);
                    break;
            }
        }
    };
}
