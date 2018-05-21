package com.family.afamily.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BabyDetailsView;
import com.family.afamily.activity.mvp.presents.BabyDetailsPresenter;
import com.family.afamily.adapters.BabyDetailsAdapter;
import com.family.afamily.entity.BabyDetailsData;
import com.family.afamily.entity.BabyDetailsList;
import com.family.afamily.entity.ItemBabyData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class BabyDetailsActivity extends BaseActivity<BabyDetailsPresenter> implements BabyDetailsView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.baby_d_issue)
    ImageView babyDIssue;
    @BindView(R.id.baby_d_title)
    LinearLayout babyDTitle;
    @BindView(R.id.baby_d_head)
    ImageView babyDHead;
    @BindView(R.id.baby_d_nick)
    TextView babyDNick;
    @BindView(R.id.baby_d_year)
    TextView babyDYear;
    @BindView(R.id.baby_list_rv)
    RecyclerView babyListRv;
    @BindView(R.id.baby_list_rl)
    RelativeLayout babyListRl;
    @BindView(R.id.baby_d_list_rv)
    SuperRecyclerView babyDListRv;
    @BindView(R.id.baby_d_edit)
    ImageView babyDEdit;

    private List<Map<String, String>> childList = new ArrayList<>();
    private CommonAdapter<Map<String, String>> childAdapter;
    private BabyDetailsAdapter adapter;
    private List<ItemBabyData> mList = new ArrayList<>();
    private String token;
    private BabyDetailsData detailsData;
    private BabyDetailsList detailsList;
    LinearLayoutManager linearLayoutManager;
    String id = "";
    boolean mFull = false;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_baby_details);
        token = (String) SPUtils.get(mActivity, "token", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.baby_d_title));
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public BabyDetailsPresenter initPresenter() {
        return new BabyDetailsPresenter(this);
    }

    @OnClick(R.id.baby_d_issue)
    public void clickIssue() {
        if (TextUtils.isEmpty(id)) {
            toast("未获取到宝宝信息");
        } else {
            Intent intent = new Intent(mActivity, BabyIssueActivity.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, 100);
        }
    }

    @OnClick(R.id.baby_d_head)
    public void clickHead() {
        if (babyListRl.isShown()) {
            babyListRl.setVisibility(View.GONE);
            babyListRl.setAnimation(AnimationUtils.makeOutAnimation(this, true));
        } else {
            babyListRl.setVisibility(View.VISIBLE);
            babyListRl.setAnimation(AnimationUtils.makeInAnimation(this, false));
        }
    }

    @OnClick(R.id.baby_d_edit)
    public void clickEdit() {
        if (detailsData != null) {
            Map<String, String> info = detailsData.getChild_info();
            if (info != null) {
                Intent intent = new Intent(mActivity, AddBabyActivity.class);
                intent.putExtra("isDetailsIn", true);
                intent.putExtra("headImage", info.get("icon"));
                intent.putExtra("birthday", info.get("birthday"));
                intent.putExtra("nickname", info.get("nickname"));
                intent.putExtra("id", info.get("id"));
                startActivityForResult(intent, 100);
            }
        }

    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, "", 1, 1);
        }
        Map<String, String> map = new HashMap<>();
        map.put("isAddChild", "Y");
        childList.add(map);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mActivity);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        babyListRv.setLayoutManager(linearLayoutManager2);
        childAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_child_list_layout, childList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> data, int position) {
                ImageView item_child_img = holder.getView(R.id.item_child_img);
                LinearLayout item_child_root = holder.getView(R.id.item_child_root);
                final String str = data.get("isAddChild");
                if (TextUtils.isEmpty(str)) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .transform(new GlideCircleTransform(mActivity));
                    Glide.with(mActivity).load(data.get("icon")).apply(options).into(item_child_img);
                    holder.setText(R.id.item_child_name, data.get("nickname"));
                } else {
                    item_child_img.setImageResource(R.mipmap.ic_add_baby);
                    holder.setText(R.id.item_child_name, "添加宝贝");
                }

                item_child_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        babyListRl.setVisibility(View.GONE);
                        babyListRl.setAnimation(AnimationUtils.makeOutAnimation(mActivity, true));
                        if (!TextUtils.isEmpty(str) && str.equals("Y")) {
                            Intent intent = new Intent(mActivity, AddBabyActivity.class);
                            intent.putExtra("isDetailsIn", true);
                            startActivityForResult(intent, 100);
                        } else {
                            if (Utils.isConnected(mActivity)) {
                                id = data.get("id");
                                presenter.getData(token, id, 1, 1);
                            }
                        }
                    }
                });

            }
        };
        babyListRv.setAdapter(childAdapter);

        linearLayoutManager = new LinearLayoutManager(this);
        babyDListRv.setLayoutManager(linearLayoutManager);
        //babyDListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        babyDListRv.addItemDecoration(divider);
        babyDListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        babyDListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        babyDListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        babyDListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        babyDListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        babyDListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new BabyDetailsAdapter(mActivity, mList);
        babyDListRv.setAdapter(adapter);

        babyDListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

    }

    @Override
    public void successData(BabyDetailsData detailsData, int getType, boolean isOk) {
        if (isOk) {
            if (detailsData != null) {
                this.detailsData = detailsData;
                Map<String, String> info = detailsData.getChild_info();
                if (info != null) {
                    id = info.get("id");
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .transform(new GlideCircleTransform(mActivity));
                    Glide.with(mActivity).load(info.get("icon")).apply(options).into(babyDHead);
                    babyDNick.setText(info.get("nickname"));
                    babyDYear.setText(info.get("time"));

                }
                List<Map<String, String>> child = detailsData.getOther_child();
                if (child != null && child.size() > 0) {
                    childList.clear();
                    childList.addAll(child);
                    Map<String, String> map = new HashMap<>();
                    map.put("isAddChild", "Y");
                    childList.add(child.size(), map);
                    childAdapter.notifyDataSetChanged();
                }
                detailsList = detailsData.getMessage_list();
                if (detailsList != null) {
                    List<ItemBabyData> datas = detailsList.getList_data();
                    if (datas != null && datas.size() > 0) {
                        if (getType == 2) {
                            mList.clear();
                            babyDListRv.completeRefresh();
                        } else if (getType == 3) {
                            babyDListRv.completeLoadMore();
                        }
                        mList.addAll(datas);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (getType == 2) {
                            babyDListRv.completeRefresh();
                        } else if (getType == 3) {
                            babyDListRv.completeLoadMore();
                        }
                        mList.clear();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (getType == 2) {
                        babyDListRv.completeRefresh();
                    } else if (getType == 3) {
                        babyDListRv.completeLoadMore();
                    }
                    mList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            if (getType == 2) {
                babyDListRv.completeRefresh();
            } else if (getType == 3) {
                babyDListRv.completeLoadMore();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            presenter.getData(token, id, 1, 1);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, id, 1, 2);
        } else {
            babyDListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (detailsList != null) {
                if (detailsList.getPage() < detailsList.getTotle_page()) {
                    presenter.getData(token, id, detailsList.getPage() + 1, 3);
                } else {
                    if (detailsList.getTotle_page() == detailsList.getPage()) {
                        babyDListRv.setNoMore(true);
                    } else {
                        babyDListRv.completeLoadMore();
                    }
                }
            }
        } else {
            babyDListRv.completeLoadMore();
        }
    }

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

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }

}
