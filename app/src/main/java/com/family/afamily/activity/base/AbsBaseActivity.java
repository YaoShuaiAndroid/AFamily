package com.family.afamily.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.family.afamily.MyApplication;
import com.family.afamily.utils.ActivityManager;

import butterknife.ButterKnife;


/**
 * 基类
 * Created by hp2015-7
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements OnDataListener {
    protected AbsBaseActivity mActivity = this;
    protected MyApplication myApplication;
    protected ActivityManager activityManager = ActivityManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityManager.addActivity(this);
        myApplication = (MyApplication) getApplication();
        onCreateActivity(savedInstanceState);
        ButterKnife.bind(this);
        initDataAsync();
        initDataSync();
    }

    @Override
    public void initDataAsync() {

    }

    @Override
    public void initDataSync() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 创建Activity
     *
     * @param savedInstanceState Bundle对象
     */
    protected abstract void onCreateActivity(Bundle savedInstanceState);

    /**
     * Activity间跳转
     *
     * @param cls 目标Activity
     */
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(mActivity, cls));
    }

    /**
     * Activity间跳转（传值）
     *
     * @param cls    目标Activity
     * @param bundle 传递的数据
     */
    protected void startActivity(Class<?> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(mActivity, cls);
        if (bundle != null)
            intent.putExtras(bundle);// 添加数据
        startActivity(intent);
    }

    /**
     * Activity间跳转（带返回值）
     *
     * @param cls         目标Activity
     * @param requestCode 请求码
     */
    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(mActivity, cls), requestCode);
    }

    /**
     * Activity间跳转（带返回值且传值）
     *
     * @param cls         目标Activity
     * @param requestCode 请求码
     * @param bundle      传递的数据
     */
    protected void startActivityForResult(Class<?> cls, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(mActivity, cls);
        if (bundle != null)
            intent.putExtras(bundle);// 添加数据
        startActivityForResult(intent, requestCode);
    }

    //    ---------------------------------start 点击空白处隐藏键盘-------------------------------------------------
    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判断是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    ---------------------------------end 点击空白处隐藏键盘-------------------------------------------------
}

