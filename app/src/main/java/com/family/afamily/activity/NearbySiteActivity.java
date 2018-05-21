package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BaseView;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 附近站点
 * Created by hp2015-7 on 2018/1/12.
 */

public class NearbySiteActivity extends BaseActivity implements BDLocationListener, OnGetGeoCoderResultListener, BaseView {
    @BindView(R.id.item_radio_btn)
    RadioButton itemRadioBtn;
    @BindView(R.id.item_nearby_rl)
    RelativeLayout itemNearbyRl;
    @BindView(R.id.nearby_list_rv)
    RecyclerView nearbyListRv;

    private LocationClient mLocClient;//定位类
    private LocationClientOption option;
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    private List<PoiInfo> list = new ArrayList<>();
    private CommonAdapter<PoiInfo> adapter;
    private int address_index;
    private String address;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nearby_site);
        address_index = getIntent().getIntExtra("position", -1);
        address = getIntent().getStringExtra("address");
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "所在位置");
        showProgress(3);
        mLocation();
        if (address_index == -1) {
            itemRadioBtn.setVisibility(View.VISIBLE);
        } else {
            itemRadioBtn.setVisibility(View.GONE);
        }


        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        itemNearbyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("address", "不显示位置");
                intent.putExtra("position", "-1");
                setResult(100, intent);
                finish();
            }
        });

        nearbyListRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, ContextCompat.getColor(mActivity, R.color.line_color));
        nearbyListRv.addItemDecoration(divider);
        adapter = new CommonAdapter<PoiInfo>(mActivity, R.layout.item_nearby_site_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final PoiInfo poiInfo, final int position) {
                holder.setText(R.id.item_nearby_name, poiInfo.name);
                RelativeLayout item_nearby_rl = holder.getView(R.id.item_nearby_rl);
                RadioButton item_radio_btn = holder.getView(R.id.item_radio_btn);

                if (address_index == position) {
                    if (poiInfo.name.equals(address)) {
                        item_radio_btn.setVisibility(View.VISIBLE);
                    } else {
                        item_radio_btn.setVisibility(View.GONE);
                    }
                } else {
                    item_radio_btn.setVisibility(View.GONE);
                }

                item_nearby_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("address", poiInfo.name);
                        intent.putExtra("position", position);
                        setResult(100, intent);
                        finish();
                    }
                });
            }
        };
        nearbyListRv.setAdapter(adapter);
    }

    @Override
    public BasePresent initPresenter() {
        return null;
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
            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            //poi检索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
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


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        //L.e("tag",geoCodeResult+"--------------ttt---------------->");
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        hideProgress();
        // L.e("tag",result+"--------------222---------------->");
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Utils.showMToast(mActivity, "抱歉，未能找到结果");
            return;
        }
        List<PoiInfo> mlist = result.getPoiList();
        if (mlist != null && mlist.size() > 0) {
            list.clear();
            list.addAll(mlist);
            adapter.notifyDataSetChanged();
        }
    }

}
