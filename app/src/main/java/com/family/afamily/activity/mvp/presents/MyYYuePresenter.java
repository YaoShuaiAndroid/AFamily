package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.MyYYueView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public class MyYYuePresenter extends BasePresent<MyYYueView> {
    public MyYYuePresenter(MyYYueView view) {
        attach(view);
    }

    /**
     * 获取数据列表
     *
     * @param token
     */
    public void getData(String token,final int p,final int getType) {
        if(getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", p+"");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.POOL_APPOINTMENT_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.successData(null,getType);
            }

            @Override
            public void onResponse(ResponsePageBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null,getType);
                } else {
                    //view.toast(response.getMsg());
                    view.successData(response.getData(),getType);
                }
            }
        }, params);
    }

    /**
     * 提示确认
     *
     * @param mActivity
     * @param token
     * @param id
     */
    public void showCancelDialog(final Activity mActivity, final String token, final String id) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否取消该泳池预约？");
                dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isConnected(mActivity)) {
                            dialog.dismiss();
                            submitCancel(token, id);
                        }
                    }
                });
            }
        };
    }

    /**
     * 取消预约
     *
     * @param token
     * @param id
     */
    public void submitCancel(final String token, String id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.OFF_APPOINTMENT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "取消预约失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    //getData(token);
                    view.updateData();
                }
            }
        }, params);
    }

}
