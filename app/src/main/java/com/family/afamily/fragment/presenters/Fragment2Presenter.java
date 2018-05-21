package com.family.afamily.fragment.presenters;

import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.FollowData;
import com.family.afamily.entity.MasterData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ZJRecommendData;
import com.family.afamily.fragment.interfaces.Fragment2View;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class Fragment2Presenter extends BasePresent<Fragment2View> {
    public Fragment2Presenter(Fragment2View view1) {
        attach(view1);
    }

    /**
     * 获取推荐列表
     *
     * @param token
     */
    public void getRecommendList(String token, int type) {
        if (type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_RECOMMEND_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<ZJRecommendData>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<List<ZJRecommendData>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successRecommend(response.getData());
                }
            }
        }, params);
    }

    /**
     * 获取关注列表
     *
     * @param token
     */
    public void getFollowList(String token, int type) {
        if (type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_FOLLOW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<FollowData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<FollowData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successFollow(response.getData());
                }
            }
        }, params);
    }

    /**
     * 获取达人列表
     *
     * @param token
     */
    public void getMasterList(String token, int type) {
        if (type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        L.e("Tag", params.toString());
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_MASTER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<MasterData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<MasterData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    //view.toast(response.getMsg());
                    view.successMaster(response.getData());
                }
            }
        }, params);
    }

    /**
     * 获取活动列表
     *
     * @param token
     */
    public void getActionList(String token, int type) {
        if (type == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ACTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<Map<String, String>>>>() {
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
                    view.successAction(response.getData());
                }
            }
        }, params);
    }

    /**
     * 关注、取消
     *
     * @param token
     * @param user_id
     * @param attention 1：未关注，2：已关注
     */
    public void submitFollow(String token, String user_id, String attention) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("user_id", user_id);
        params.put("attention", attention);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ADD_FOLLOW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
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
                    // view.submitCollectResult(0);
                } else {
                    view.toast(response.getMsg());
                    view.submitFollow();
                }
            }
        }, params);
    }
}
