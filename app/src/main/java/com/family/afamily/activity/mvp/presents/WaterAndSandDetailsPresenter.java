package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.WaterAndSandDetailsView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/26.
 */

public class WaterAndSandDetailsPresenter extends BasePresent<WaterAndSandDetailsView> {

    public WaterAndSandDetailsPresenter(WaterAndSandDetailsView view) {
        attach(view);
    }

    public void getData(String token, String id, boolean sand) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);

        String url = sand ? UrlUtils.SAND_INFO_URL : UrlUtils.POOL_DETAILS_URL;

        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
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
