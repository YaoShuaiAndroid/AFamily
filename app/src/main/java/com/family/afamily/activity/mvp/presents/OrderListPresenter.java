package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.OrderListView;
import com.family.afamily.adapters.OrderListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.OrderListData;
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
 * Created by hp2015-7 on 2018/1/10.
 */

public class OrderListPresenter extends BasePresent<OrderListView> {

    public OrderListPresenter(OrderListView view) {
        attach(view);
    }

    public void getData(String token, String status, int page, final int getType,
                        final List<OrderListData> list, final SuperRecyclerView recyclerView, final OrderListAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", page + "");
        //if(!TextUtils.isEmpty(status)) {
        params.put("status", status);
        // }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ORDER_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<OrderListData>>() {
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
            public void onResponse(ResponsePageBean<OrderListData> response) {
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
                    BasePageBean<OrderListData> dataBean = response.getData();
                    if (dataBean != null) {
                        view.successData(dataBean);
                        List<OrderListData> mapList = dataBean.getList_data();
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

    /**
     * 催单
     *
     * @param token
     * @param order_id
     */
    public void submitReminder(String token, String order_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_id", order_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MY_REMINDER_ORDER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交催单失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(false);
                }
            }
        }, params);
    }

    /**
     * 提交兑换
     *
     * @param token
     * @param order_id
     */
    public void submitRefund(String token, String order_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_id", order_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.APPLY_REFUND_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交退货失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(true);
                }
            }
        }, params);
    }

    /**
     * 确认收货
     *
     * @param token
     * @param order_id
     */
    public void submitConfirm(String token, String order_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_id", order_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.AFFIRM_RECEIVED_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交确认收货失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(true);
                }
            }
        }, params);
    }

    /**
     * 取消订单
     *
     * @param token
     * @param order_id
     */
    public void submitCancel(String token, String order_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("order_id", order_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CANCEL_ORDER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "取消订单失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess(true);
                }
            }
        }, params);
    }

    /**
     * 提示框
     *
     * @param context
     * @param token
     * @param order_id
     * @param showType 1是取消订单，2是申请退货 ，3是确认收货
     */
    public void showDialog(Context context, final String token, final String order_id, final int showType) {
        new BaseDialog(context, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                if (showType == 1) {
                    dialog_content_tv.setText("是否取消该订单？");
                } else if (showType == 2) {
                    dialog_content_tv.setText("是否确认申请退货？");
                } else {
                    dialog_content_tv.setText("请确认收到货后再进行此项操作，已确认收到货？");
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
                        if (showType == 1) {
                            submitCancel(token, order_id);
                        } else if (showType == 2) {
                            submitRefund(token, order_id);
                        } else {
                            submitConfirm(token, order_id);
                        }
                    }
                });
            }
        };
    }

}
