package com.family.afamily.activity.mvp.presents;

import com.family.afamily.activity.mvp.interfaces.RegisterView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.Log;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class RegisterNextPresenter extends BasePresent<RegisterView> {
    public RegisterNextPresenter(RegisterView registerView) {
        attach(registerView);
    }


    /**
     * 提交登录
     *
     * @param mobile
     * @param tkrMobile
     * @param nick
     * @param pw
     * @param headFile
     */
    public void submitRegister(String mobile, String tkrMobile, String nick, String pw, File headFile) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("nick_name", nick);
        params.put("promoter_mobile", tkrMobile);
        params.put("mobile_phone", mobile);
        params.put("password", pw);

        Log.e("头像地址：" + headFile.getAbsolutePath());

        params = Utils.getParams(params);
        try {
            OkHttpClientManager.postAsyn(UrlUtils.REGISTER_NEXT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("注册失败");
                }

                @Override
                public void onResponse(ResponseBean<Map<String, String>> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "注册失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        Map<String, String> data = response.getData();
                        SPUtils.put(context, "user_id", data.get("user_id"));
                        view.registerSuccess(data.get("token"));
                    }

                }
            }, headFile, "avatar", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
