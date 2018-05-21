package com.family.afamily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.entity.ConfigData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class StartActivity extends AppCompatActivity {
   // private ImageView start_bg_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_start);
       // start_bg_iv = (ImageView) findViewById(R.id.start_bg_iv);
      //  Glide.with(this).load(R.mipmap.start_bg).into(start_bg_iv);

        final String token = (String) SPUtils.get(StartActivity.this, "token", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(token)) {
                    boolean isShow = (boolean) SPUtils.get(StartActivity.this, "isShowGuide", true);
                    if (isShow) {
                        Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000);

        getConfigData(this);
    }


    /**
     * 获取配置信息
     */
    public void getConfigData(final Activity mActivity) {
        Map<String, String> params = new HashMap<>();
        // params.put("token",token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CONFIG_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ConfigData>>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(ResponseBean<ConfigData> response) {
                if (response == null || response.getRet_code() != 1 || response.getData() == null) {
                } else {
                    Utils.save(response.getData(), FileUtile.configPath(mActivity));
                }
            }
        }, params);
    }
}
