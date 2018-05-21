package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.Frag4DetailsView;
import com.family.afamily.adapters.GoodsInfoCommAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.GoodsInfoData;
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
 * Created by hp2015-7 on 2018/1/6.
 */

public class Frag4DetailsPresenter extends BasePresent<Frag4DetailsView> {

    public Frag4DetailsPresenter(Frag4DetailsView view) {
        attach(view);
    }

    public void getData(String token, String goods_id) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SF_GOODS_DETAILS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<GoodsInfoData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<GoodsInfoData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    public void getCommentLsit(String token, String goods_id, int p, final int getType,
                               final List<Map<String, String>> list, final SuperRecyclerView recyclerView, final GoodsInfoCommAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SF_COMMENT_LSIT_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
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

    /**
     * 收藏、取消
     *
     * @param token
     * @param goods_id
     * @param collect  如果想取消就传1 不取消就不传参数
     */
    public void submitCollect(String token, String goods_id, String collect) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("cancel_collect", collect);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SF_COLLECTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
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
     * 添加购物车
     *
     * @param token
     * @param goods_id
     */
    public void addShoppingCar(String token, String goods_id, String spec, final int type) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("number", "1");
        params.put("spec", spec);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SF_ADD_CAR_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
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
                    view.submitCollectResult(type);
                }
            }
        }, params);
    }

}
