package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.MyActionView;
import com.family.afamily.adapters.MyActionAdapter;
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
 * Created by hp2015-7 on 2018/1/14.
 */

public class MyActionPresenter extends BasePresent<MyActionView> {

    public MyActionPresenter(MyActionView view) {
        attach(view);
    }

    public void getData(String token, final int type, int p, final int getType, final List<Map<String, String>> list, final SuperRecyclerView recyclerView, final MyActionAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", p + "");
        params.put("type", type + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MY_ACTION_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
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
                            adapter.setType(type);
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

    /**
     * 取消报名
     *
     * @param token
     * @param id
     */
    public void submitCancel(String token, String id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CANCEL_ACTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "取消报名失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess();
                }
            }
        }, params);
    }

    public void submitDel(String token, String id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.DEL_ACTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "删除活动失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess();
                }
            }
        }, params);
    }


    /**
     * 提示框
     *
     * @param context
     * @param token
     * @param id
     */
    public void showDialog(Context context, final String token, final String id, final int type) {
        new BaseDialog(context, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                if (type == 1) {
                    dialog_content_tv.setText("是否取消参与活动？");
                } else {
                    dialog_content_tv.setText("是否删除该活动？");
                }

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
                        if (type == 1) {
                            submitCancel(token, id);
                        } else {
                            submitDel(token, id);
                        }
                    }
                });
            }
        };
    }
}
