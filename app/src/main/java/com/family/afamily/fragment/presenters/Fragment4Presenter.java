package com.family.afamily.fragment.presenters;


import android.text.TextUtils;

import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.Frag4IndexData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.fragment.interfaces.Fragment4View;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class Fragment4Presenter extends BasePresent<Fragment4View> {

    public Fragment4Presenter(Fragment4View view1) {
        attach(view1);
    }


    /**
     * 获取首页数据
     *
     * @param token
     */
    public void getHomeData(String token,int type) {
        if(type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            params.put("token", token);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.WZX_INDEX_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Frag4IndexData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.successData(null);
            }

            @Override
            public void onResponse(ResponseBean<Frag4IndexData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null);
                } else {
                    // view.toast(response.getMsg());
                    view.successData(response.getData());
                }
            }
        }, params);
    }
}
