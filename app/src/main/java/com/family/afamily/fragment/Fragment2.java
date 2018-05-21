package com.family.afamily.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.ActionDetailsActivity;
import com.family.afamily.activity.MasterActivity;
import com.family.afamily.activity.ReleaseActionActivity;
import com.family.afamily.activity.ReleaseVideoActivity;
import com.family.afamily.activity.SearchActivity;
import com.family.afamily.adapters.Frag2FollowAdapter;
import com.family.afamily.adapters.Frag2MasterAdapter;
import com.family.afamily.adapters.Frag2RecommendAdapter;
import com.family.afamily.entity.FollowData;
import com.family.afamily.entity.MasterData;
import com.family.afamily.entity.ZJRecommendData;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.fragment.interfaces.Fragment2View;
import com.family.afamily.fragment.presenters.Fragment2Presenter;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.MultiItemTypeAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Fragment2 extends BaseFragment<Fragment2Presenter> implements Fragment2View {
    @BindView(R.id.frag2_title_video)
    ImageView frag2TitleVideo;
    @BindView(R.id.frag2_title_search)
    LinearLayout frag2TitleSearch;
    @BindView(R.id.frag2_recommend_tv)
    TextView frag2RecommendTv;
    @BindView(R.id.frag2_recommend_v)
    View frag2RecommendV;
    @BindView(R.id.frag2_follow_tv)
    TextView frag2FollowTv;
    @BindView(R.id.frag2_follow_v)
    View frag2FollowV;
    @BindView(R.id.frag2_master_tv)
    TextView frag2MasterTv;
    @BindView(R.id.frag2_master_v)
    View frag2MasterV;
    @BindView(R.id.frag2_action_tv)
    TextView frag2ActionTv;
    @BindView(R.id.frag2_action_v)
    View frag2ActionV;
    @BindView(R.id.frag2_list_rv)
    MyListView frag2ListRv;
    @BindView(R.id.frag2_follow_list_rv)
    RecyclerView frag2FollowListRv;
    @BindView(R.id.frag2_follow_ll)
    LinearLayout frag2FollowLl;
    @BindView(R.id.frag2_master_list_rv)
    RecyclerView frag2MasterListRv;
    @BindView(R.id.frag2_master_ll)
    LinearLayout frag2MasterLl;
    @BindView(R.id.frag2_scroll_sv)
    ScrollView frag2ScrollSv;
    @BindView(R.id.frag2_action_list_rv)
    RecyclerView frag2ActionListRv;
    @BindView(R.id.frag2_action_btn)
    TextView frag2ActionBtn;
    @BindView(R.id.frag2_action_rl)
    RelativeLayout frag2ActionRl;
    @BindView(R.id.frag2_not_data)
    LinearLayout frag2NotData;
    Unbinder unbinder;

    private Activity mActivity;
    private int tab_index = 1;
    private Frag2RecommendAdapter commonAdapter;
    private List<ZJRecommendData> recommendLsit = new ArrayList<>();


    private CommonAdapter<Map<String, String>> followAdapter;
    private List<Map<String, String>> followHead = new ArrayList<>();
    private List<Map<String, String>> followList = new ArrayList<>();

    private Frag2FollowAdapter frag2FollowAdapter;

    private CommonAdapter<Map<String, String>> masterAdapter;
    private Frag2MasterAdapter frag2MasterAdapter;
    private List<Map<String, String>> masterHead = new ArrayList<>();
    private List<Map<String, String>> masterList = new ArrayList<>();


    private CommonAdapter<Map<String, String>> actionAdapter;
    private List<Map<String, String>> actionList = new ArrayList<>();
    private String token;
    private FollowData followData;
    private MasterData masterData;
    private boolean isFrist = true;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_layout, container, false);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Utils.getStatusHeight(getActivity(), view.findViewById(R.id.base_fragment_title));
        }
        unbinder = ButterKnife.bind(this, view);
        token = (String) SPUtils.get(mActivity, "token", "");
        setData();
        return view;
    }

    @OnClick(R.id.frag2_title_search)
    public void clickSearch() {
        Intent intent = new Intent(mActivity, SearchActivity.class);
        intent.putExtra("isZaoj",true);
        startActivity(intent);
    }

    @OnClick(R.id.frag2_recommend_tv)
    public void clickRecommend() {
        if (tab_index != 1) {
            tab_index = 1;
            initView(frag2RecommendTv, frag2RecommendV);
            showRecommendData();
            if (Utils.isConnected(mActivity)) {
                presenter.getRecommendList(token, 1);
            }
        }
    }

    @OnClick(R.id.frag2_follow_tv)
    public void clickFollow() {
        if (tab_index != 2) {
            tab_index = 2;
            initView(frag2FollowTv, frag2FollowV);
            showFollowData();
            if (Utils.isConnected(mActivity)) {
                presenter.getFollowList(token, 1);
            }
        }
    }

    @OnClick(R.id.frag2_master_tv)
    public void clickMaster() {
        if (tab_index != 3) {
            tab_index = 3;
            initView(frag2MasterTv, frag2MasterV);
            showMasterData();
            if (Utils.isConnected(mActivity)) {
                presenter.getMasterList(token, 1);
            }
        }
    }

    @OnClick(R.id.frag2_action_tv)
    public void clickAction() {
        if (tab_index != 4) {
            tab_index = 4;
            initView(frag2ActionTv, frag2ActionV);
            showActionData();
            if (Utils.isConnected(mActivity)) {
                presenter.getActionList(token, 1);
            }
        }
    }

    @OnClick(R.id.frag2_title_video)
    public void clickReleaseVideo() {
        startActivity(new Intent(mActivity, ReleaseVideoActivity.class));
    }

    @OnClick(R.id.frag2_action_btn)
    public void clickReleaseAction() {
        startActivity(new Intent(mActivity, ReleaseActionActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        L.e("tag", "-------------------是否可见-------->" + isHidden());
        if (!isFrist) {
            if (tab_index == 1) {
                if (Utils.isConnected(mActivity)) {
                    presenter.getRecommendList(token, 2);
                }
            } else if (tab_index == 2) {
                if (Utils.isConnected(mActivity)) {
                    presenter.getFollowList(token, 2);
                }
            } else if (tab_index == 3) {
                if (Utils.isConnected(mActivity)) {
                    presenter.getMasterList(token, 2);
                }
            } else {
                if (Utils.isConnected(mActivity)) {
                    presenter.getActionList(token, 2);
                }
            }
        }
        isFrist = false;

    }

    private void initView(TextView tv, View v) {
        frag2RecommendTv.setTextColor(Color.parseColor("#222222"));
        frag2FollowTv.setTextColor(Color.parseColor("#222222"));
        frag2MasterTv.setTextColor(Color.parseColor("#222222"));
        frag2ActionTv.setTextColor(Color.parseColor("#222222"));
        frag2RecommendV.setVisibility(View.INVISIBLE);
        frag2FollowV.setVisibility(View.INVISIBLE);
        frag2MasterV.setVisibility(View.INVISIBLE);
        frag2ActionV.setVisibility(View.INVISIBLE);
        tv.setTextColor(Color.parseColor("#fb9927"));
        v.setVisibility(View.VISIBLE);
        if (tab_index == 4) {
            frag2NotData.setVisibility(View.GONE);
            frag2ScrollSv.setVisibility(View.GONE);
            frag2ActionRl.setVisibility(View.VISIBLE);
        } else {
            frag2NotData.setVisibility(View.GONE);
            frag2ScrollSv.setVisibility(View.VISIBLE);
            frag2ActionRl.setVisibility(View.GONE);
        }
        if (tab_index == 2) {
            frag2FollowLl.setVisibility(View.VISIBLE);
            frag2MasterLl.setVisibility(View.GONE);
        } else if (tab_index == 3) {
            frag2MasterLl.setVisibility(View.VISIBLE);
            frag2FollowLl.setVisibility(View.GONE);
        } else {
            frag2FollowLl.setVisibility(View.GONE);
            frag2MasterLl.setVisibility(View.GONE);
        }
    }

    private void setData() {
        showRecommendData();
        if (Utils.isConnected(mActivity)) {
            presenter.getRecommendList(token, 1);
        }
    }

    /**
     * 显示推荐书籍
     */
    private void showRecommendData() {
        frag2FollowAdapter = null;
        frag2MasterAdapter = null;
        commonAdapter = new Frag2RecommendAdapter(mActivity, recommendLsit);
        frag2ListRv.setAdapter(commonAdapter);
    }

    /**
     * 显示关注数据
     */
    private void showFollowData() {
        commonAdapter = null;
        frag2MasterAdapter = null;
        if (followData != null) {

            followHead.clear();
            if (followData.getAttention_list() != null&&followData.getAttention_list().size()>0) {
                followHead.addAll(followData.getAttention_list());
                frag2NotData.setVisibility(View.GONE);
                frag2ScrollSv.setVisibility(View.VISIBLE);
                frag2ActionRl.setVisibility(View.GONE);
            }else{
                frag2NotData.setVisibility(View.VISIBLE);
                frag2ScrollSv.setVisibility(View.GONE);
                frag2ActionRl.setVisibility(View.GONE);
            }
            followList.clear();
            if (followData.getVideo_info() != null) {

                followList.addAll(followData.getVideo_info());
            }
        }else{
            frag2NotData.setVisibility(View.VISIBLE);
            frag2ScrollSv.setVisibility(View.GONE);
            frag2ActionRl.setVisibility(View.GONE);
        }

        if (followAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            frag2FollowListRv.setLayoutManager(linearLayoutManager);
            followAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag2_follow_head, followHead) {
                @Override
                protected void convert(ViewHolder holder, final Map<String, String> s, int position) {
                    ImageView item_follow_head = holder.getView(R.id.item_follow_head);
                    TextView item_follow_nick = holder.getView(R.id.item_follow_nick);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .error(R.mipmap.tx)
                            .transform(new GlideCircleTransform(mActivity));
                    Glide.with(mActivity).load(s.get("images")).apply(options).into(item_follow_head);
                    item_follow_nick.setText(s.get("nick_name"));

                    item_follow_head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, MasterActivity.class);
                            intent.putExtra("user_id", s.get("attentioned_id"));
                            startActivityForResult(intent, 100);
                        }
                    });
                }
            };
            frag2FollowListRv.setAdapter(followAdapter);
        } else {
            followAdapter.notifyDataSetChanged();
        }

        frag2FollowAdapter = new Frag2FollowAdapter(mActivity, followList);

        frag2ListRv.setAdapter(frag2FollowAdapter);
    }

    /**
     * 显示达人数据
     */
    private void showMasterData() {
        commonAdapter = null;
        frag2FollowAdapter = null;
        if (masterData != null) {
            masterHead.clear();
            masterHead.addAll(masterData.getExpert());
            masterList.clear();
            masterList.addAll(masterData.getVideo_info());
        }
        if (masterAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            frag2MasterListRv.setLayoutManager(linearLayoutManager);
            masterAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag2_master_foot, masterHead) {
                @Override
                protected void convert(ViewHolder holder, final Map<String, String> s, int position) {
                    ImageView item_master_head_iv = holder.getView(R.id.item_master_head_iv);
                    TextView item_master_fans_tv = holder.getView(R.id.item_master_fans_tv);
                    TextView item_master_video_tv = holder.getView(R.id.item_master_video_tv);
                    TextView item_master_follow_btn = holder.getView(R.id.item_master_follow_btn);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .transform(new GlideCircleTransform(mActivity));
                    Glide.with(mActivity).load(s.get("images")).apply(options).into(item_master_head_iv);

                    item_master_fans_tv.setText("粉丝：" + s.get("attention"));
                    item_master_video_tv.setText("视频：" + s.get("video_count"));

                    item_master_follow_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.submitFollow(token, s.get("user_id"), "1");
                        }
                    });

                }
            };
            frag2MasterListRv.setAdapter(masterAdapter);
        } else {
            masterAdapter.notifyDataSetChanged();
        }
        frag2MasterAdapter = new Frag2MasterAdapter(mActivity, masterList);
        frag2ListRv.setAdapter(frag2MasterAdapter);

    }

    /**
     * 显示活动数据
     */
    private void showActionData() {
        frag2ActionListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        frag2ActionListRv.addItemDecoration(divider);
        actionAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag2_action_layout, actionList) {
            @Override
            protected void convert(ViewHolder holder, Map<String, String> s, int position) {
                ImageView item_action_img = holder.getView(R.id.item_action_img);
                TextView item_action_decs = holder.getView(R.id.item_action_decs);

                item_action_decs.setText(Html.fromHtml(s.get("detail")));
                holder.setText(R.id.item_action_title, s.get("title"));
               // holder.setText(, );
                holder.setText(R.id.item_action_time, s.get("active_time"));
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.error_pic);
                Glide.with(mActivity).load(s.get("picture")).apply(options).into(item_action_img);
            }
        };
        frag2ActionListRv.setAdapter(actionAdapter);

        actionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ActionDetailsActivity.class);
                intent.putExtra("id", actionList.get(position).get("id"));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Fragment2Presenter initPresenter() {
        return new Fragment2Presenter(this);
    }

    @Override
    public void successRecommend(List<ZJRecommendData> data) {
        if (data != null && data.size() > 0) {
            recommendLsit.clear();
            recommendLsit.addAll(data);
            if (tab_index == 1) {
                showRecommendData();
            }
        }
    }

    @Override
    public void successFollow(FollowData data) {
        if (data != null) {
            followData = data;
            if (tab_index == 2) {
                showFollowData();
            }
        }else{
            L.e("Tag","------------ddd----------->");
            frag2NotData.setVisibility(View.VISIBLE);
            frag2ScrollSv.setVisibility(View.GONE);
            frag2ActionRl.setVisibility(View.GONE);
        }
    }

    @Override
    public void successMaster(MasterData data) {
        if (data != null) {
            masterData = data;
            if (tab_index == 3) {
                showMasterData();
            }
        }
    }

    @Override
    public void successAction(List<Map<String, String>> data) {
        if (data != null && data.size() > 0) {
            actionList.clear();
            actionList.addAll(data);
            if (tab_index == 4) {
                showActionData();
            }
        }
    }

    @Override
    public void submitFollow() {
        if (tab_index == 3) {
            if (Utils.isConnected(mActivity)) {
                presenter.getMasterList(token, 1);
            }
        }
    }


}
