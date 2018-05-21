package com.family.afamily.activity.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.BaseView;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.utils.DialogLoading;
import com.family.afamily.utils.L;
import com.family.afamily.utils.Utils;


/**
 * activity 基类
 * Created by hp2015-7
 */
public abstract class BaseActivity<P extends BasePresent> extends AbsBaseActivity implements BaseView {
    protected BaseActivity mActivity = this;
    public TextView title;//标题
    private ConnectionChangeReceiver changeReceiver;//网络状态监听
    private boolean isShow = false;//是否显示了无网络界面
    private DialogLoading dialogLoading;

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //注册网络监听
        changeReceiver = new ConnectionChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(changeReceiver, mFilter);
        presenter = initPresenter();
        if (presenter != null) {
            presenter.context = mActivity;
        }
        dialogLoading = new DialogLoading(mActivity);
        super.onCreate(savedInstanceState);
    }

    public void back(View v) {
        finish();
    }


    public void setTitle(Activity activity, String v) {
        title = (TextView) activity.findViewById(R.id.base_title_tv);
        if (title != null) {
            title.setText(v);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
        if (dialogLoading != null) {
            dialogLoading = null;
        }
        //退出时取消网络监听
        if (changeReceiver != null) {
            unregisterReceiver(changeReceiver);
        }
    }

    @Override
    public void showProgress(int showType) {
        if (dialogLoading != null) {
            L.e("tag", "showType=" + showType);
            if (showType == 1) {
                dialogLoading.setDialogContext("正在登录中....");
            } else if (showType == 2) {
                dialogLoading.setDialogContext("正在提交....");
            } else if (showType == 3) {
                dialogLoading.setDialogContext("正在获取....");
            } else {
                dialogLoading.setDialogContext("正在下载....");
            }
        }
    }

    @Override
    public void hideProgress() {
        if (dialogLoading != null) {
            dialogLoading.closeDialog();
        }
    }

    @Override
    public void toast(CharSequence s) {
        Utils.showMToast(mActivity, s);
    }


    @Override
    public void initDataAsync() {
        super.initDataAsync();
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
    }

    /**
     * 网络变化监听
     */
    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                if (isShow) {
                    netWorkConnected();
                }
            }
        }
    }

    //网络已连接，通知继承者
    public abstract void netWorkConnected();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public abstract P initPresenter();
}
