package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.PhysiqueView;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public class PhysiquePresenter extends BasePresent<PhysiqueView> {

    public PhysiquePresenter(PhysiqueView view) {
        attach(view);
    }

    public void getBehaviorInfo(final String id, String page, String uid, final int type) {
        Map<String, String> params = new HashMap<>();
        params.put("type",id);
        params.put("page",page);
        if(TextUtils.isEmpty(uid)){
            params.put("uid",uid);
        }
        params = Utils.getParams(params);

        OkHttpClientManager.postAsyn(UrlUtils.behaviorInfo, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<InnateIntelligenceModel>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.successData(null,type);
            }

            @Override
            public void onResponse(ResponseBean<BasePageBean<InnateIntelligenceModel>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null,type);
                } else {
                    view.successData(response.getData(),type);
                }
            }
        }, params);
    }

}
