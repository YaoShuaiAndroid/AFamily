package com.family.afamily.activity;

import  android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.CityListView;
import com.family.afamily.activity.mvp.presents.CityListPresenter;
import com.family.afamily.entity.WeChatBean;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.ContactLocaleUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.sidebar.widget.SuperSideBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.qqtheme.framework.util.LogUtils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hp2015-7 on 2017/12/15.
 */

public class CityListActivity extends BaseActivity<CityListPresenter>
        implements BDLocationListener ,EasyPermissions.PermissionCallbacks,CityListView {
    @BindView(R.id.city_list_location_tv)
    TextView cityListLocationTv;
    @BindView(R.id.city_list_refresh)
    TextView cityListRefresh;
    @BindView(R.id.city_list_rv)
    RecyclerView cityListRv;
    @BindView(R.id.super_side_bar)
    SuperSideBar superSideBar;
    @BindView(R.id.super_tv_hint)
    TextView superTvHint;
    @BindView(R.id.city_item_location_rl)
    RelativeLayout cityItemLocationRl;

    private LocationClient mLocClient;//定位类
    private LocationClientOption option;

    private List<WeChatBean> list = new ArrayList<>();
    private CommonAdapter<WeChatBean> adapter;

    private LinearLayoutManager layoutManager;

    //申请权限
    private static String[] PERMISSIONS_STORAGE = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list.addAll((Collection<? extends WeChatBean>) msg.obj);

            adapter.notifyDataSetChanged();

            superSideBar.setSourceDatas(list)// 设置数据
                    .invalidate();
        }
    };

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_city_list);
        verifyStoragePermissions();

        getData();
    }

    @Override
    public void netWorkConnected() {

    }

    /**
     * 获取行为类别
     */
    public void getData() {
        if (AppUtil.checkNetWork(mActivity)) {
            presenter.getData();
        } else {
            toast("网络异常");
        }
    }

    @Override
    public CityListPresenter initPresenter() {
        return new CityListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "城市列表");
        mLocation();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityListRv.setLayoutManager(layoutManager);

        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#EEEEEE"));
        cityListRv.addItemDecoration(divider);
        adapter = new CommonAdapter<WeChatBean>(mActivity, R.layout.item_city_list_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final WeChatBean stringStringMap, int position) {
                holder.setText(R.id.item_text_tv, stringStringMap.getName());

               /* TextView item_text_tv=holder.getView(R.id.item_text_tv);

                if(stringStringMap.getLabel().equals("-1")){
                    item_text_tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    item_text_tv.setTextColor(Color.parseColor("#666666"));
                }else{
                    item_text_tv.setTextColor(Color.parseColor("#333333"));
                    item_text_tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }*/

                holder.setOnClickListener(R.id.item_text_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent data = new Intent();
                            data.putExtra("city", stringStringMap.getName());
                            setResult(100, data);
                            finish();
                    }
                });

            }
        };
        cityListRv.setAdapter(adapter);
      /*  superSideBar.setmPressedShowTextView(superTvHint)// 设置滑动的字母A,B,C
                .setNeedRealIndex(true)// 设置需要真实的索引
                .setmLayoutManager(layoutManager);// 设置RecyclerView的LayoutManager*/

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = myApplication.cityJson();
                mHandler.sendMessage(msg);
            }
        }).start();*/


        //刷新定位
        cityListRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityListLocationTv.setText("定位中...");
                mLocation();
            }
        });

        cityItemLocationRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = cityListLocationTv.getText().toString();
                if (str.equals("定位中...")) {
                    toast("未获取到定位地址");
                } else {
                    Intent data = new Intent();
                    data.putExtra("city", str);
                    setResult(100, data);
                    finish();
                }
            }
        });

    }

    //开启定位
    private void mLocation() {
        mLocClient = new LocationClient(mActivity);
        mLocClient.registerLocationListener(this);
        option = new LocationClientOption();
        option.setOpenGps(true);//打开GPS
        option.setCoorType("bd09ll"); //coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(10000);//单位毫秒，当<1000(1s)时，定时定位无效
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //   L.e("tag",bdLocation +"--------------定位结果---------------->");
        if (bdLocation != null) {
            stopLocation();
            cityListLocationTv.setText(bdLocation.getCity());
        }
    }

    /**
     * 关闭定位
     */
    public void stopLocation() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
    }

    private boolean verifyStoragePermissions() {
        if (!EasyPermissions.hasPermissions(this, PERMISSIONS_STORAGE)) {
            EasyPermissions.requestPermissions(this, "APP需要定位权限", 1, PERMISSIONS_STORAGE);
            return false;
        }
        return true;
    }

    //请求权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Utils.showMToast(mActivity, "请开启定位权限");
        finish();
    }

    @Override
    public void successData(List<String> data) {
        Log.i("tag",""+data.toString());
        List<WeChatBean> weChatBeans=new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            WeChatBean weChatBean=new WeChatBean();

            String sk = ContactLocaleUtils.getIntance().getSortKey(
                    data.get(i), ContactLocaleUtils.FullNameStyle.CHINESE);
            Log.i("tag","sk->"+sk);
            weChatBean.setPinyin(sk);
            weChatBean.setName(data.get(i));
            Log.i("tag",weChatBean.getPinyin()+";"+weChatBean.getName());
            weChatBeans.add(weChatBean);
        }
        list.addAll(weChatBeans);

        adapter.notifyDataSetChanged();

        /*superSideBar.setSourceDatas(list)// 设置数据
                .invalidate();*/
    }
}
