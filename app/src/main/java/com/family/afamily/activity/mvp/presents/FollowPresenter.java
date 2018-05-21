package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.FollowActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.adapters.FollowAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class FollowPresenter extends BasePresent<PageSuccessView> {
    public FollowPresenter(PageSuccessView v) {
        attach(v);
    }

    /**
     * @param token
     * @param page
     * @param getType
     * @param type         1：我关注的，2：我的好友
     * @param list
     * @param recyclerView
     * @param adapter
     */
    public void getData(String token, int page, final int getType, String type,
                        final List<Map<String, String>> list, final SuperRecyclerView recyclerView, final FollowAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", page + "");
        params.put("type", type);

        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MY_FOLLOW_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                if (getType == 1) {
                    view.toast("获取数据失败");
                } else if (getType == 2) {
                    recyclerView.completeRefresh();
                } else {
                    recyclerView.completeLoadMore();
                }
            }

            @Override
            public void onResponse(ResponsePageBean<Map<String, String>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                    if (getType == 2) {
                        recyclerView.completeRefresh();
                    } else {
                        recyclerView.completeLoadMore();
                    }
                } else {
                    BasePageBean<Map<String, String>> dataBean = response.getData();
                    if (dataBean != null) {
                        view.successData(dataBean);
                        List<Map<String, String>> mapList = dataBean.getList_data();
                        if (mapList != null && mapList.size() > 0) {
                            if (getType == 1) {
                                list.clear();
                                list.addAll(mapList);
                            } else if (getType == 2) {
                                list.clear();
                                list.addAll(mapList);
                                recyclerView.completeRefresh();
                            } else {
                                list.addAll(mapList);
                                recyclerView.completeLoadMore();
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getType == 1) {
                                list.clear();
                            } else if (getType == 2) {
                                list.clear();
                                recyclerView.completeRefresh();
                            } else {
                                recyclerView.completeLoadMore();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        if (getType == 2) {
                            recyclerView.completeRefresh();
                        } else {
                            recyclerView.completeLoadMore();
                        }
                    }
                }
            }
        }, params);
    }


    public void showCancelDialog(final String token, final String user_id, final FollowActivity activity) {
        new BaseDialog(activity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否取消关注该用户？");
                dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        submitFollow(token, user_id, "2", activity);
                    }
                });
            }
        };
    }

    /**
     * 关注、取消
     *
     * @param token
     * @param user_id
     * @param attention 1：添加未关注，2：取消已关注
     */
    public void submitFollow(String token, String user_id, String attention, final FollowActivity activity) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("user_id", user_id);
        params.put("attention", attention);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_ADD_FOLLOW_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    activity.updateData();
                    // view.submitCollectResult(1);
                }
            }
        }, params);
    }

}
