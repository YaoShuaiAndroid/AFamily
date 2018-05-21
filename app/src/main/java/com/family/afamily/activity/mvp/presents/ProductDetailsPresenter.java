package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ProductDetailsView;
import com.family.afamily.entity.GoodsInfoData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/3/2.
 */

public class ProductDetailsPresenter extends BasePresent<ProductDetailsView> {
    public ProductDetailsPresenter(ProductDetailsView view){
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


    public void submitLove(String token, String goods_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.PRODUCT_LOVE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
                view.submitCollectResult(0);
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
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

    public void submitCancelLove(String token, String goods_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.PRODUCT_CANCEL_LOVE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
                view.submitCollectResult(0);
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                    view.submitCollectResult(0);
                } else {
                    view.toast("取消成功");
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

}
