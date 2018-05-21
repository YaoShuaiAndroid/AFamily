package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class AddressListPresenter extends BasePresent<SubmitSuccessView> {

    public AddressListPresenter(SubmitSuccessView view) {
        attach(view);
    }

    public void getData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ADDRESS_LIST_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<Map<String, String>>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<List<Map<String, String>>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.submitSuccess(response.getData());
                }
            }
        }, params);
    }


}
