package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class AddressCityActivity extends BaseActivity {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.address_province_tv)
    TextView addressProvinceTv;
    @BindView(R.id.address_city_tv)
    TextView addressCityTv;
    @BindView(R.id.address_area_tv)
    TextView addressAreaTv;
    @BindView(R.id.address_list_rv)
    RecyclerView addressListRv;
    private List<Map<String, String>> mlist = new ArrayList<>();
    private List<Map<String, String>> list = new ArrayList<>();
    private CommonAdapter<Map<String, String>> adapter;

    private int type = 1;
    private String province_id, city_id, area_id;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_city);
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }


    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "选择地址");
        baseTitleRightTv.setText("保存");
        getDataList("1");

        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = addressProvinceTv.getText().toString();
                String city = addressCityTv.getText().toString();
                String aerea = addressAreaTv.getText().toString();
                if (TextUtils.isEmpty(province)) {
                    toast("请选择省份");
                } else if (TextUtils.isEmpty(city)) {
                    toast("请选择城市");
                } else if (TextUtils.isEmpty(aerea)) {
                    toast("请选择区域");
                } else {
                    String address = province + city + aerea;
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    intent.putExtra("province_id", province_id);
                    intent.putExtra("city_id", city_id);
                    intent.putExtra("area_id", area_id);
                    setResult(100, intent);
                    finish();
                }
            }
        });


        addressProvinceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                if (mlist != null) {
                    list.clear();
                    list.addAll(mlist);
                    adapter.notifyDataSetChanged();
                }
                addressProvinceTv.setText("");
                addressCityTv.setVisibility(View.GONE);
                addressAreaTv.setVisibility(View.GONE);

//                String str = (String) addressProvinceTv.getTag();
//                if(!TextUtils.isEmpty(str)){
//                    getDataList(str);
//                }
                addressCityTv.setText("");
                addressAreaTv.setText("");
            }
        });

        addressCityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                addressCityTv.setText("");
                addressAreaTv.setVisibility(View.GONE);
                addressAreaTv.setText("");
                list.clear();
                adapter.notifyDataSetChanged();
                String str = (String) addressProvinceTv.getTag();
                if (!TextUtils.isEmpty(str)) {
                    getDataList(str);
                }
            }
        });


        addressListRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        addressListRv.addItemDecoration(divider);
        adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_city_text_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> stringStringMap, int position) {
                TextView item_text_tv = holder.getView(R.id.item_text_tv);

                holder.setText(R.id.item_text_tv, stringStringMap.get("region_name"));

                item_text_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 1) {
                            type = 2;
                            province_id = stringStringMap.get("region_id");
                            addressProvinceTv.setText(stringStringMap.get("region_name"));
                            addressProvinceTv.setTag(stringStringMap.get("region_id"));
                            addressCityTv.setVisibility(View.VISIBLE);
                            getDataList(stringStringMap.get("region_id"));
                            list.clear();
                            adapter.notifyDataSetChanged();
                        } else if (type == 2) {
                            type = 3;
                            city_id = stringStringMap.get("region_id");
                            addressCityTv.setTag(stringStringMap.get("region_id"));
                            addressCityTv.setText(stringStringMap.get("region_name"));
                            addressAreaTv.setVisibility(View.VISIBLE);
                            getDataList(stringStringMap.get("region_id"));
                        } else {
                            area_id = stringStringMap.get("region_id");
                            addressAreaTv.setText(stringStringMap.get("region_name"));
                        }
                    }
                });
            }
        };
        addressListRv.setAdapter(adapter);
    }


    public void getDataList(final String region_id) {
        showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("region_id", region_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.GET_REGIONS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<List<Map<String, String>>>>() {
            @Override
            public void onError(Request request, Exception e) {
                hideProgress();
                toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<List<Map<String, String>>> response) {
                hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    toast(msg);
                } else {
                    List<Map<String, String>> mapList = response.getData();
                    if (mapList != null && mapList.size() > 0) {
                        if (region_id.equals("1")) {
                            mlist = mapList;
                        }
                        list.clear();
                        list.addAll(mapList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, params);
    }

}
