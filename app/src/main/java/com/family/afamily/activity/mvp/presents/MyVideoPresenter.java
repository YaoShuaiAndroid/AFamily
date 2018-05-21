package com.family.afamily.activity.mvp.presents;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.MyVideoView;
import com.family.afamily.adapters.MyVideoAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.Config;
import com.family.afamily.utils.L;
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

public class MyVideoPresenter extends BasePresent<MyVideoView> {
    public MyVideoPresenter(MyVideoView v) {
        attach(v);
    }

    public void getData(String token, int type, int page, final int getType,
                        final List<Map<String, String>> list, final SuperRecyclerView recyclerView, final MyVideoAdapter adapter) {
        if (getType == 1) {
            view.showProgress(3);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("p", page + "");
        params.put("type", type + "");

        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.MY_VIDEO_LIST_URL, new OkHttpClientManager.ResultCallback<ResponsePageBean<Map<String, String>>>() {
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
     * 取消订单
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
        OkHttpClientManager.postAsyn(UrlUtils.DEL_MY_VIDEO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "删除视频失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.delSuccess();
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
    public void showDialog(Context context, final String token, final String id) {
        new BaseDialog(context, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否删除该视频？");
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
                        submitCancel(token, id);
                    }
                });
            }
        };
    }

    /**
     * 删除上传任务
     *
     * @param context
     * @param data
     * @param uploadDao
     */
    public void showUploadDialog(final Context context, final UploadVideoData data, final UploadDao uploadDao) {
        new BaseDialog(context, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(final View v, final Dialog dialog) {
                TextView dialog_title_tv = v.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = v.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = v.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = v.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否删除该视频？");
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
                        uploadDao.delData(data.getId());
                        Intent intent = new Intent(context, UploadVideoService.class);
                        intent.putExtra("id", data.getId());
                        context.startService(intent);
                        // uploadAsynTask.cancel(true);
                        view.updateData();
                        delete(context, data.getName(), data.getUploadId());
                    }
                });
            }
        };
    }


    //删除上传
    public void delete(Context context, String name, String uploadId) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Config.OSS_ACCESS_ID, Config.OSS_ACCESS_KEY);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSClient oss = new OSSClient(context, Config.endpoint, credentialProvider);
        AbortMultipartUploadRequest abort = new AbortMultipartUploadRequest(Config.bucket, name, uploadId);
        try {
            oss.abortMultipartUpload(abort);
            L.e("Tag", "--------删除任务成功------------->");
        } catch (ClientException e) {
            e.printStackTrace();
            L.e("Tag", "--------删除任务失败------------->");
        } catch (ServiceException e) {
            e.printStackTrace();
            L.e("Tag", "--------删除任务失败------------->");
        }
    }

    /**
     * 发布视频
     *
     * @param token
     * @param url
     * @param title
     * @param id
     * @param videoData
     * @param uploadDao
     */
    public void submitData(String token, final String url, String title, String id, final UploadVideoData videoData, final UploadDao uploadDao) {
        // view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", id);
        params.put("intro", title);
        params.put("video_url", url);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ZJ_PUT_VIDEO_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                L.e("tag", "-------发布失败--------------->");
                Utils.showMToast(context, "视频重新发布失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                if (response == null || response.getRet_code() != 1) {
                    Utils.showMToast(context, "视频重新发布失败");
                } else {
                    L.e("tag", "-------发布成功--------------->");
                    uploadDao.delData(videoData.getId());
                    view.updateData();
                }
            }
        }, params);
    }


    /**
     * 发布视频
     *
     * @param token
     * @param child_id
     * @param content
     * @param video
     * @param create_time
     * @param address
     */
    public void submitVideo(String token, String child_id, String content, final String video,
                            String create_time, String address, final UploadVideoData videoData, final UploadDao uploadDao) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("child_id", child_id);
        params.put("content", content);
        params.put("video", video);
        params.put("create_time", create_time);
        params.put("address", address);
        params.put("type", "2");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BABY_ISSUE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                Utils.showMToast(context, "视频重新发布失败");

            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                if (response == null || response.getRet_code() != 1) {
                    Utils.showMToast(context, "视频重新发布失败");
                } else {
                    L.e("tag", "-------发布成功--------------->");
                    uploadDao.delData(videoData.getId());
                    view.updateData();
                }
            }
        }, params);
    }


    public void updateUploadData() {
        view.updateData();
    }


}
