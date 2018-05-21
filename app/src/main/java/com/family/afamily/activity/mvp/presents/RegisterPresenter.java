package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.RegisterView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class RegisterPresenter extends BasePresent<RegisterView> {
    public RegisterPresenter(RegisterView registerView) {
        attach(registerView);
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @param msgType
     */
    public void getCode(String mobile, int msgType) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", msgType + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.GET_CODE, new OkHttpClientManager.ResultCallback<ResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取验证码失败");
                view.getCodeFail();
            }

            @Override
            public void onResponse(ResponseBean response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "发送验证码出错" : response.getMsg();
                    view.toast(msg);
                    view.getCodeFail();
                } else {
                    view.toast(response.getMsg());
                }
            }
        }, params);
    }

    /**
     * 提交校验
     *
     * @param mobile
     * @param code
     */
    public void submitVerify(String mobile, String code) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("mobile_phone", mobile);
        params.put("verify", code);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.REGISTER_ONE_URL, new OkHttpClientManager.ResultCallback<ResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("校验失败");
            }

            @Override
            public void onResponse(ResponseBean response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "校验失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.verifySuccess();
                }
            }
        }, params);
    }

}
