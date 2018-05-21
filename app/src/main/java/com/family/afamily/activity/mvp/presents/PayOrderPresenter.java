package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.PayOrderView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/16.
 */

public class PayOrderPresenter extends BasePresent<PayOrderView> {

    public PayOrderPresenter(PayOrderView view) {
        attach(view);
    }

    public void submitData(String token, String order_sn, String type) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_sn", order_sn);
        params.put("type", type);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SUBMIT_PAY_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交支付订单失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }
}
