package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.DetectListView;
import com.family.afamily.entity.BodyModel;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public class DetectListPresenter extends BasePresent<DetectListView> {

    public DetectListPresenter(DetectListView view) {
        attach(view);
    }

    public void getData(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.childInfo, new OkHttpClientManager.ResultCallback<ResponseBean<List<BodyModel>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<List<BodyModel>> response) {
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
