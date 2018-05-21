package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.CouponView;
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
 * Created by hp2015-7 on 2017/12/28.
 */

public class CouponPresenter extends BasePresent<CouponView> {
    public CouponPresenter(CouponView view) {
        attach(view);
    }

    /**
     * 获取数据列表
     *
     * @param token
     */
    public void getData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.COUPON_LIST_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<Map<String, String>>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<List<Map<String, String>>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    /**
     * 领取优惠券
     *
     * @param token
     * @param id
     */
    public void collarCoupon(final Activity activity, final String token, String id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.GET_COUNPON_URL, new OkHttpClientManager.ResultCallback<ResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("领取失败");
            }

            @Override
            public void onResponse(ResponseBean response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "领取失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    showCollarSuccess(activity);
                    getData(token);
                }
            }
        }, params);
    }

    private void showCollarSuccess(Activity activity) {
        new BaseDialog(activity, R.layout.toas_coupon_ok) {
            @Override
            protected void getMView(View view, Dialog dialog) {
            }
        };
    }

}
