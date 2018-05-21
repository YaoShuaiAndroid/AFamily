package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.ReleaseVideoView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/2.
 */

public class ReleaseVideoPresenter extends BasePresent<ReleaseVideoView> {

    public ReleaseVideoPresenter(ReleaseVideoView view) {
        attach(view);
    }

    public void getTypeList(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_TYPE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<Map<String, String>>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取类型数据失败");
            }

            @Override
            public void onResponse(ResponseBean<List<Map<String, String>>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取类型数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successTypeData(response.getData());
                }
            }
        }, params);
    }

    public void submitData(String token, String url, String title, String id) {
        // view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", id);
        params.put("intro", title);
        params.put("video_url", url);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_PUT_VIDEO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "发布失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess();
                }
            }
        }, params);
    }

}
