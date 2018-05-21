package com.family.afamily.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.ConfigData;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/12.
 */

public class SettingActivity extends BaseActivity {
    //@BindView(R.id.setting_clean_history)
    //  RelativeLayout settingCleanHistory;
    @BindView(R.id.setting_clean_cache)
    RelativeLayout settingCleanCache;
    @BindView(R.id.setting_feedback)
    RelativeLayout settingFeedback;
    @BindView(R.id.setting_check_update)
    RelativeLayout settingCheckUpdate;
    @BindView(R.id.setting_about_us)
    RelativeLayout settingAboutUs;
    @BindView(R.id.setting_qr_code)
    RelativeLayout settingQrCode;
    @BindView(R.id.setting_logout_btn)
    TextView settingLogoutBtn;
    @BindView(R.id.setting_update_has)
    TextView settingUpdateHas;
    @BindView(R.id.setting_update_not)
    TextView settingUpdateNot;

    private ConfigData configData;

    private boolean isUpdataVersion = false;//是否有版本更新
    private int progress;//下载进度
    private boolean cancelUpdate;//取消下载
    private Dialog dialog;
    private ProgressBar dialog_update_pbar;//更新进度条

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        configData = (ConfigData) Utils.load(FileUtile.configPath(this));
    }

    @Override
    public void netWorkConnected() {

    }

    /**
     * 添加意见反馈
     */
    @OnClick(R.id.setting_feedback)
    public void clickFeedBack() {
        startActivity(FeedBackActivity.class);
    }

    @OnClick(R.id.setting_about_us)
    public void clickAbout() {
        startActivity(AboutUsActivity.class);
    }

    @OnClick(R.id.setting_qr_code)
    public void clickQRCode() {
        startActivity(WXQRCodeActivity.class);
    }

    @OnClick(R.id.setting_clean_cache)
    public void clickCleanCache() {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否清除应用缓存？");
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
                        toast("清除成功");
                    }
                });
            }
        };
    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "设置");

        if (configData != null) {
            if (configData.getVersion_number() > getVersion()) {
                isUpdataVersion = true;
                settingUpdateNot.setVisibility(View.GONE);
                settingUpdateHas.setVisibility(View.VISIBLE);
            } else {
                isUpdataVersion = false;
                settingUpdateNot.setVisibility(View.VISIBLE);
                settingUpdateHas.setVisibility(View.GONE);
            }
        } else {
            settingUpdateNot.setVisibility(View.VISIBLE);
            settingUpdateHas.setVisibility(View.GONE);
        }

        settingCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdataVersion && configData != null) {
                    if (configData != null && !TextUtils.isEmpty(configData.getAnzhuo_url())) {
                        showUpdateDialog(mActivity, "版本更新", configData.getVersion_info(), "更新", configData);
                    } else {
                        Utils.showMToast(mActivity, "获取数据失败");
                    }
                } else {
                    Utils.showMToast(mActivity, "您已使用最新版本，无需更新");
                }
            }
        });

    }

    @OnClick(R.id.setting_logout_btn)
    public void clickLogout() {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否退出登录？");
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
                        SPUtils.remove(mActivity, "token");
                        SPUtils.remove(mActivity, "user_id");
                        activityManager.killAllActivity();
                        startActivity(LoginActivity.class);
                    }
                });
            }
        };
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Dialog showUpdateDialog(final Activity mActivity, String title, String content,
                                   String confirmName, final ConfigData update) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.dialog);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_update_layout, null);
//        AutoUtils.auto(view);
        builder.setView(view);
        TextView title_tv = (TextView) view.findViewById(R.id.dialog_title_tv);
        TextView content_tv = (TextView) view.findViewById(R.id.dialog_content_tv);
        TextView dialog_cancel_tv = (TextView) view.findViewById(R.id.dialog_cancel_tv);
        TextView dialog_confirm_tv = (TextView) view.findViewById(R.id.dialog_confirm_tv);
        dialog_update_pbar = (ProgressBar) view.findViewById(R.id.dialog_update_pbar);
        title_tv.setText(title);
        content_tv.setText(content);
        dialog_confirm_tv.setText(confirmName);

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnected(mActivity)) {
                    dialog_update_pbar.setVisibility(View.VISIBLE);
                    new downloadApkThread(update.getAnzhuo_url(), update.getVersion_name()).start();
                }
            }
        });
        return dialog;
    }

    /**
     * 开启线程下载apk，下载完自动安装
     */
    private class downloadApkThread extends Thread {
        String path;
        String version;

        public downloadApkThread(String path, String version) {
            this.path = path;
            this.version = version;
        }

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = FileUtile.getApkDirestory();
                    URL url = new URL(path);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(sdpath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(sdpath, version);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        handler.sendEmptyMessage(1);
                        if (numread <= 0) {
                            // 下载完成
                            Message msg = handler.obtainMessage();
                            msg.what = 2;
                            msg.obj = version;
                            handler.sendMessage(msg);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(3);
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(4);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//更新进度
                dialog_update_pbar.setVisibility(View.VISIBLE);
                dialog_update_pbar.setMax(100);
                dialog_update_pbar.setProgress(progress);
            } else if (msg.what == 2) {//安装
                installApk((String) msg.obj);
                if (dialog != null) {
                    dialog.dismiss();
                }
                dialog_update_pbar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 3) {
                Utils.showMToast(mActivity, "下载错误");
                if (dialog != null) {
                    dialog.dismiss();
                }
            } else {
                Utils.showMToast(mActivity, "请插入SD卡");
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    };

    /**
     * 安装APK文件
     */
    private void installApk(String version) {
        File apkfile = new File(FileUtile.getApkDirestory(), version);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致  com.qmkuaisong.fileprovider
            Uri install = FileProvider.getUriForFile(this, "com.yjlx.afamily.fileprovider", apkfile);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setDataAndType(install, "application/vnd.android.package-archive");
        } else {
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        }
        startActivity(i);
    }

}
