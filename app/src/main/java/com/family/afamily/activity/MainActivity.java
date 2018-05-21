package com.family.afamily.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.detection.DetectListActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.fragment.Fragment1;
import com.family.afamily.fragment.Fragment3;
import com.family.afamily.fragment.Fragment4;
import com.family.afamily.fragment.Fragment5;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.view.TabButtonView;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.main_tab_1)
    TabButtonView mainTab1;
    @BindView(R.id.main_tab_2)
    TabButtonView mainTab2;
    @BindView(R.id.main_tab_3)
    TabButtonView mainTab3;
    @BindView(R.id.main_tab_4)
    TabButtonView mainTab4;
    @BindView(R.id.main_tab_5)
    TabButtonView mainTab5;

    private int index = 1;
    private Fragment1 fragment1; //水与沙
    private DetectListActivity detectListActivity; //早教
    private Fragment3 fragment3; //书房
    private Fragment4 fragment4; //玩中学
    private Fragment5 fragment5; //我的
    public static final String ACTION_KILL_ALL_ACTIVITY = "com.family.afamily.killall";

    @Override
    public void netWorkConnected() {
    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //注册广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_KILL_ALL_ACTIVITY);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        String user_id = (String) SPUtils.get(mActivity, "user_id", "");
        if (!TextUtils.isEmpty(user_id)) {
            UploadDao dao = new UploadDao(mActivity);
            List<UploadVideoData> list = dao.getUploadList(user_id);
            if (list != null && list.size() > 0) {
                L.e("tag", "---------------有未完成视频上传------->");
                Intent service = new Intent(mActivity, UploadVideoService.class);
                startService(service);
            }
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();

        setTabInit(mainTab1);
        //显示第一个
        if (fragment1 == null) {
            fragment1 = new Fragment1();
            addFragment(fragment1);
            showFragment(fragment1);
        } else {
            showFragment(fragment1);
        }
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);
        mainTab4.setOnClickListener(this);
        mainTab5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab_1:
                if(index!=1){
                    index = 1;
                    if (fragment1 == null) {
                        fragment1 = new Fragment1();
                        addFragment(fragment1);
                        showFragment(fragment1);
                    } else {
                        showFragment(fragment1);
                    }
                    setTabInit(mainTab1);
                }
                break;
            case R.id.main_tab_2:
                if(index!=2){
                    index = 2;
                    if (detectListActivity == null) {
                        detectListActivity = new DetectListActivity();
                        addFragment(detectListActivity);
                        showFragment(detectListActivity);
                    } else {
                        showFragment(detectListActivity);
                    }
                    setTabInit(mainTab2);
                }
                break;
            case R.id.main_tab_3:
                if(index!=3){
                    index = 3;
                    if (fragment3 == null) {
                        fragment3 = new Fragment3();
                        addFragment(fragment3);
                        showFragment(fragment3);
                    } else {
                        showFragment(fragment3);
                    }
                    setTabInit(mainTab3);
                }
                break;
            case R.id.main_tab_4:
                if(index!=4){
                    index = 4;
                    if (fragment4 == null) {
                        fragment4 = new Fragment4();
                        addFragment(fragment4);
                        showFragment(fragment4);
                    } else {
                        showFragment(fragment4);
                    }
                    setTabInit(mainTab4);
                }
                break;
            case R.id.main_tab_5:
                if(index!=5){
                    index = 5;
                    if (fragment5 == null) {
                        fragment5 = new Fragment5();
                        addFragment(fragment5);
                        showFragment(fragment5);
                    } else {
                        showFragment(fragment5);
                    }
                    setTabInit(mainTab5);
                }
                break;
        }
    }

    private void setTabInit(TabButtonView tbv) {
        mainTab1.setSelectTab(false);
        mainTab2.setSelectTab(false);
        mainTab3.setSelectTab(false);
        mainTab4.setSelectTab(false);
        mainTab5.setSelectTab(false);
        tbv.setSelectTab(true);
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示Fragment
     **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        //  ft.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_in);
        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (fragment1 != null) {
            ft.hide(fragment1);
        }
        if (detectListActivity != null) {
            ft.hide(detectListActivity);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            ft.hide(fragment4);
        }
        if (fragment5 != null) {
            ft.hide(fragment5);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_KILL_ALL_ACTIVITY)) {
                toast(intent.getStringExtra("msg"));
                activityManager.killAllActivity();
                L.e("tag", "关闭所有activity---->");
                Intent intent1 = new Intent(context, LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }
}
