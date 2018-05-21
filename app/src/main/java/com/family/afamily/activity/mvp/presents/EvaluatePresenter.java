package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class EvaluatePresenter extends BasePresent<SubmitSuccessView> {

    public EvaluatePresenter(SubmitSuccessView view) {
        attach(view);
    }

    public void submitData(String token, String order_id, String goods_id, String gild, String comm) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_id", order_id);
        params.put("goods_id", goods_id);
        params.put("comment_rank", gild);
        params.put("content", comm);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SUBMIT_EVALUATE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交价论失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(null);
                }
            }
        }, params);
    }
}
