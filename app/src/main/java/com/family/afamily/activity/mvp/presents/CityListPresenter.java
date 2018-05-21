package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.CityListView;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.adapters.ActionSignAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class CityListPresenter extends BasePresent<CityListView> {

    public CityListPresenter(CityListView view) {
        attach(view);
    }

    public void getData() {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.city, new OkHttpClientManager.ResultCallback<ResponseBean<List<String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<List<String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successData(response.getData());
                }
            }
        }, params);
    }
}
