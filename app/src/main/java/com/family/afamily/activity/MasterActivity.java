package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MasterView;
import com.family.afamily.activity.mvp.presents.MasterPresenter;
import com.family.afamily.adapters.MasterAdapter;
import com.family.afamily.entity.MasterHomeData;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.utils.GlideCircleTransform;
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
 * Created by hp2015-7 on 2017/12/15.
 */

public class MasterActivity extends BaseActivity<MasterPresenter> implements MasterView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.master_head_iv)
    ImageView masterHeadIv;
    @BindView(R.id.master_nick_tv)
    TextView masterNickTv;
    @BindView(R.id.master_btn_tv)
    TextView masterBtnTv;
    @BindView(R.id.master_fans_count_tv)
    TextView masterFansCountTv;
    @BindView(R.id.frag5_follow_count)
    TextView frag5FollowCount;
    @BindView(R.id.master_issue_count_tv)
    TextView masterIssueCountTv;
    @BindView(R.id.master_count_tv)
    TextView masterCountTv;
    @BindView(R.id.frag5_head_bottom)
    LinearLayout frag5HeadBottom;
    @BindView(R.id.master_list_rv)
    SuperRecyclerView masterListRv;
    @BindView(R.id.master_title)
    LinearLayout masterTitle;
    private MasterAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String user_id;
    private String token;
    private MasterHomeData<Map<String, String>> dataRoot;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_master);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.master_title));
        }
        token = (String) SPUtils.get(mActivity, "token", "");
        user_id = getIntent().getStringExtra("user_id");
    }

    @OnClick(R.id.master_btn_tv)
    public void clickMasterBtn() {
        String flag = (String) masterBtnTv.getTag();
        if (TextUtils.isEmpty(flag)) {
            toast("未获取到用户信息");
        } else {
            masterBtnTv.setEnabled(false);
            if (flag.equals("Y")) {
                presenter.submitFollow(token, user_id, "2");
            } else {
                presenter.submitFollow(token, user_id, "1");
            }
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MasterPresenter initPresenter() {
        return new MasterPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();

        if (TextUtils.isEmpty(user_id)) {
            toast("用户ID不存在");
            finish();
        } else {
            presenter.getData(token, user_id, 1, 1);
        }

        masterListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        masterListRv.addItemDecoration(divider);
        masterListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        masterListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        masterListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        masterListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        masterListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        masterListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MasterAdapter(mActivity, mList);
        masterListRv.setAdapter(adapter);

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
    }

    @Override
    public void successData(MasterHomeData<Map<String, String>> data, int getType, boolean isOk) {
        if (isOk) {
            if (data != null) {
                dataRoot = data;
                Map<String, String> userInfo = data.getUser_info();
                if (userInfo != null) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .error(R.mipmap.tx)
                            .transform(new GlideCircleTransform(mActivity));
                    Glide.with(mActivity).load(userInfo.get("images")).apply(options).into(masterHeadIv);
                    masterNickTv.setText(userInfo.get("nick_name"));
                    masterFansCountTv.setText(userInfo.get("fans_count"));
                    masterIssueCountTv.setText(userInfo.get("video_count"));
                    masterCountTv.setText(userInfo.get("attention_count"));
                    String focus = userInfo.get("focus");
                    masterBtnTv.setTag(focus);
                    if (focus.equals("Y")) {
                        masterBtnTv.setText("取消关注");
                    } else {
                        masterBtnTv.setText("+关注");
                    }
                }
                List<Map<String, String>> mapList = data.getList_data();
                if (mapList != null && mapList.size() > 0) {
                    if (getType == 1) {
                        mList.clear();
                        mList.addAll(mapList);
                    } else if (getType == 2) {
                        mList.clear();
                        mList.addAll(mapList);
                        masterListRv.completeRefresh();
                    } else {
                        mList.addAll(mapList);
                        masterListRv.completeLoadMore();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (getType == 1) {
                        mList.clear();
                    } else if (getType == 2) {
                        mList.clear();
                        masterListRv.completeRefresh();
                    } else {
                        masterListRv.completeLoadMore();
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (getType == 1) {
                    mList.clear();
                } else if (getType == 2) {
                    mList.clear();
                    masterListRv.completeRefresh();
                } else {
                    masterListRv.completeLoadMore();
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            if (getType == 1) {
                mList.clear();
            } else if (getType == 2) {
                mList.clear();
                masterListRv.completeRefresh();
            } else {
                masterListRv.completeLoadMore();
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void followSuccess(int type) {
        if (type == 1) {
            presenter.getData(token, user_id, 1, 1);
        }
        masterBtnTv.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, user_id, 1, 2);
        } else {
            masterListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (dataRoot != null) {
                if (dataRoot.getPage() < dataRoot.getTotle_page()) {
                    presenter.getData(token, user_id, dataRoot.getPage() + 1, 3);
                } else {
                    if (dataRoot.getTotle_page() == dataRoot.getPage()) {
                        masterListRv.setNoMore(true);
                    } else {
                        masterListRv.completeLoadMore();
                    }
                }
            }
        } else {
            masterListRv.completeLoadMore();
        }
    }
}
