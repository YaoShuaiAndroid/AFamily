package com.family.afamily.activity.mvp.presents;

import android.widget.TextView;

import com.family.afamily.activity.mvp.interfaces.BabyChartView;
import com.family.afamily.entity.BabyChartData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hp2015-7 on 2018/3/6.
 */

public class BabyChartPresenter extends BasePresent<BabyChartView> {
    public BabyChartPresenter(BabyChartView view){
        attach(view);
    }

    public void getData(String token,String type){
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", type);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BABY_CHART_URL, new OkHttpClientManager.ResultCallback<ResponseBean<BabyChartData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取失败");
            }

            @Override
            public void onResponse(ResponseBean<BabyChartData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }


}
