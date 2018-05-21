package com.family.afamily.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.EnrollActionView;
import com.family.afamily.activity.mvp.presents.EnrollActionPresenter;
import com.family.afamily.entity.PayResult;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.wxapi.WXPayResultCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/8.
 */

public class EnrollActionActivity extends BaseActivity<EnrollActionPresenter> implements EnrollActionView {
    @BindView(R.id.enroll_head_img)
    ImageView enrollHeadImg;
    @BindView(R.id.enroll_head_title)
    TextView enrollHeadTitle;
    @BindView(R.id.enroll_name_et)
    EditText enrollNameEt;
    @BindView(R.id.enroll_phone_et)
    EditText enrollPhoneEt;
    @BindView(R.id.enroll_spinner_et)
    Spinner enrollSpinnerEt;
    @BindView(R.id.enroll_submit_btn)
    TextView enrollSubmitBtn;
    @BindView(R.id.enroll_price)
    TextView enrollPrice;
    @BindView(R.id.enroll_price_rl)
    RelativeLayout enrollPriceRl;
    @BindView(R.id.pay_balance_check)
    CheckBox payBalanceCheck;
    @BindView(R.id.pay_balance_item)
    RelativeLayout payBalanceItem;
    @BindView(R.id.pay_wx_check)
    CheckBox payWxCheck;
    @BindView(R.id.pay_wx_item)
    RelativeLayout payWxItem;
    @BindView(R.id.pay_alipay_check)
    CheckBox payAlipayCheck;
    @BindView(R.id.pay_alipay_item)
    RelativeLayout payAlipayItem;
    @BindView(R.id.pay_ll)
    LinearLayout payLl;
    private String id, title, picture, price;
    private String token;
    private Double price1 = 0.0;
    private IWXAPI api;//微信
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_enroll_action);
        token = (String) SPUtils.get(mActivity, "token", "");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            title = bundle.getString("title");
            picture = bundle.getString("picture");
            price = bundle.getString("price");
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public EnrollActionPresenter initPresenter() {
        return new EnrollActionPresenter(this);
    }

    @OnClick(R.id.pay_balance_item)
    public void clickBalence() {
        payBalanceCheck.setChecked(true);
        payWxCheck.setChecked(false);
        payAlipayCheck.setChecked(false);
    }

    @OnClick(R.id.pay_wx_item)
    public void clickWXPay() {
        payBalanceCheck.setChecked(false);
        payWxCheck.setChecked(true);
        payAlipayCheck.setChecked(false);
    }

    @OnClick(R.id.pay_alipay_item)
    public void clickAliPay() {
        payBalanceCheck.setChecked(false);
        payWxCheck.setChecked(false);
        payAlipayCheck.setChecked(true);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "报名");
        if (TextUtils.isEmpty(id)) {
            toast("数据异常");
        } else {
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.error_pic);
            Glide.with(mActivity).load(picture).apply(options).into(enrollHeadImg);
            enrollHeadTitle.setText(title);
        }

        if (!TextUtils.isEmpty(price)) {
            price1 = Double.parseDouble(price);
            if (price1 > 0) {
                enrollPriceRl.setVisibility(View.VISIBLE);
                payLl.setVisibility(View.VISIBLE);
                enrollPrice.setText(price+"元");

                myApplication.setWxPayResultCallback(new WXPayResultCallback() {
                    @Override
                    public void payResultCallback(int resultCode) {
                        String mes;
                        if (resultCode == -1) {
                            mes = "支付失败,错误码是" + resultCode;
                        } else if (resultCode == -2) {
                            mes = "取消支付";
                        } else if (resultCode == 0) {
                            mes = "支付成功";
                            toast(mes);
                            setResult(100);
                            finish();
                            return;
                        } else {
                            mes = "支付失败,错误码是" + resultCode;
                        }
                        toast(mes);
                        enrollSubmitBtn.setEnabled(true);
                    }
                });

            }
        }

        enrollSpinnerEt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(price1>0){
                    enrollPrice.setText((price1*(position+1))+"元");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enrollSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enrollNameEt.getText().toString();
                String phone = enrollPhoneEt.getText().toString();
                String number = enrollSpinnerEt.getSelectedItem().toString();
                if(price1>0){
                    enrollSubmitBtn.setEnabled(false);
                    String payType = payBalanceCheck.isChecked() ? "balance" : payWxCheck.isChecked() ? "wxpay" : "alipay";
                    presenter.submitEnroll(token, id, phone, number, name,payType);
                }else{
                    L.e("tag", number + "---------------->");
                    presenter.submitEnroll(token, id, phone, number, name,"");
                }
            }
        });
    }

    @Override
    public void submitSuccess(final Map<String,String> data) {
        if (data != null) {
            String payType = data.get("paytype");
            if (TextUtils.isEmpty(payType)) {
                toast("");
                setResult(100);
                finish();
            } else {
                if (data.get("paytype").equalsIgnoreCase("wxpay")) {
                    //微信支付
                    api = WXAPIFactory.createWXAPI(mActivity, data.get("appid"));
                    api.registerApp(data.get("appid"));
                    try {
                        PayReq req = new PayReq();
                        req.appId = data.get("appid");
                        req.partnerId = data.get("partnerid");
                        req.prepayId = data.get("prepayid");
                        req.nonceStr = data.get("noncestr");
                        req.timeStamp = data.get("timestamp");
                        req.packageValue = data.get("packages");
                        req.sign = data.get("sign");
                        toast("正在调起微信支付，请稍后...");
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (data.get("paytype").equalsIgnoreCase("alipay")) {
                    //支付宝支付逻辑
                    if (TextUtils.isEmpty(data.get("orderInfo"))) {
                        enrollSubmitBtn.setEnabled(true);
                        toast("未获取到订单数据");
                        return;
                    }
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(mActivity);
                            Map<String, String> result = alipay.payV2(data.get("orderInfo"), true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else if (data.get("paytype").equals("balance")) {
                    toast("报名成功");
                    setResult(100);
                    finish();
                }
            }
        }else{
            toast("报名成功");
            setResult(100);
            finish();
        }

    }
    private int jjl = 0xff;
    //支付宝sdk返回结果集标识
    private static final int SDK_PAY_FLAG = 1;
    //支付宝支付结果
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    toast("支付成功");
                    setResult(100);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    toast("支付失败");
                    enrollSubmitBtn.setEnabled(true);
                }
            }
        }
    };

}
