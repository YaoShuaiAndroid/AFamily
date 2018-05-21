package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.ConductTestView;
import com.family.afamily.adapters.CommonChaAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ConductClassListModel;
import com.family.afamily.entity.ConductClassModel;
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

public class ConductTestPresenter extends BasePresent<ConductTestView> {

    public ConductTestPresenter(ConductTestView view) {
        attach(view);
    }

    public void getData(String month) {
        Map<String, String> params = new HashMap<>();
        params.put("month",month);
        params = Utils.getParams(params);

        OkHttpClientManager.postAsyn(UrlUtils.behaviorType, new OkHttpClientManager.ResultCallback<ResponseBean<ConductClassListModel>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<ConductClassListModel> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successClassData(response.getData());
                }
            }
        }, params);
    }

    public void getBehaviorInfo(String type,String month,String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        params.put("month",month);
        params.put("uid",uid);
        params = Utils.getParams(params);
        //params.put("page",page);

        OkHttpClientManager.postAsyn(UrlUtils.behaviorInfo, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<InnateIntelligenceModel>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<BasePageBean<InnateIntelligenceModel>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null);
                } else {
                    //view.toast(response.getMsg());
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    public void setNotify(List<ConductClassModel> data, CommonChaAdapter commonChaAdapter,int position) {
        for (int i = 0; i <data.size() ; i++) {
            if(i==position){
                data.get(i).setSelect(true);
            }else{
                data.get(i).setSelect(false);
            }
        }

        commonChaAdapter.notifyDataSetChanged();
    }
}
