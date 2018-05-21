package com.family.afamily;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.family.afamily.entity.OrderListChildData;
import com.family.afamily.entity.WeChatBean;
import com.family.afamily.utils.L;
import com.family.afamily.utils.Log;
import com.family.afamily.wxapi.WXPayResultCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.util.LogUtils;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class MyApplication extends Application {
    public final String TAG = "HJR";
    public static MyApplication instance;
    private List<Map<String, String>> user_bonus;
    private List<OrderListChildData> listChildDatas;

    private String city = "深圳";
    private WXPayResultCallback wxPayResultCallback;

    public static Context getInstance() {
        return instance;
    }
    {
        PlatformConfig.setWeixin("wx44727cafeed440da", "3ed956b17582baca8f60f832bfe2f6b8");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        instance = this;
        Log.init(TAG, BuildConfig.LOG_DEBUG);
        //cityJson();
        UMShareAPI.get(this);
        Config.DEBUG = true;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Map<String, String>> getUser_bonus() {
        return user_bonus;
    }

    public void setUser_bonus(List<Map<String, String>> user_bonus) {
        this.user_bonus = user_bonus;
    }

    public List<OrderListChildData> getListChildDatas() {
        return listChildDatas;
    }

    public void setListChildDatas(List<OrderListChildData> listChildDatas) {
        this.listChildDatas = listChildDatas;
    }

    public List<WeChatBean> cityJson() {
        long t = System.currentTimeMillis();
        Gson mGson = new Gson();
        List<WeChatBean> m = null;
        try {
            m = mGson.fromJson(toString(getAssets().open("city.json"), "utf-8"), new TypeToken<List<WeChatBean>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.e("tag", (System.currentTimeMillis() - t) + "-------耗时------------------>");
        return m;
    }

    public static String toString(InputStream is, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return sb.toString();
    }

    public WXPayResultCallback getWxPayResultCallback() {
        return wxPayResultCallback;
    }

    public void setWxPayResultCallback(WXPayResultCallback wxPayResultCallback) {
        this.wxPayResultCallback = wxPayResultCallback;
    }
}
