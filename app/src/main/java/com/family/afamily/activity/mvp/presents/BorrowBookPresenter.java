package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.BorrowBookView;
import com.family.afamily.entity.BorrowBookData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class BorrowBookPresenter extends BasePresent<BorrowBookView> {
    public BorrowBookPresenter(BorrowBookView view) {
        attach(view);
    }

    public void getData(String token, int p, final int getType) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MY_BORROW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<BorrowBookData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.successData(null, getType, false);
            }

            @Override
            public void onResponse(ResponseBean<BorrowBookData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successData(null, getType, false);
                } else {
                    view.successData(response.getData(), getType, true);
                }
            }
        }, params);
    }
}
