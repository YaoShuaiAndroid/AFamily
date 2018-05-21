package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ActionDetailsView;
import com.family.afamily.activity.mvp.interfaces.InnateDetailView;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.CommentModel;
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

public class InnateDetailPresenter extends BasePresent<InnateDetailView> {

    public InnateDetailPresenter(InnateDetailView view) {
        attach(view);
    }

    public void getData(String conmmentId,int page, final int type) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("commentId", conmmentId);
        params.put("page",""+ page);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.detailsFaculty, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<CommentModel>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.successData(null,type);
            }

            @Override
            public void onResponse(ResponseBean<BasePageBean<CommentModel>> response) {
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

    public void submitLike(String conmmentId,String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("contentId", conmmentId);
        params.put("token",""+ token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.earlyFacultyLike, new OkHttpClientManager.ResultCallback<ResponseBean<BasePageBean<CommentModel>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.submitLike(0);
            }

            @Override
            public void onResponse(ResponseBean<BasePageBean<CommentModel>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.submitLike(0);
                } else {
                    //view.toast(response.getMsg());
                    view.submitLike(1);
                }
            }
        }, params);
    }

    public void detailsFacultys(String conmmentId,String uid) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("commentId", conmmentId);
        if(!TextUtils.isEmpty(uid)) {
            params.put("uid", uid);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.detailsFacultys, new OkHttpClientManager.ResultCallback<ResponseBean<InnateIntelligenceModel>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<InnateIntelligenceModel> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successDetailData(response.getData());
                }
            }
        }, params);
    }

    public void earlyFacultyComment(final String conmmentId, String token, String content, String comid) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("commentId", conmmentId);
        params.put("token",""+ token);
        params.put("comment",""+ content);
        if(!TextUtils.isEmpty(comid)){
            params.put("commentedId",""+ comid);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.earlyFacultyComment, new OkHttpClientManager.ResultCallback<ResponseBean<CommentModel>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<CommentModel> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.submitComment(response.getData());
                }
            }
        }, params);
    }
}
