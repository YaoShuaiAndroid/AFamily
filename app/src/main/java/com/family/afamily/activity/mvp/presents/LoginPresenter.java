package com.family.afamily.activity.mvp.presents;

import android.text.TextUtils;

import com.family.afamily.activity.mvp.interfaces.LoginView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class LoginPresenter extends BasePresent<LoginView> {
    public LoginPresenter(LoginView loginView) {
        attach(loginView);
    }

    /**
     * 提交登录
     *
     * @param mobile
     * @param pw
     */
    public void loginSubmit(String mobile, String pw) {
        if (TextUtils.isEmpty(mobile)) {
            view.toast("请输入手机号");
        } else if (!Utils.isMobile(mobile)) {
            view.toast("请输入正确手机号");
        } else if (TextUtils.isEmpty(pw)) {
            view.toast("请输入登录密码");
        } else {
            view.showProgress(1);
            Map<String, String> params = new HashMap<>();
            params.put("mobile_phone", mobile);
            params.put("password", pw);
            params = Utils.getParams(params);
            OkHttpClientManager.postAsyn(UrlUtils.LOGIN_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("登录失败");
                }

                @Override
                public void onResponse(ResponseBean<Map<String, String>> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "登录失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        Map<String, String> data = response.getData();
                        view.loginSuccess();
                        SPUtils.put(context, "token", data.get("token"));
                        SPUtils.put(context, "user_id", data.get("user_id"));
                    }
                }
            }, params);
        }
    }
}
