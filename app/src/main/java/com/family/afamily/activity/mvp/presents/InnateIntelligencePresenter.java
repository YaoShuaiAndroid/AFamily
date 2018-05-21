package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.InnateIntelligenceView;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
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

public class InnateIntelligencePresenter extends BasePresent<InnateIntelligenceView> {

    public InnateIntelligencePresenter(InnateIntelligenceView view) {
        attach(view);
    }

    public void getData(String uid, int page, final int type) {
        if(type==1){
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(uid)){
            params.put("uid", uid);
        }
        params.put("page",""+ page);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.facultyInfo, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<InnateIntelligenceModel>>>() {
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
                    //view.toast(response.getMsg());
                    view.successData(response.getData(),type);
                }
            }
        }, params);
    }


    public void getParent(String uid, int page, final int type,int month) {
        if(type==1){
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(uid)){
            params.put("uid", uid);
        }
        params.put("page",""+ page);
        params.put("month",""+ month);

        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.guideInfo, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<InnateIntelligenceModel>>>() {
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
                    //view.toast(response.getMsg());
                    view.successData(response.getData(),type);
                }
            }
        }, params);
    }
}
