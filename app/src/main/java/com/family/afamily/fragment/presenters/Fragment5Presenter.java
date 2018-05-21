package com.family.afamily.fragment.presenters;


import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.UserInfoData;
import com.family.afamily.fragment.interfaces.Fragment5View;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class Fragment5Presenter extends BasePresent<Fragment5View> {

    public Fragment5Presenter(Fragment5View view1) {
        attach(view1);
    }


    /**
     * 获取用户数据
     *
     * @param token
     */
    public void getUserinfoData(String token, int type) {
        if (type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.USER_INFO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<UserInfoData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<UserInfoData> response) {
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
}
