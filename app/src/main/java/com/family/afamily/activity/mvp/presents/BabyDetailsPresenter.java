package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.BabyDetailsView;
import com.family.afamily.entity.BabyDetailsData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class BabyDetailsPresenter extends BasePresent<BabyDetailsView> {

    public BabyDetailsPresenter(BabyDetailsView view) {
        attach(view);
    }

    public void getData(String token, String id, int p, final int getType) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BABY_DETAILS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<BabyDetailsData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.successData(null, getType, false);
            }

            @Override
            public void onResponse(ResponseBean<BabyDetailsData> response) {
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
}
