package com.family.afamily.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ReplyView;
import com.family.afamily.activity.mvp.presents.ReplyPresenter;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class ReplyActivity extends BaseActivity<ReplyPresenter> implements ReplyView {
    @BindView(R.id.reply_list_rv)
    RecyclerView replyListRv;
    private String token;
    private List<Map<String, String>> list = new ArrayList<>();
    private CommonAdapter<Map<String, String>> adapter;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reply_list);
        token = (String) SPUtils.get(mActivity, "token", "");

    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public ReplyPresenter initPresenter() {
        return new ReplyPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "回复列表");
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token);
        }

        replyListRv.setLayoutManager(new LinearLayoutManager(mActivity));

        adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_reply_layout, list) {
            @Override
            protected void convert(ViewHolder holder, Map<String, String> data, int position) {
                holder.setText(R.id.item_reply_title_tv, data.get("question"));
                String str = TextUtils.isEmpty(data.get("answer")) ? "回复内容：未有回复" : "回复内容：" + data.get("answer");

                holder.setText(R.id.item_reply_content_tv, str);
            }
        };
        replyListRv.setAdapter(adapter);
    }

    @Override
    public void successData(List<Map<String, String>> data) {
        if (data != null && data.size() > 0) {
            list.clear();
            list.addAll(data);
            adapter.notifyDataSetChanged();
        } else {
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
