package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.IntegralView;
import com.family.afamily.entity.IntegralData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class IntegralPresenter extends BasePresent<IntegralView> {
    public IntegralPresenter(IntegralView view) {
        attach(view);
    }

    public void getData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.INTEGRAL_INDEX_URL, new OkHttpClientManager.ResultCallback<ResponseBean<IntegralData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<IntegralData> response) {
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

}
