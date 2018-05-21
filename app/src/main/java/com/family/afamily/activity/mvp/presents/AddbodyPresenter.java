package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.AddBodyView;
import com.family.afamily.entity.BodyModel;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public class AddbodyPresenter extends BasePresent<AddBodyView> {

    public AddbodyPresenter(AddBodyView view) {
        attach(view);
    }

    /**
     * 添加宝宝
     * @param token
     * @param nickname
     * @param birthday
     * @param file
     */
    public void submitData(String token, String sex, String nickname, String birthday, String file) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("token", token);
        params.put("birthday", birthday);
        params.put("sex",sex);
        params = Utils.getParams(params);
        try {
            OkHttpClientManager.postAsyn(UrlUtils.add_child, new OkHttpClientManager.ResultCallback<ResponseBean<BodyModel>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("提交失败");
                }

                @Override
                public void onResponse(ResponseBean<BodyModel> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "添加失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        view.submitSuccess(response.getData());
                    }
                }
            }, new File(file), "avatar", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改宝宝
     *
     * @param token
     * @param file
     */
    public void submitEditData(String token, final BodyModel bodyModel, String file,int type) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("nickname", bodyModel.getNickname());
        params.put("token", token);
        params.put("birthday", bodyModel.getBirthday());
        params.put("height", bodyModel.getHeight());
        if(type==1){
            params.put("type", "1");
        }
        params.put("weight",bodyModel.getWeight());
        params.put("sex",bodyModel.getSex());
        params.put("id",bodyModel.getId());
        params = Utils.getParams(params);

        OkHttpClientManager.ResultCallback resultCallback= new OkHttpClientManager.ResultCallback<ResponseBean<BodyModel>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<BodyModel> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "添加失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    if(response.getData()==null){
                        view.submitSuccess(bodyModel);
                    }else{
                        BodyModel bodyModel1=response.getData();
                        bodyModel1.setSex(bodyModel.getSex());
                        if(bodyModel1.getIcon()==null){
                            bodyModel1.setIcon(bodyModel.getIcon());
                        }
                        view.submitSuccess(bodyModel1);
                    }
                }
            }
        };

        try {
            if(type==1&&!TextUtils.isEmpty(file)){
                OkHttpClientManager.postAsyn(UrlUtils.add_child,resultCallback, new File(file), "avatar", params);
            }else{
                OkHttpClientManager.postAsyn(UrlUtils.add_child,resultCallback, params);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
