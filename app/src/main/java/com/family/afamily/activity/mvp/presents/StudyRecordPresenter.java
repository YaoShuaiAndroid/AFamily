package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.StudyRecordView;
import com.family.afamily.adapters.StudyRecordAdapter;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.StudyRecordData;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class StudyRecordPresenter extends BasePresent<StudyRecordView> {

    public StudyRecordPresenter(StudyRecordView view) {
        attach(view);
    }

    public void getData(String token, int p, final int getType, final List<Map<String, String>> list, final SuperRecyclerView recyclerView, final StudyRecordAdapter adapter) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", p + "");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.STUDY_RECORD_URL, new OkHttpClientManager.ResultCallback<ResponseBean<StudyRecordData>>() {
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
            public void onResponse(ResponseBean<StudyRecordData> response) {
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
                    StudyRecordData dataBean = response.getData();
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

}
