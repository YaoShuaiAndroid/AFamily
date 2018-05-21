package com.family.afamily.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MyVideoView;
import com.family.afamily.activity.mvp.presents.MyVideoPresenter;
import com.family.afamily.adapters.MyVideoAdapter;
import com.family.afamily.adapters.UploadVideoAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.L;
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
 * Created by hp2015-7 on 2017/12/14.
 */

public class MyVideoActivity extends BaseActivity<MyVideoPresenter> implements SuperRecyclerView.LoadingListener, MyVideoView {
    @BindView(R.id.video_yfb_tv)
    TextView videoYfbTv;
    @BindView(R.id.video_yfb_v)
    View videoYfbV;
    @BindView(R.id.video_dsp_tv)
    TextView videoDspTv;
    @BindView(R.id.video_dsp_v)
    View videoDspV;
    @BindView(R.id.video_btg_tv)
    TextView videoBtgTv;
    @BindView(R.id.video_btg_v)
    View videoBtgV;
    @BindView(R.id.my_video_list_rv)
    SuperRecyclerView myVideoListRv;
    @BindView(R.id.video_upload_ing_tv)
    TextView videoUploadIngTv;
    @BindView(R.id.video_upload_ing_v)
    View videoUploadIngV;
    private MyVideoAdapter adapter;
    private List<Map<String, String>> mList = new ArrayList<>();
    private int index = 1;
    private BasePageBean pageBean;
    private String token;
    private String user_id;
    private UploadVideoAdapter uploadVideoAdapter;
    private List<UploadVideoData> videoDataList;
    private UploadDao uploadDao;
    private MyUploadBroadcast uploadBroadcast;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_video);
        token = (String) SPUtils.get(mActivity, "token", "");
        index = getIntent().getIntExtra("index", 1);
        uploadDao = new UploadDao(mActivity);
        user_id = (String) SPUtils.get(mActivity, "user_id", "");
        //从数据库获取上传列表
        videoDataList = uploadDao.getUploadList(user_id);

    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.video_yfb_tv)
    public void clickYFB() {
        if (index != 1) {
            index = 1;
            initView(videoYfbTv, videoYfbV);
            myVideoListRv.completeRefresh();
            mList.clear();
            adapter = null;
            adapter = new MyVideoAdapter(mActivity, mList, presenter);
            myVideoListRv.setAdapter(adapter);
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myVideoListRv, adapter);
            }

            adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (index == 1) {
                        Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                        intent.putExtra("id", mList.get(position - 1).get("id"));
                        intent.putExtra("study", mList.get(position - 1).get("look"));
                        startActivity(intent);
                    }
                }
            });
            isUpdate = false;
        }
    }

    @OnClick(R.id.video_dsp_tv)
    public void clickDSP() {
        if (index != 2) {
            index = 2;
            initView(videoDspTv, videoDspV);
            myVideoListRv.completeRefresh();
            mList.clear();
            adapter = null;
            adapter = new MyVideoAdapter(mActivity, mList, presenter);
            myVideoListRv.setAdapter(adapter);
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myVideoListRv, adapter);
            }
            isUpdate = false;
        }
    }

    @OnClick(R.id.video_btg_tv)
    public void clickBTG() {
        if (index != 3) {
            index = 3;
            initView(videoBtgTv, videoBtgV);
            myVideoListRv.completeRefresh();
            mList.clear();
            adapter = null;
            adapter = new MyVideoAdapter(mActivity, mList, presenter);
            myVideoListRv.setAdapter(adapter);

            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myVideoListRv, adapter);
            }
            isUpdate = false;
        }
    }

    @OnClick(R.id.video_upload_ing_tv)
    public void clickUpload() {
        if (index != 4) {
            index = 4;
            initView(videoUploadIngTv, videoUploadIngV);
            mList.clear();
            adapter.notifyDataSetChanged();
            adapter = null;
            if (videoDataList != null && videoDataList.size() > 0) {
                if (!UploadVideoService.isServiceRunning) {
                    Intent service = new Intent(mActivity, UploadVideoService.class);
                    startService(service);
                }
                isUpdate = true;
                myVideoListRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                }, 1500);
                L.e("tag", "----------有视频--->");
                uploadVideoAdapter = null;
                uploadVideoAdapter = new UploadVideoAdapter(mActivity, videoDataList, presenter);
                myVideoListRv.setAdapter(uploadVideoAdapter);
            }
        }
    }

    public void initView(TextView tv, View v) {
        videoYfbTv.setTextColor(Color.parseColor("#333333"));
        videoDspTv.setTextColor(Color.parseColor("#333333"));
        videoBtgTv.setTextColor(Color.parseColor("#333333"));
        videoUploadIngTv.setTextColor(Color.parseColor("#333333"));
        videoYfbV.setVisibility(View.INVISIBLE);
        videoDspV.setVisibility(View.INVISIBLE);
        videoBtgV.setVisibility(View.INVISIBLE);
        videoUploadIngV.setVisibility(View.INVISIBLE);
        v.setVisibility(View.VISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_yellow));

        if (index == 4) {
            myVideoListRv.setRefreshEnabled(false);// 可以定制是否开启下拉刷新
            myVideoListRv.setLoadMoreEnabled(false);// 可以定制是否开启加载更多
        } else {
            myVideoListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
            myVideoListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        }
    }


    @Override
    public MyVideoPresenter initPresenter() {
        return new MyVideoPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的视频");

        uploadBroadcast = new MyUploadBroadcast();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_data");
        intentFilter.addAction("update_data_time");
        registerReceiver(uploadBroadcast, intentFilter);

        myVideoListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        myVideoListRv.addItemDecoration(divider);
        myVideoListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        myVideoListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        myVideoListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        myVideoListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        myVideoListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        myVideoListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MyVideoAdapter(mActivity, mList, presenter);
        myVideoListRv.setAdapter(adapter);
        if (index != 4) {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, index, 1, 1, mList, myVideoListRv, adapter);
            }
        } else {
            initView(videoUploadIngTv, videoUploadIngV);
            uploadVideoAdapter = new UploadVideoAdapter(mActivity, videoDataList, presenter);
            myVideoListRv.setAdapter(uploadVideoAdapter);
            isUpdate = true;
            myVideoListRv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            }, 1500);
        }
    }

    boolean isUpdate = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (isUpdate) {
                    updata();
                    if (videoDataList.size() > 0) {
                        myVideoListRv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendEmptyMessage(1);
                            }
                        }, 1500);
                    }
                }
            }
        }
    };

    class MyUploadBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("update_data".equals(action)) {
                updata();
            } else if ("update_data_time".equals(action)) {
                updata();
            }
        }
    }


    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }

    @Override
    public void delSuccess() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 1, mList, myVideoListRv, adapter);
        }
    }

    @Override
    public void updateData() {
        isUpdate = false;
        updata();
    }


    public void updata() {
        if (index == 4) {
            List<UploadVideoData> data = uploadDao.getUploadList(user_id);
            videoDataList.clear();
            if (data != null) {
                videoDataList.addAll(data);
            }
            if (uploadVideoAdapter != null) {
                uploadVideoAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, index, 1, 2, mList, myVideoListRv, adapter);
        } else {
            myVideoListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, index, pageBean.getPage() + 1, 3, mList, myVideoListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        myVideoListRv.setNoMore(true);
                    } else {
                        myVideoListRv.completeLoadMore();
                    }
                }
            }
        } else {
            myVideoListRv.completeLoadMore();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uploadBroadcast != null) {
            unregisterReceiver(uploadBroadcast);
        }
    }
}
