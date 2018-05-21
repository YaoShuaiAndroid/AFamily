package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.CollectionActivity;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.adapters.CollectionAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponseBean;
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
 * Created by hp2015-7 on 2018/1/9.
 */

public class CollectionPresenter extends BasePresent<PageSuccessView> {

    public CollectionPresenter(PageSuccessView view) {
        attach(view);
    }

    /**
     * @param token
     * @param type         1：文章，2：视频，3：书，4：玩具/教具
     * @param page
     * @param getType
     * @param list
     * @param recyclerView
     * @param adapter
     */
    public void getData(String token, final int type, int page, final int getType, final List<Map<String, String>> list,
                        final SuperRecyclerView recyclerView, final CollectionAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", type + "");
        params.put("p", page + "");

        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.COLLECT_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
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
     * 删除收藏
     *
     * @param token
     * @param id
     * @param type
     * @param activity
     */
    public void cancelCollect(String token, String id, String type, final CollectionActivity activity) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("type", type);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.DEL_COLLECT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "删除失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    activity.updataData();
                }
            }
        }, params);
    }
}
