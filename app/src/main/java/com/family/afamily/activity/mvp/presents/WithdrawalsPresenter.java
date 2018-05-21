package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.BindBankView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class WithdrawalsPresenter extends BasePresent<BindBankView> {

    public WithdrawalsPresenter(BindBankView view) {
        attach(view);
    }

    public void submitData(String token, String money, String pw) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("amount", money);
        params.put("password_account_replay", pw);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.WITHDRAW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "申请提现失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.successData();
                }
            }
        }, params);
    }
}
