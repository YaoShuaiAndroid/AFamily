package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.presents.ActionDetailsPresenter;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/8.
 */

public class ActionDetailsActivity extends BaseActivity<ActionDetailsPresenter> implements ActionDetailsView {
    @BindView(R.id.act_details_obj)
    TextView actDetailsObj;
    @BindView(R.id.act_details_time)
    TextView actDetailsTime;
    @BindView(R.id.act_details_address)
    TextView actDetailsAddress;
    @BindView(R.id.act_d_submit_btn)
    TextView actDSubmitBtn;
    @BindView(R.id.act_details_decs)
    TextView actDetailsDecs;
    @BindView(R.id.act_d_img_iv)
    ImageView actDImgIv;
    @BindView(R.id.act_d_title_tv)
    TextView actDTitleTv;
    @BindView(R.id.act_details_bm_ll)
    LinearLayout actDetailsBmLl;
    @BindView(R.id.act_details_bm_name)
    TextView actDTitleBmName;
    @BindView(R.id.act_details_bm_phone)
    TextView actDTitleBmPhone;
    @BindView(R.id.act_details_bm_number)
    TextView actDTitleBmNumber;
    @BindView(R.id.act_details_price)
    TextView actDetailsPrice;

    private String id;
    private String token;
    private String is_register;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_action_details);
        token = (String) SPUtils.get(mActivity, "token", "");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public ActionDetailsPresenter initPresenter() {
        return new ActionDetailsPresenter(this);
    }

    @OnClick(R.id.act_d_submit_btn)
    public void clickActSubmit() {
        Map<String, String> data = (Map<String, String>) actDSubmitBtn.getTag();
        if (data == null) {
            toast("未获取到活动信息,请稍后...");
            presenter.getData(token, id);
        } else {
            String str = actDSubmitBtn.getText().toString();
            if (str.equals("删除活动")) {
                if ("0".equals(is_register)) {
                    toast("已有人参加活动，不可删除");
                } else {
                    setResult(100);
                    presenter.showDialog(mActivity, token, data.get("id"), 2);
                }
            } else if (str.equals("立即报名")) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("title", data.get("title"));
                bundle.putString("picture", data.get("picture"));
                //有包名费
                if(actDetailsPrice.isShown()){
                    bundle.putString("price", data.get("price"));
                }
                startActivityForResult(EnrollActionActivity.class, 100, bundle);
            } else {
                presenter.showDialog(mActivity, token, data.get("id"), 1);
            }
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "活动详情");
        if (TextUtils.isEmpty(id)) {
            toast("数据异常");
        } else {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, id);
            }
        }
    }

    @Override
    public void successData(Map<String, String> data) {
        /**
         *  "data": {
         "id": "1",
         "title": "d飞",
         "who": "宝妈",
         "man_number": "50",
         "active_time": "02月12日",
         "active_address": "福田广场的回复开始连接发货是肯定急发是点击分类是点击客服里开始的的手机分类看看是杜绝浪费",
         "picture": "/Uploads/Picture/2017-12-11/5a2e57a422167.jpg",
         "detail": "<p>倒计时法律手段会计法看来是时代峻峰莱克斯顿</p><p>水电费接受到</p><p>发经十东路</p><p>打扫房间开始懂了</p>",
         "status": "2",
         "user_id": "1",
         "username": null
         }
         */

        if (data != null) {
            actDSubmitBtn.setTag(data);
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.error_pic);
            Glide.with(mActivity).load(data.get("picture")).apply(options).into(actDImgIv);
            String str = data.get("price");
            if(!TextUtils.isEmpty(str)){
                Double price = Double.parseDouble(str);
                if(price>0){
                    actDetailsPrice.setVisibility(View.VISIBLE);
                    actDetailsPrice.setText("费用："+str+"元");
                }
            }
            actDTitleTv.setText(data.get("title"));
            actDetailsObj.setText("对象：" + data.get("who"));
            actDetailsTime.setText("时间：" + data.get("active_time"));
            actDetailsAddress.setText("地址：" + data.get("active_address"));
            actDetailsDecs.setText(Html.fromHtml(data.get("detail")));
            is_register = data.get("is_register");
            String oneself = data.get("oneself");
            //自己发布的活动
            if ("1".equals(oneself)) {
                actDetailsBmLl.setVisibility(View.GONE);
                actDSubmitBtn.setText("删除活动");
            } else {
                String is_apply = data.get("is_apply");
                if ("1".equals(is_apply)) {
                    actDetailsBmLl.setVisibility(View.VISIBLE);
                    actDSubmitBtn.setText("取消报名");
                } else {
                    actDetailsBmLl.setVisibility(View.GONE);
                    actDSubmitBtn.setText("立即报名");
                }
            }
            actDTitleBmName.setText("报名人："+data.get("username"));
            actDTitleBmPhone.setText("报名电话："+data.get("phone"));
            actDTitleBmNumber.setText("报名人数："+data.get("number"));

        }
    }

    @Override
    public void submitSuccess(int type) {
        if (type == 1) {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, id);
            }
        } else {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            if (Utils.isConnected(mActivity)) {
                presenter.getData(token, id);
            }
        }
    }
}
