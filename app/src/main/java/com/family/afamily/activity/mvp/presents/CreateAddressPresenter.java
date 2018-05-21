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
 * Created by hp2015-7 on 2018/1/5.
 */

public class CreateAddressPresenter extends BasePresent<SubmitSuccessView> {

    public CreateAddressPresenter(SubmitSuccessView view) {
        attach(view);
    }

    public void submitData(String token, String address_id, String province, String city, String district, String address, String consignee, String tel) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("address_id", address_id);
        // params.put("is_default",is_default);
        params.put("province", province);
        params.put("city", city);
        params.put("district", district);
        params.put("address", address);
        params.put("consignee", consignee);
        params.put("tel", tel);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ADD_ADDRESS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(null);
                }
            }
        }, params);
    }

}
