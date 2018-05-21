package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.MsgView;
import com.family.afamily.activity.mvp.interfaces.PageSuccessView;
import com.family.afamily.adapters.MsgAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.MsgData;
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

public class MsgPresenter extends BasePresent<MsgView> {

    public MsgPresenter(MsgView view) {
        attach(view);
    }

    public void getData(String token, int page, final int getType,
                        final List<MsgData> list, final SuperRecyclerView recyclerView, final MsgAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", page + "");

        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MSG_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<MsgData>>() {
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
            public void onResponse(ResponsePageBean<MsgData> response) {
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
                    BasePageBean<MsgData> dataBean = response.getData();
                    if (dataBean != null) {
                        view.successData(dataBean);
                        List<MsgData> mapList = dataBean.getList_data();
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


    public void submitDel(String token,String type,String ids){
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", type);
        if(type.equals("2")){
            params.put("ids", ids);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.DEL_MSG_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
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
                    view.successData();
                }
            }
        },params);
    }

    public void showDeleteDialog(Activity mActivity, final String token, final String ids, final String type) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                if(type.equals("1")) {
                    dialog_content_tv.setText("是否清空消息列表？");
                }else{
                    dialog_content_tv.setText("是否删除选中条目？");
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
                        submitDel(token,type,ids);
                        //presenter.delCarNumber(token, rec_id);
                    }
                });
            }
        };
    }

}
