package com.family.afamily.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.YuYueView;
import com.family.afamily.activity.mvp.presents.YuYuePresenter;
import com.family.afamily.entity.YuYueDetailsData;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/1.
 */

public class YuYueActivity extends BaseActivity<YuYuePresenter> implements YuYueView {
    @BindView(R.id.yy_head_rl)
    RelativeLayout yyHeadRl;
    @BindView(R.id.yy_order_sn_tv)
    TextView yyOrderSnTv;
    @BindView(R.id.yyue_img)
    ImageView yyueImg;
    @BindView(R.id.yy_title_tv)
    TextView yyTitleTv;
    @BindView(R.id.yy_decs_tv)
    TextView yyDecsTv;
    @BindView(R.id.yy_arrow_iv)
    ImageView yyArrowIv;
    @BindView(R.id.yy_t_img)
    ImageView yyTImg;
    @BindView(R.id.yy_time_tv)
    TextView yyTimeTv;
    @BindView(R.id.yy_select_time_rl)
    RelativeLayout yySelectTimeRl;
    @BindView(R.id.yy_yysj_tv)
    TextView yyYysjTv;
    @BindView(R.id.yy_time_ll)
    LinearLayout yyTimeLl;
    @BindView(R.id.yy_name_tv)
    TextView yyNameTv;
    @BindView(R.id.yy_type_tv)
    TextView yyTypeTv;
    @BindView(R.id.yy_address_tv)
    TextView yyAddressTv;
    @BindView(R.id.yy_submit_tv)
    TextView yySubmitTv;

    private YuYueDetailsData rootData;
    private String token;
    private List<Map<String, String>> list = new ArrayList<>();
    private List<String> housList = new ArrayList<>();

    private String id;
    private String integral;
    private String store_id;
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_yu_yue);
        token = (String) SPUtils.get(mActivity, "token", "");
        if (Utils.isConnected(mActivity)) {
            if (TextUtils.isEmpty(myApplication.getCity())) {
                toast("未定位到当前城市，请手动选择城市");
                finish();
            } else {
                L.e("tag", "--------------tttt------------------->");
                presenter.getData(token, myApplication.getCity());
            }
        }

    }

    @OnClick(R.id.yy_head_rl)
    public void clickHead() {
        if (list != null && list.size() > 1) {
            presenter.showDiaog(mActivity, myApplication.getCity(), list);
        } else {
            toast("没有更多场馆");
        }
    }

    @OnClick(R.id.yy_select_time_rl)
    public void clickDate() {
        presenter.showDateDialog(mActivity, Utils.getMonthTime(), housList);
    }

    @OnClick(R.id.yy_submit_tv)
    public void clickSubmit() {
        if (rootData != null) {
            String time = yyTimeTv.getText().toString();
            if (TextUtils.isEmpty(time)) {
                toast("请选择预约时间");
            } else {
                int t = Integer.parseInt(integral);
                if (t <= rootData.getUser_integral()) {
                    presenter.showYuYueDialog(mActivity, token, integral, rootData.getUser_integral() + "",
                            rootData.getBack_integral() + "", time, id,store_id);

                } else {
                    toast("积分不足，无法预约");
                }
            }
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public YuYuePresenter initPresenter() {
        return new YuYuePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "预约");
    }

    @Override
    public void successData(YuYueDetailsData data) {
        if (data != null) {
            rootData = data;
            if (rootData.getPool_list() != null && rootData.getPool_list().size() > 0) {
                list.clear();
                list.addAll(rootData.getPool_list());
                setData(list.get(0));
            }
        }
    }

    @Override
    public void selectData(Map<String, String> data) {
        setData(data);
    }

    @Override
    public void selectTime(String time) {
        yyTimeTv.setText(time);
    }

    @Override
    public void submitSuccess() {
        setResult(101);
        finish();
    }

    //设置数据
    private void setData(Map<String, String> data) {
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(mActivity).load(data.get("picture")).apply(options).into(yyueImg);
        yyTitleTv.setText(data.get("pool_name"));
        yyDecsTv.setText(data.get("intro"));
        yyNameTv.setText("游泳池：" + data.get("pool_name"));
        yyTypeTv.setText("预约类型：" + data.get("pool_type"));
        yyAddressTv.setText("场馆地址：" + data.get("address"));
        id = data.get("id");
        integral = data.get("integral");
        store_id = data.get("store_id");

        int open = Integer.parseInt(data.get("open_time"));
        int close = Integer.parseInt(data.get("close_time"));
        int start = Integer.parseInt(data.get("start_rest"));
        int end = Integer.parseInt(data.get("end_rest"));
        housList.clear();
        for (int i = open; i < close; i++) {
            if (i >= start && i < end) {
            } else {
                if (i < 10) {
                    housList.add("0" + i + ":00");
                } else {
                    housList.add(i + ":00");
                }
            }
        }
    }
}
