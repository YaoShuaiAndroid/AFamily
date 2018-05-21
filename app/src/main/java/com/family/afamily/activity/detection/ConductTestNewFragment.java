package com.family.afamily.activity.detection;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.family.afamily.R;
import com.family.afamily.activity.PhysiqueActivity;
import com.family.afamily.activity.mvp.interfaces.ConductTestView;
import com.family.afamily.activity.mvp.presents.ConductTestPresenter;
import com.family.afamily.adapters.BabyDetailsAdapter;
import com.family.afamily.adapters.CommonChaAdapter;
import com.family.afamily.adapters.ConductAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ConductClassListModel;
import com.family.afamily.entity.ConductClassModel;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.fragment.base.LazyFragment;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Maibenben on 2018/4/26.
 */

public class ConductTestNewFragment extends LazyFragment<ConductTestPresenter>
        implements ConductTestView,SuperRecyclerView.LoadingListener{
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @BindView(R.id.conduct_list_rv)
    SuperRecyclerView superRecyclerView;
    @BindView(R.id.conduct_layout)
    LinearLayout conductLayout;
    @BindView(R.id.conduct_text_person_recy1)
    RecyclerView conductSuspensionRecy;
    @BindView(R.id.conduct_text_name)
    TextView conductTextName;

    private Activity mActivity;
    private ConductAdapter adapter;
    Unbinder unbinder;

    private List<InnateIntelligenceModel> mList = new ArrayList<>();

    private int month = 1;
    //当前用轴滑动距离
    private int currentScrollY=0;

    //可见范围内的第一项的位置
    private int firstVisibleItemPositionActivity = 0;
    //可见范围内的最后一项的位置
    private int lastVisibleItemPositionActivity = 0;

    public static ConductTestNewFragment newInstance(String category, int multiType) {
        ConductTestNewFragment maintenanceFragment = new ConductTestNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_conduct_test_new_fragment, container, false);
        mActivity = getActivity();

        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    public void init() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        superRecyclerView.setLayoutManager(linearLayoutManager);
        superRecyclerView.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        superRecyclerView.setLoadMoreEnabled(false);// 可以定制是否开启加载更多
        superRecyclerView.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        superRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        superRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        superRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new ConductAdapter(mActivity, mList);
        superRecyclerView.setAdapter(adapter);

        superRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(getScollYDistance(recyclerView)< AppUtil.dip2px(mActivity,415)){
                    conductLayout.setVisibility(View.GONE);
                }else{
                    conductLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        superRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //返回当前recyclerview的可见的item数目，也就是datas.length
            //dx是水平滚动的距离，dy是垂直滚动距离，向上滚动的时候为正，向下滚动的时候为负
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition< lastVisibleItemPositionActivity) {
                    //向下滑
                    if (data!=null&&data.getType_next() != null && personCommonAdapter != null && lastVisibleItemPosition-2   >= 0) {
                        presenter.setNotify(data.getType_next(), personCommonAdapter, lastVisibleItemPosition-2  );
                    }
                }else if (lastVisibleItemPosition> lastVisibleItemPositionActivity) {
                    //向上滑
                    if (data!=null&&data.getType_next() != null && personCommonAdapter != null && lastVisibleItemPosition-2  >= 0) {
                        presenter.setNotify(data.getType_next(), personCommonAdapter, lastVisibleItemPosition-2  );
                    }
                }

               /* if (firstVisibleItemPosition != firstVisibleItemPositionActivity
                        &&currentScrollY>getScollYDistance(recyclerView)) {
                    //向上滑
                    Log.i("tag","向上滑");
                    if (data!=null&&data.getType_next() != null && personCommonAdapter != null && firstVisibleItemPosition - 2 >= 0) {
                        presenter.setNotify(data.getType_next(), personCommonAdapter, firstVisibleItemPosition - 2);
                    }
                }*/

                //currentScrollY=getScollYDistance(recyclerView);
                firstVisibleItemPositionActivity = firstVisibleItemPosition;
                lastVisibleItemPositionActivity = lastVisibleItemPosition;
            }
        });

        superRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(BabyDetailsAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!mFull) {
                            GSYVideoPlayer.releaseAllVideos();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        addHeadView();
    }


    RecyclerView baseRecyclerView;
    RecyclerView personRecyclerView;
    private TextView conductTextName1;

    private void addHeadView() {
        View headView = getLayoutInflater()
                .inflate(R.layout.header_conduct_item, (ViewGroup) superRecyclerView.getParent(), false);

        baseRecyclerView = headView.findViewById(R.id.conduct_text_base_recy);
        personRecyclerView = headView.findViewById(R.id.conduct_text_person_recy);
        conductTextName1=headView.findViewById(R.id.conduct_text_name);

        adapter.addHeaderView(headView);
    }

    @Override
    protected void loadData() {
        getData();
    }

    /**
     * 获取行为类别
     */
    public void getData() {
        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getData("" + month);
        } else {
            toast("网络异常");
        }
    }

    @Override
    public ConductTestPresenter initPresenter() {
        return new ConductTestPresenter(this);
    }

    private ConductClassListModel data;

    @Override
    public void successClassData(ConductClassListModel data) {
        this.data = data;

        if (data.getType_next() != null && data.getType_next().size() > 0) {
            data.getType_next().get(0).setSelect(true);
        }
        this.mList.clear();
        superRecyclerView.completeRefresh();

        this.mList.addAll(data.getData_list());
        adapter.notifyDataSetChanged();

        setClassRecy(data);
    }

    @Override
    public void successData(BasePageBean<InnateIntelligenceModel> data) {

    }

    private CommonChaAdapter baseCommonAdapter, personCommonAdapter;

    public void setClassRecy(final ConductClassListModel data) {
        baseRecyclerView.setHasFixedSize(true);
        baseRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        baseRecyclerView.setLayoutManager(linearLayoutManager);

        baseCommonAdapter = new CommonChaAdapter<ConductClassModel>(R.layout.list_base_conduct_item, data.getType_top()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ConductClassModel item) {
                TextView conduct_item_class_text = baseViewHolder.getView(R.id.conduct_item_class_text);

                if (baseViewHolder.getPosition() % 4 == 0) {
                    conduct_item_class_text.setBackgroundResource(R.mipmap.test_01);
                } else if (baseViewHolder.getPosition() % 4 == 1) {
                    conduct_item_class_text.setBackgroundResource(R.mipmap.test_02);
                } else if (baseViewHolder.getPosition() % 4 == 2) {
                    conduct_item_class_text.setBackgroundResource(R.mipmap.test_);
                } else if (baseViewHolder.getPosition() % 4 == 3) {
                    conduct_item_class_text.setBackgroundResource(R.mipmap.test_04);
                }

                conduct_item_class_text.setText(item.getType());
            }
        };

        baseRecyclerView.setAdapter(baseCommonAdapter);
        baseCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhysiqueActivity.start(mActivity, data.getType_top().get(position).getId(), data.getType_top().get(position).getType());
            }
        });

        personRecyclerView.setHasFixedSize(true);
        personRecyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 5);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mActivity, 5);
        personRecyclerView.setLayoutManager(gridLayoutManager);
        conductSuspensionRecy.setLayoutManager(gridLayoutManager1);

        personCommonAdapter = new CommonChaAdapter<ConductClassModel>(R.layout.list_person_conduct_item, data.getType_next()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ConductClassModel item) {
                TextView conduct_item_my_class = baseViewHolder.getView(R.id.conduct_item_my_class);

                if (item.isSelect()) {
                    conduct_item_my_class.setBackgroundResource(R.drawable.btn_5_yellowbg);
                    conduct_item_my_class.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    conduct_item_my_class.setBackgroundResource(R.drawable.gray_line_4dp);
                    conduct_item_my_class.setTextColor(Color.parseColor("#999999"));
                }

                conduct_item_my_class.setText(item.getType());
            }
        };

        personRecyclerView.setAdapter(personCommonAdapter);
        conductSuspensionRecy.setAdapter(personCommonAdapter);

        personCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //getBehaviorInfo(data.getType_next().get(position).getId());
                superRecyclerView.scrollToPosition(position + 2);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) superRecyclerView.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(position + 2, AppUtil.dip2px(mActivity,140));

                presenter.setNotify(data.getType_next(), personCommonAdapter, position);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            getData();
        } else {
            superRecyclerView.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {

    }

    public void setMonth(int month,String name){
        this.month=month;
        if(!TextUtils.isEmpty(name)){
            conductTextName.setText("#"+name+" 行为检测指南");
            if(conductTextName1!=null){
                conductTextName1.setText("#"+name+" 行为检测指南");
            }
        }

        getData();
    }

    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    boolean mFull = false;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }

    }

  /*  @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(mActivity)) {
            return;
        }
        super.onBackPressed();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }
}
