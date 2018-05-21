package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.MyInfoView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class MyInfoPresenter extends BasePresent<MyInfoView> {

    public MyInfoPresenter(MyInfoView view) {
        attach(view);
    }

    /**
     * 获取用户信息
     *
     * @param token
     */
    public void getUserinfoData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BASE_USER_INFO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取用户信息失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    // view.toast(response.getMsg());
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    /**
     * 修改用户信息
     *
     * @param token
     * @param type      1：修改头像，2：修改昵称，3：修改密码
     * @param nick_name
     * @param password
     * @param pass
     * @param file
     */
    public void submitUserData(String token, String type, String nick_name, String password, String pass, File file) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", type);
        if (type.equals("1")) {
            params = Utils.getParams(params);
            try {
                OkHttpClientManager.postAsyn(UrlUtils.USER_CHANGE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        view.hideProgress();
                        view.toast("提交失败");
                    }

                    @Override
                    public void onResponse(ResponseBean<String> response) {
                        view.hideProgress();
                        if (response == null || response.getRet_code() != 1) {
                            String msg = response == null ? "修改头像失败" : response.getMsg();
                            view.toast(msg);
                        } else {
                            view.toast(response.getMsg());
                            view.updateResult();
                        }

                    }
                }, file, "avatar", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (type.equals("2")) {
                params.put("nick_name", nick_name);
            } else if (type.equals("3")) {
                params.put("password", password);
                params.put("pass", pass);
            }
            params = Utils.getParams(params);
            OkHttpClientManager.postAsyn(UrlUtils.USER_CHANGE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("提交失败");
                }

                @Override
                public void onResponse(ResponseBean<String> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "修改失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        view.updateResult();
                    }
                }
            }, params);
        }
    }
}
