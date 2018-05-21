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
import android.widget.ImageView;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.LoginActivity;
import com.family.afamily.activity.ParentingDetailActivity;
import com.family.afamily.activity.mvp.interfaces.InnateIntelligenceView;
import com.family.afamily.activity.mvp.presents.InnateIntelligencePresenter;
import com.family.afamily.adapters.InnateIntelligenceAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.fragment.base.LazyFragment;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.AgeDialog;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParentingGuideFragment extends LazyFragment<InnateIntelligencePresenter>
        implements SuperRecyclerView.LoadingListener, InnateIntelligenceView {
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @BindView(R.id.innate_recy)
    SuperRecyclerView superRecyclerView;

    private TextView parentGuideYearText;

    Unbinder unbinder;

    private Activity mActivity;

    private int page = 1;
    private int pages=1;
    //选择观看的年龄资讯
    private int month = 1;

    private List<InnateIntelligenceModel> mList = new ArrayList<>();

    private InnateIntelligenceAdapter adapter;

    public static ParentingGuideFragment newInstance(String category, int multiType) {
        ParentingGuideFragment maintenanceFragment = new ParentingGuideFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    month = msg.arg1;

                    parentGuideArrowDown.setBackgroundResource(R.mipmap.arrow_down);

                    parentGuideYearText.setText(month/12+"岁"+month%12+"个月");

                    page=0;
                    getData(2);
                    break;
                case 2:
                    parentGuideArrowDown.setBackgroundResource(R.mipmap.arrow_down);
                    break;
                case 1002:
                    Intent intent=new Intent(mActivity, ParentingDetailActivity.class);
                    intent.putExtra("id",""+msg.obj);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_innate_intelligence_fragment, container, false);

        mActivity = getActivity();

        unbinder = ButterKnife.bind(this, view);

        init();
        return view;
    }


    @Override
    protected void loadData() {
        getData(1);
    }

    public void init() {
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
                Intent intent=new Intent(mActivity, ParentingDetailActivity.class);
                intent.putExtra("id",mList.get(position-1).getId());
                startActivity(intent);
            }
        });

        addHeadView();
    }

    private ImageView parentGuideArrowDown;

    private void addHeadView() {
        View headView = getActivity().getLayoutInflater()
                .inflate(R.layout.head_parent_guide_layout, (ViewGroup) superRecyclerView.getParent(), false);

        parentGuideYearText = headView.findViewById(R.id.parent_guide_year_text);
        parentGuideArrowDown = headView.findViewById(R.id.parent_guide_arrow_down);

        parentGuideYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentGuideArrowDown.setBackgroundResource(R.mipmap.arrow_up);

                new AgeDialog(mActivity, superRecyclerView, handler, month);
            }
        });

        adapter.addHeaderView(headView);
    }

    public void getData(int type) {
        String user_id = (String) SPUtils.get(mActivity, "user_id", "");

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getParent(user_id, page, type, month);
        } else {
            toast("网络异常");
        }
    }

    @Override
    public InnateIntelligencePresenter initPresenter() {
        return new InnateIntelligencePresenter(this);
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

    public void setMonth(int month){
        this.month=month;
        if(parentGuideYearText!=null){
            parentGuideYearText.setText(month/12+"岁"+month%12+"个月");
        }
    }
}
