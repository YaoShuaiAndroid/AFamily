package com.family.afamily.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.MsgView;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.activity.mvp.presents.MsgPresenter;
import com.family.afamily.adapters.MsgAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.MsgData;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
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
import butterknife.ButterKnife;

/**
 * Created by hp2015-7 on 2017/12/12.
 */

public class MsgActivity extends BaseActivity<MsgPresenter> implements MsgView, SuperRecyclerView.LoadingListener {
    @BindView(R.id.msg_list_rv)
    SuperRecyclerView msgListRv;
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.msg_del_tv)
    TextView msgDelTv;
    @BindView(R.id.msg_clean_tv)
    TextView msgCleanTv;
    @BindView(R.id.msg_bottom_rl)
    LinearLayout msgBottomRl;
    private MsgAdapter adapter;
    private List<MsgData> mList = new ArrayList<>();
    private BasePageBean pageBean;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_msg);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public MsgPresenter initPresenter() {
        return new MsgPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "我的消息");
        baseTitleRightTv.setTag(1);
        baseTitleRightTv.setText("编辑");
        msgListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        msgListRv.addItemDecoration(divider);
        msgListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        msgListRv.setLoadMoreEnabled(true);// 可以定制是否开启加载更多
        msgListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        msgListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        msgListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        msgListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        adapter = new MsgAdapter(mActivity, mList);
        msgListRv.setAdapter(adapter);

        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, msgListRv, adapter);
        }
        //清空数据
        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.isEmpty()){
                    toast("您没有消息数据");
                    return;
                }
                int tag = (int) baseTitleRightTv.getTag();
                if(tag == 1){
                    baseTitleRightTv.setText("完成");
                    baseTitleRightTv.setTag(2);
                    msgBottomRl.setVisibility(View.VISIBLE);
                    adapter.setShow(true);
                }else{
                    baseTitleRightTv.setTag(1);
                    baseTitleRightTv.setText("编辑");
                    msgBottomRl.setVisibility(View.GONE);
                    adapter.setShow(false);
                }
                adapter.notifyDataSetChanged();
            }
        });

        msgDelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String ids = "";
                List<String> ids = new ArrayList<>();
                for (int i = 0; i <mList.size() ; i++) {
                    if(mList.get(i).isCheck()){
                      //  ids += mList.get(i).getId()+",";
                        ids.add(mList.get(i).getId());
                    }
                }
                if(ids.isEmpty()){
                    toast("请选择要删除消息条目");
                }else{
                    L.e("tag",ids.toString()+"-------------->");
                    presenter.showDeleteDialog(mActivity,token,ids.toString(),"2");
                  //  presenter.submitDel(token,"2",ids.toString());
                }
//                if(TextUtils.isEmpty(ids)){
//                    toast("请选择要删除消息条目");
//                }else{
//                    ids = ids.substring(0,ids.length()-1);
//                    L.e("tag",ids+"-------------->");
//                    presenter.submitDel(token,"2",ids);
//                }
            }
        });


        msgCleanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showDeleteDialog(mActivity,token,"","1");
            }
        });
    }


    @Override
    public void successData(BasePageBean pageBean) {
        if (pageBean != null) {
            this.pageBean = pageBean;
        }
    }

    @Override
    public void successData() {
        baseTitleRightTv.setTag(1);
        baseTitleRightTv.setText("编辑");
        msgBottomRl.setVisibility(View.GONE);
        adapter.setShow(false);
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 1, mList, msgListRv, adapter);
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token, 1, 2, mList, msgListRv, adapter);
        } else {
            msgListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (pageBean != null) {
                if (pageBean.getPage() < pageBean.getTotle_page()) {
                    presenter.getData(token, pageBean.getPage() + 1, 3, mList, msgListRv, adapter);
                } else {
                    if (pageBean.getTotle_page() == pageBean.getPage()) {
                        msgListRv.setNoMore(true);
                    } else {
                        msgListRv.completeLoadMore();
                    }
                }
            }
        } else {
            msgListRv.completeLoadMore();
        }
    }

}
