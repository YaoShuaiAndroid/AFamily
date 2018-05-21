package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.IntegralDetailsView;
import com.family.afamily.entity.IntegralDetailsData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class IntegralDetailsPresenter extends BasePresent<IntegralDetailsView> {

    public IntegralDetailsPresenter(IntegralDetailsView view) {
        attach(view);
    }

    public void getData(String token, String goods_id, String type) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("goods_id", goods_id);
        params.put("type", type);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.EXCHANGE_DETAILS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<IntegralDetailsData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<IntegralDetailsData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.successData(response.getData());
                }
            }
        }, params);
    }

    public void showDialog(final Activity mActivity, String name, String img, String needInte, String mIntegral, final String goods_id, final int type) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exhange_layout, null);
        TextView dialog_exchange_name_tv = inflate.findViewById(R.id.dialog_exchange_name_tv);
        TextView dialog_exchange_k_integral_tv = inflate.findViewById(R.id.dialog_exchange_k_integral_tv);
        TextView dialog_exchange_integral_tv = inflate.findViewById(R.id.dialog_exchange_integral_tv);
        TextView dialog_submit_btn = inflate.findViewById(R.id.dialog_submit_btn);
        ImageView dialog_exchange_iv = inflate.findViewById(R.id.dialog_exchange_iv);

        dialog_exchange_name_tv.setText(name);
        dialog_exchange_k_integral_tv.setText(needInte);
        dialog_exchange_integral_tv.setText("剩余积分：" + mIntegral);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(mActivity).load(img).apply(options2).into(dialog_exchange_iv);

        dialog_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String token = (String) SPUtils.get(mActivity, "token", "");
                submitExehange(token, goods_id, type);
            }
        });

        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    public void submitExehange(String token, String goods_id, int type) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        if (type == 1) {
            params.put("goods_id", goods_id);
        } else {
            params.put("type_id", goods_id);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SUBMIT_EXCHANGE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "兑换失败失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess();
                }
            }
        }, params);
    }

}
