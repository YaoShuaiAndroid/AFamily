package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.SearchView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.entity.SearchData;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/15.
 */

public class SearchPresenter extends BasePresent<SearchView> {

    public SearchPresenter(SearchView view) {
        attach(view);
    }

    /**
     *
     * @param token
     * @param isZaoj
     */
    public void getData(String token,boolean isZaoj) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        String url = isZaoj ? UrlUtils.SEARCH_LIST_URL : UrlUtils.PRODUCT_SEARCH_URL;
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ResponseBean<SearchData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<SearchData> response) {
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

    public void submitData(String token, String word, boolean isZaoj, int p, final int getType) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("search_word", word);
        params.put("p", p+"");
        params = Utils.getParams(params);
        String url = isZaoj ? UrlUtils.SUBMIT_SEARCH_URL : UrlUtils.SUBMIT_PRODUCT_SEARCH_URL;
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
                view.searchSuccess(null,getType);
            }

            @Override
            public void onResponse(ResponsePageBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.searchSuccess(null,getType);
                } else {
                    //view.toast(response.getMsg());
                    view.searchSuccess(response.getData(),getType);
                }
            }
        }, params);
    }

}
