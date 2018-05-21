package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.OrderPayListView;
import com.family.afamily.entity.OrderPayData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class OrderPayListPresenter extends BasePresent<OrderPayListView> {

    public OrderPayListPresenter(OrderPayListView view) {
        attach(view);
    }

    public void submitOrder(final String token, String rec_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("rec_id", rec_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SHOPPING_CAR_SUBMIT, new OkHttpClientManager.ResultCallback<ResponseBean<OrderPayData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交订单失败");
            }

            @Override
            public void onResponse(ResponseBean<OrderPayData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交订单失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    public void submitNext(String token, String address_id, String surplus, String user_year_card_id, String bonus, String user_year_card_money) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("address_id", address_id);
        params.put("surplus", surplus);
        params.put("user_year_card_id", user_year_card_id);
        params.put("bonus", bonus);
        params.put("user_year_card_money", user_year_card_money);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SUBMIT_ORDER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交订单失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交订单失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.submitSuccess(response.getData());
                }
            }
        }, params);
    }
}
