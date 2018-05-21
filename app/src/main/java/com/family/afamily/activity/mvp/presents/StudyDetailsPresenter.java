package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.OpenMemberActivity;
import com.family.afamily.activity.mvp.interfaces.StudyDetailsView;
import com.family.afamily.adapters.GoodsInfoCommAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.GoodsInfoData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class StudyDetailsPresenter extends BasePresent<StudyDetailsView> {

    public StudyDetailsPresenter(StudyDetailsView view) {
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
     * 下载音频
     *
     * @param url
     * @param filePath
     */
    public void downloadAudio(String url, String filePath, final String total_time, final Activity activity) {
        L.e("tag", url + "---------->" + filePath);
        File file = new File(filePath, getFileName(url));
        if (file.exists()) {
            showPlayAudio(activity, file.getAbsolutePath(), total_time);
        } else {
            view.showProgress(4);
            OkHttpClientManager.downloadAsyn(url, filePath, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("下载音频文件出错");
                }

                @Override
                public void onResponse(String response) {
                    if(view!=null) {
                        view.hideProgress();
                        view.toast("下载传成功");
                        showPlayAudio(activity, response, total_time);
                    }
                }
            });
        }
    }

    SeekBar seekBar;

    /**
     * 播放音频弹窗
     *
     * @param context
     * @param filePath
     * @param total_time
     */
    public void showPlayAudio(Context context, final String filePath, final String total_time) {
        new BaseDialog(context, R.layout.dialog_play_audio_layout) {
            @Override
            protected void getMView(View view, Dialog dialog) {
                seekBar = view.findViewById(R.id.seekBar);
                final TextView dialog_time_tv = view.findViewById(R.id.dialog_time_tv);
                final TextView dialog_time_total = view.findViewById(R.id.dialog_time_total);
                final ImageView dialog_play_iv = view.findViewById(R.id.dialog_play_iv);
                TextView dialog_total_tv = view.findViewById(R.id.dialog_total_tv);

                dialog_total_tv.setVisibility(View.INVISIBLE);

                final SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                final MediaPlayer mediaPlayer = new MediaPlayer();
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            if (seekBar != null) {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                dialog_time_tv.setText(format.format(mediaPlayer.getCurrentPosition()));

                                dialog_time_total.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendEmptyMessage(1);
                                    }
                                }, 1000);
                            }
                        }
                    }
                };

                try {
                    File file = new File(filePath);
                    mediaPlayer.setDataSource(file.getPath()); // 指定音频文件的路径
                    mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
                    dialog_play_iv.setTag("0");
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    dialog_play_iv.setImageResource(R.mipmap.ic_audio_pause);
                    dialog_time_total.setText(format.format(mediaPlayer.getDuration()));
                    dialog_time_total.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            handler.sendEmptyMessage(1);// 发送消息
                        }
                    }, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog_total_tv.setText("总长：" + total_time);
                dialog_play_iv.setTag("0");


                //监听进度条拖动
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.seekTo(0);
                        dialog_play_iv.setImageResource(R.mipmap.ic_audio_play);
                    }
                });

                //播放/暂停
                dialog_play_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String) dialog_play_iv.getTag();
                        if (tag.equals("1")) {
                            dialog_play_iv.setTag("0");
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                            }
                            dialog_play_iv.setImageResource(R.mipmap.ic_audio_pause);
                        } else {
                            dialog_play_iv.setTag("1");
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause(); // 暂停播放
                            }
                            dialog_play_iv.setImageResource(R.mipmap.ic_audio_play);
                        }
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        seekBar = null;
                    }
                });
            }
        };
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 添加购物车
     *
     * @param token
     * @param goods_id
     */
    public void addShoppingCar(String token, String goods_id, final int type) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("number", "1");
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

    /**
     * 提交借书
     *
     * @param token
     * @param goods_id
     * @param store_id
     */
    public void submitBorrow(String token, String goods_id, final String store_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("store_id", store_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BORROW_BOOK_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
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
                }
            }
        }, params);
    }

    public void showJieYueDialog(final Activity mActivity, final String token, final GoodsInfoData infoData) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_jie_yue_layout, null);
        ImageView dialog_img = inflate.findViewById(R.id.dialog_img);
        TextView dialog_name_tv = inflate.findViewById(R.id.dialog_name_tv);
        final TextView dialog_address_tv = inflate.findViewById(R.id.dialog_address_tv);
        TextView dialog_vip_tv = inflate.findViewById(R.id.dialog_vip_tv);
        TextView submit_btn = inflate.findViewById(R.id.submit_btn);
        RelativeLayout dialog_address_rl = inflate.findViewById(R.id.dialog_address_rl);

        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(mActivity).load(infoData.getGoods_thumb()).apply(options2).into(dialog_img);
        dialog_name_tv.setText(infoData.getGoods_name());
        final List<Map<String, String>> addList = infoData.getBook_address_list();
        if (addList != null && addList.size() > 0) {
            dialog_address_tv.setText(addList.get(0).get("address"));
            dialog_address_tv.setTag(addList.get(0).get("store_id"));
        } else {
            dialog.dismiss();
            view.toast("借书地址有误，请联系客服解决");
        }

        dialog_vip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OpenMemberActivity.class);
                mActivity.startActivity(intent);
            }
        });

        final int isVIP = infoData.getYear_number_status();
        if (isVIP == 0) {
            dialog_vip_tv.setVisibility(View.VISIBLE);
        } else {
            dialog_vip_tv.setVisibility(View.GONE);
        }
        //地址选择
        dialog_address_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addList != null && addList.size() > 1) {
                    showAddreessDialog(mActivity, addList, dialog_address_tv);
                } else {
                    view.toast("没有更多地址选择");
                }
            }
        });
        //提交按钮
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVIP == 0) {
                    view.toast("请先开通会员");
                } else {
                    String id = (String) dialog_address_tv.getTag();
                    if (TextUtils.isEmpty(id)) {
                        view.toast("请选择借书地址");
                    } else {
                        dialog.dismiss();
                        submitBorrow(token, infoData.getGoods_id(), id);
                    }
                }
            }
        });
    }


    public void showAddreessDialog(final Activity mActivity, final List<Map<String, String>> list, final TextView textView) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_address_select, null);
        RecyclerView dialog_yyue_list_rv = inflate.findViewById(R.id.dialog_borrow_list_rv);
        View dialog_yyue_root_rl = inflate.findViewById(R.id.dialog_borrow_root_rl);

        dialog_yyue_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#EEEEEE"));
        dialog_yyue_list_rv.addItemDecoration(divider);
        CommonAdapter<Map<String, String>> adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_text_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> stringStringMap, final int position) {
                holder.setText(R.id.item_text_tv, stringStringMap.get("address"));
                holder.setOnClickListener(R.id.item_text_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(stringStringMap.get("address"));
                        textView.setTag(stringStringMap.get("store_id"));
                        dialog.dismiss();
                    }
                });
            }
        };
        dialog_yyue_list_rv.setAdapter(adapter);

        dialog_yyue_root_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(inflate);

        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

}
