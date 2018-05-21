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

public class BindBankPresenter extends BasePresent<BindBankView> {

    public BindBankPresenter(BindBankView view) {
        attach(view);
    }

    public void submitData(String token, String name, String number, String bk_name, String bk_zh, String bk_city, String pw) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("real_name", name);
        params.put("bank_account", number);
        params.put("bank_name", bk_name);
        params.put("bank_branch", bk_zh);
        params.put("bank_address", bk_city);
        params.put("password_account_replay", pw);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BIND_BANK_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "绑定银行卡失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.successData();
                }
            }
        }, params);
    }

}
