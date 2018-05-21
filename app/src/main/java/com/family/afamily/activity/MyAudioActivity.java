package com.family.afamily.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.AudioData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.upload_db.AudioDao;
import com.family.afamily.upload_service.DownloadAudioService;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hp2015-7 on 2018/1/26.
 */

public class MyAudioActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.audio_list_rl)
    RecyclerView audioListRl;
    @BindView(R.id.audio_img)
    ImageView audioImg;
    @BindView(R.id.my_audio_time)
    TextView myAudioTime;
    @BindView(R.id.my_audio_seek_bar)
    SeekBar myAudioSeekBar;
    @BindView(R.id.my_audio_total_time)
    TextView myAudioTotalTime;
    @BindView(R.id.audio_name_tv)
    TextView audioNameTv;
    @BindView(R.id.audio_play_iv)
    ImageView audioPlayIv;
    @BindView(R.id.audio_bottom_rl)
    RelativeLayout audioBottomRl;

    private List<AudioData> list = new ArrayList<>();
    private CommonAdapter<AudioData> adapter;
    private AudioDao audioDao;
    private String user_id;
    private String downloadPath = "audio/";
    private String path;
    private MyDownloadBroadcast myDownloadBroadcast;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private MediaPlayer mediaPlayer;
    private int playId = -1;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_audio);

        user_id = (String) SPUtils.get(mActivity, "user_id", "");
        audioDao = new AudioDao(mActivity);
        update();
        if (verifyCameraPermissions()) {
            path = FileUtile.createDir(downloadPath).getPath();
            if (isUpdate) {
                if (!DownloadAudioService.isServiceRunning) {
                    Intent intent = new Intent(mActivity, DownloadAudioService.class);
                    startService(intent);
                }
            }
        }
        myDownloadBroadcast = new MyDownloadBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_audio_data");
        intentFilter.addAction("close_audio_data");
        registerReceiver(myDownloadBroadcast, intentFilter);

    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        setTitle(this, "我的音频");
        audioListRl.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#dddddd"));
        audioListRl.addItemDecoration(divider);
        adapter = new CommonAdapter<AudioData>(mActivity, R.layout.item_my_audio_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final AudioData data, final int position) {
                ImageView item_image_iv = holder.getView(R.id.item_image_iv);
                TextView item_title_tv = holder.getView(R.id.item_title_tv);
                LinearLayout item_audio_download = holder.getView(R.id.item_audio_download);
                LinearLayout item_play_btn = holder.getView(R.id.item_play_btn);
                final TextView item_progress_tv = holder.getView(R.id.item_progress_tv);
                final ProgressBar item_progress_bar = holder.getView(R.id.item_progress_bar);
                final TextView item_audio_d_tv = holder.getView(R.id.item_audio_d_tv);
                TextView item_mobile_net = holder.getView(R.id.item_mobile_net);

                RequestOptions options2 = new RequestOptions();
                options2.error(R.drawable.error_pic);
                Glide.with(mActivity).load(data.getImg()).apply(options2).into(item_image_iv);

                item_title_tv.setText(data.getAudio_name());
                L.e("tag", data.getAudioDownload() + "----------->");
                int flag = data.getDownloadFlag();
                if (flag == 0) {
                    item_audio_download.setVisibility(View.VISIBLE);
                    item_play_btn.setVisibility(View.GONE);
                    item_progress_tv.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.GONE);
                    item_audio_d_tv.setText("下载音频");
                } else if (flag == 2) {
                    item_audio_download.setVisibility(View.VISIBLE);
                    item_play_btn.setVisibility(View.GONE);
                    item_progress_tv.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.VISIBLE);
                    item_mobile_net.setText("当前为移动网络");
                    item_audio_d_tv.setText("继续下载");
                } else if (flag == 1 || flag == 4) {
                    if (data.getCurrentSize() > 0) {
                        item_progress_bar.setMax(data.getTotalSize());
                        item_progress_bar.setProgress(data.getCurrentSize());
                        item_progress_tv.setText("已下载：" + ((data.getCurrentSize() * 100) / data.getTotalSize()) + "%");
                    } else {
                        item_progress_bar.setMax(100);
                        item_progress_bar.setProgress(0);
                        item_progress_tv.setText("已下载：0%");
                    }
                    item_progress_tv.setVisibility(View.VISIBLE);
                    item_progress_bar.setVisibility(View.VISIBLE);
                    item_audio_download.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.GONE);
                    item_play_btn.setVisibility(View.GONE);
                } else if (flag == 3) {
                    item_audio_download.setVisibility(View.GONE);
                    item_play_btn.setVisibility(View.VISIBLE);
                    item_progress_tv.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.GONE);
                } else if (flag == 5) {
                    item_audio_download.setVisibility(View.VISIBLE);
                    item_play_btn.setVisibility(View.GONE);
                    item_progress_tv.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.VISIBLE);
                    item_mobile_net.setText("下载失败");
                    item_audio_d_tv.setText("继续下载");
                } else if (flag == 6) {
                    item_audio_download.setVisibility(View.VISIBLE);
                    item_play_btn.setVisibility(View.GONE);
                    item_progress_tv.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.GONE);
                    item_mobile_net.setVisibility(View.VISIBLE);
                    item_mobile_net.setText("音频下载异常");
                    item_audio_d_tv.setText("重新下载");
                }

                item_audio_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = item_audio_d_tv.getText().toString();
                        if (Utils.isWifi(mActivity) || "继续下载".equals(str) || "重新下载".equals(str)) {
                            int flag = Utils.isWifi(mContext) ? 1 : 4;
                            File d = new File(path);
                            String filePath = d.getAbsolutePath().concat("/").concat(user_id + getFileName(data.getAudioDownload()));
                            data.setAudioPath(filePath);
                            ContentValues values = new ContentValues();
                            values.put("download_flag", flag);
                            values.put("audio_path", filePath);
                            audioDao.updateMsm(values, data.getId());

                            data.setDownloadFlag(flag);
                            data.setAudioPath(filePath);
                            list.set(position,data);

                            notifyDataSetChanged();
                            Intent intent = new Intent(mActivity, DownloadAudioService.class);
                            startService(intent);
                        } else {
                            showDownloadDialog(data);
                        }
                    }
                });
                //播放视频
                item_play_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String audioPath = data.getAudioPath();
                        File file = new File(audioPath);
                        if (file.exists()) {
                            if (playId == -1 || data.getId() != playId) {
                                playId = data.getId();
                                audioBottomRl.setVisibility(View.VISIBLE);
                                playAudio(data);
                            } else {
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    toast("您正在播放该音频");
                                } else {
                                    if (mediaPlayer != null) {
                                        mediaPlayer.start();
                                    }
                                }
                            }
                        } else {
                            Utils.showMToast(mActivity, "下载的本地文件已丢失");
                            data.setDownloadFlag(0);
                            list.set(position, data);
                            ContentValues values = new ContentValues();
                            values.put("download_flag", 0);
                            values.put("current_size", 0);
                            audioDao.updateMsm(values, data.getId());
                            notifyDataSetChanged();
                        }
                    }
                });

            }
        };
        audioListRl.setAdapter(adapter);

    }

    private void showDownloadDialog(final AudioData data) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("当前网络不是WIFI状态下是否继续发布？");
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
                        File d = new File(path);
                        String filePath = d.getAbsolutePath().concat("/").concat(user_id + getFileName(data.getAudioDownload()));
                        data.setAudioPath(filePath);
                        ContentValues values = new ContentValues();
                        values.put("download_flag", 4);
                        values.put("audio_path", filePath);
                        audioDao.updateMsm(values, data.getId());
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent(mActivity, DownloadAudioService.class);
                        startService(intent);
                    }
                });
            }
        };
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private final int PERMISSIONS_CAMERA = 1;

    private boolean verifyCameraPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "本次操作需要读写权限", PERMISSIONS_CAMERA, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        path = FileUtile.createDir(downloadPath).getPath();
        L.e("tag", "------22--------->" + path);
        if (isUpdate) {
            if (!DownloadAudioService.isServiceRunning) {
                Intent intent = new Intent(mActivity, DownloadAudioService.class);
                startService(intent);
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Utils.showMToast(mActivity, "请先允许获取读写权限");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDownloadBroadcast != null) {
            unregisterReceiver(myDownloadBroadcast);
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    boolean isUpdate = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (isUpdate) {
                    update();
                    if (list.size() > 0) {
                        audioListRl.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendEmptyMessage(1);
                            }
                        }, 1500);
                    }
                }
            }
        }
    };


    class MyDownloadBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("update_audio_data".equals(action)) {
                isUpdate = true;
                update();
                handler.sendEmptyMessageDelayed(1, 100);
            } else if ("close_audio_data".equals(action)) {
                update();
            }
        }
    }

    public void update() {
        List<AudioData> data = audioDao.getAudioList(user_id);
        list.clear();
        boolean isDownloading = false;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getDownloadFlag() == 1 || data.get(i).getDownloadFlag() == 4) {
                    isDownloading = true;
                }
            }
            isUpdate = isDownloading;
            list.addAll(data);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    myAudioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    myAudioTime.setText(format.format(mediaPlayer.getCurrentPosition()));
                    myAudioTime.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendEmptyMessage(1);
                        }
                    }, 1000);
                }
            }
        }
    };

    public void playAudio(final AudioData data) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.tx)
                .transform(new GlideCircleTransform(mActivity));
        Glide.with(mActivity).load(data.getImg()).apply(options).into(audioImg);
        audioNameTv.setText(data.getAudio_name());
        L.e("tag", "----------------------0000--------");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(data.getAudioPath()); // 指定音频文件的路径
            mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
            mediaPlayer.start();

            audioPlayIv.setImageResource(R.mipmap.ic_my_audio_pause);
            audioPlayIv.setTag(0);
            myAudioSeekBar.setMax(mediaPlayer.getDuration());
            myAudioTotalTime.setText(format.format(mediaPlayer.getDuration()));
            myAudioTotalTime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);// 发送消息
                }
            }, 100);


            //监听进度条拖动
            myAudioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    L.e("tag", seekBar.getMax() + "-------------2------->" + seekBar.getProgress());

                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.seekTo(0);
                    audioPlayIv.setTag(1);
                    audioPlayIv.setImageResource(R.mipmap.ic_my_audio_paly);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.audio_play_iv)
    public void clickPlay() {
        int tag = (int) audioPlayIv.getTag();
        if (tag == 1) {
            audioPlayIv.setTag(0);
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            audioPlayIv.setImageResource(R.mipmap.ic_my_audio_pause);
        } else {
            audioPlayIv.setTag(1);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); // 暂停播放
            }
            audioPlayIv.setImageResource(R.mipmap.ic_my_audio_paly);
        }
    }


}
