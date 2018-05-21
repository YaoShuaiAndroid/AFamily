package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.EveyDayTextView;
import com.family.afamily.adapters.EverydayTextAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/21.
 */

public class EveyDayTextPresenter extends BasePresent<EveyDayTextView> {

    public EveyDayTextPresenter(EveyDayTextView baseView) {
        attach(baseView);
    }

    public void getListData(String token, int page, final SuperRecyclerView recyclerView, final int getType, final List<Map<String, Object>> list, final EverydayTextAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", page + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.EVERYDAY_TEXT_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, Object>>>() {
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
            public void onResponse(ResponsePageBean<Map<String, Object>> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    if (getType == 1) {
                        String msg = response == null ? "获取数据失败" : response.getMsg();
                        view.toast(msg);
                    } else if (getType == 2) {
                        recyclerView.completeRefresh();
                    } else {
                        recyclerView.completeLoadMore();
                    }
                } else {
                    BasePageBean<Map<String, Object>> dataBean = response.getData();
                    if (dataBean != null) {
                        view.dataCallback(dataBean);
                        List<Map<String, Object>> mapList = dataBean.getList_data();
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
}
