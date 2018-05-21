package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.EveyDayTextDetailsView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/21.
 */

public class EveyDayTextDetailsPresenter extends BasePresent<EveyDayTextDetailsView> {

    public EveyDayTextDetailsPresenter(EveyDayTextDetailsView baseView) {
        attach(baseView);
    }

    public void getData(String token, String id) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.EVERYDAY_TEXT_DETAILS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, Object>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<Map<String, Object>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.dataCallback(response.getData());


                }
            }
        }, params);
    }

    /**
     * 收藏、取消
     *
     * @param token
     * @param id
     * @param type    1：文章，2：视频，3：书，4：玩具/教具
     * @param collect 1：未收藏，2：已收藏
     */
    public void submitCollect(String token, String id, String collect) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("type", "1");
        params.put("collect", collect);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ADD_COLLECT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
                view.submitCollectResult(0);
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                    view.submitCollectResult(0);
                } else {
                    view.toast(response.getMsg());
                    view.submitCollectResult(1);
                }
            }
        }, params);
    }
}
