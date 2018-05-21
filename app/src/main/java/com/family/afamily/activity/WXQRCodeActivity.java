package com.family.afamily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.ConfigData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hp2015-7 on 2018/1/16.
 */

public class WXQRCodeActivity extends BaseActivity {
    @BindView(R.id.qr_code_img)
    ImageView qrCodeImg;
    private ConfigData configData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wxqr_code);
        configData = (ConfigData) Utils.load(FileUtile.configPath(this));
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
        setTitle(this, "微信公众号");
        if (configData != null) {
            Glide.with(mActivity).load(configData.getWx_code()).into(qrCodeImg);
        } else {
            getConfigData(this);
        }
    }

    /**
     * 获取配置信息
     */
    public void getConfigData(final Activity mActivity) {
        showProgress(3);
        Map<String, String> params = new HashMap<>();
        // params.put("token",token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CONFIG_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ConfigData>>() {
            @Override
            public void onError(Request request, Exception e) {
                hideProgress();
                toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<ConfigData> response) {
                hideProgress();
                if (response == null || response.getRet_code() != 1 || response.getData() == null) {
                    String msg = response == null ? "获取失败" : response.getMsg();
                    Utils.showMToast(mActivity, msg);
                } else {
                    String path = response.getData().getWx_code();
                    Glide.with(mActivity).load(path).into(qrCodeImg);
                    Utils.save(response.getData(), FileUtile.configPath(mActivity));
                }
            }
        }, params);
    }

}
