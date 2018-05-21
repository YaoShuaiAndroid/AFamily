package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.ShoppingCarView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ShoppingCarData;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class ShoppingCarPresenter extends BasePresent<ShoppingCarView> {

    public ShoppingCarPresenter(ShoppingCarView view) {
        attach(view);
    }

    public void getDataList(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SHOPPING_CAR_LIST_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ShoppingCarData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<ShoppingCarData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    public void updataCarNumber(final String token, String rec_id, String number) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("rec_id", rec_id);
        params.put("number", number);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.UPDATE_CAR_NUMBER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ShoppingCarData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("更改失败");
            }

            @Override
            public void onResponse(ResponseBean<ShoppingCarData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "更改购物车失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    getDataList(token);
                }
            }
        }, params);
    }

    public void delCarNumber(final String token, String rec_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("rec_id", rec_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.DEL_SHOPPING_CAR_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ShoppingCarData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("删除失败");
            }

            @Override
            public void onResponse(ResponseBean<ShoppingCarData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "删除商品失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    getDataList(token);
                }
            }
        }, params);
    }
}
