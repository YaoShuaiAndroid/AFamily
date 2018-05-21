package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.MasterView;
import com.family.afamily.entity.MasterRoot;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class MasterPresenter extends BasePresent<MasterView> {

    public MasterPresenter(MasterView view) {
        attach(view);
    }

    public void getData(String token, String user_id, int p, final int getType) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("user_id", user_id);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MASTER_HOME_URL, new OkHttpClientManager.ResultCallback<MasterRoot<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.successData(null, getType, false);
            }

            @Override
            public void onResponse(MasterRoot<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null, getType, false);
                } else {
                    view.successData(response.getData(), getType, true);
                }
            }
        }, params);
    }

    /**
     * 关注、取消
     *
     * @param token
     * @param user_id
     * @param attention 1：添加未关注，2：取消已关注
     */
    public void submitFollow(String token, String user_id, String attention) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("user_id", user_id);
        params.put("attention", attention);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ADD_FOLLOW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
                view.followSuccess(0);
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                    view.followSuccess(0);
                } else {
                    view.toast(response.getMsg());
                    // view.submitCollectResult(1);
                    view.followSuccess(1);
                }
            }
        }, params);
    }

}
