package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.EnrollActionView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/2.
 */

public class EnrollActionPresenter extends BasePresent<EnrollActionView> {
    public EnrollActionPresenter(EnrollActionView v) {
        attach(v);
    }

    public void submitEnroll(String token, String id, String phone, String number, String name,String payType) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("mobile_phone", phone);
        params.put("number", number);
        params.put("nick_name", name);
        params.put("type", payType);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ENROLL_ACTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String,String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("连接异常,报名失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String,String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "报名失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.submitSuccess(response.getData());
                }
            }
        }, params);
    }

}
