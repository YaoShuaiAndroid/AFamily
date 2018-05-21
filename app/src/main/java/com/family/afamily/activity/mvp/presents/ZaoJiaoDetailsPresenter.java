package com.family.afamily.activity.mvp.presents;

import android.text.TextUtils;

import com.family.afamily.activity.mvp.interfaces.ZaoJiaoDetailsView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/29.
 */

public class ZaoJiaoDetailsPresenter extends BasePresent<ZaoJiaoDetailsView> {
    public ZaoJiaoDetailsPresenter(ZaoJiaoDetailsView view) {
        attach(view);
    }

    public void getData(String token, String id, String study) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("study", study);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_DETAILS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();

            }

            @Override
            public void onResponse(ResponseBean<Map<String, String>> response) {
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
     * 获取评论列表
     *
     * @param token
     * @param id
     * @param p
     */
    public void getCommentList(String token, String id, int p, final int getype) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_COMMENT_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponsePageBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    view.successCommentList(null, getype);
                } else {
                    //view.toast(response.getMsg());
                    view.successCommentList(response.getData(), getype);
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
        params.put("type", "2");
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

    /**
     * 点赞、取消
     *
     * @param token
     * @param id
     * @param is_like 1：未点过赞，2：点过赞
     */
    public void submitZan(String token, String id, String is_like) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("is_like", is_like);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_ZAN_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
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

    public void submitComment(String token, String id, String comment_id, String content, File file) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("commented_id", TextUtils.isEmpty(comment_id) ? "" : comment_id);
        params.put("comment", content);
        params = Utils.getParams(params);

        if (TextUtils.isEmpty(content)) {
            try {
                OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_COMMENT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        view.hideProgress();
                        view.toast("评论失败");
                    }

                    @Override
                    public void onResponse(ResponseBean<Map<String, String>> response) {
                        view.hideProgress();
                        if (response == null || response.getRet_code() != 1) {
                            String msg = response == null ? "评论失败" : response.getMsg();
                            view.toast(msg);
                        } else {
                            view.toast(response.getMsg());
                            view.successComment();
                        }
                    }
                }, file, "avatar", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            OkHttpClientManager.postAsyn(UrlUtils.ZJ_VIDEO_COMMENT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("评论失败");
                }

                @Override
                public void onResponse(ResponseBean<Map<String, String>> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "评论失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        view.successComment();
                    }
                }
            }, params);

        }
    }

}
