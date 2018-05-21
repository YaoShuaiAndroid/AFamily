package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.ToExtensionView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.ToExtensionData;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/17.
 */

public class ToExtensionPresenter extends BasePresent<ToExtensionView> {

    public ToExtensionPresenter(ToExtensionView view) {
        attach(view);
    }

    public void getData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.TO_GENERALIZE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ToExtensionData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<ToExtensionData> response) {
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


    public void showShare(final Activity mActivity, final Map<String, String> data) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_share_layout, null);
        LinearLayout wx_friend_ll = inflate.findViewById(R.id.wx_friend_ll);
        LinearLayout wx_pyj_ll = inflate.findViewById(R.id.wx_pyj_ll);
        LinearLayout wx_qq_ll = inflate.findViewById(R.id.wx_qq_ll);
        LinearLayout wx_qq_z_ll = inflate.findViewById(R.id.wx_qq_z_ll);
        LinearLayout wx_sina_ll = inflate.findViewById(R.id.wx_sina_ll);
        TextView cancel_btn = inflate.findViewById(R.id.cancel_btn);
        UMImage image = new UMImage(mActivity, data.get("share_photo"));//网络图片
        final UMWeb web = new UMWeb(data.get("url"));
        web.setTitle(data.get("title"));//标题
        web.setThumb(image);  //缩略图
        web.setDescription(data.get("share_intro"));//描述
        wx_friend_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                view.toast("正在分享，请稍后....");
                new ShareAction(mActivity)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        wx_pyj_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                view.toast("正在分享，请稍后....");
                new ShareAction(mActivity)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        wx_qq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                view.toast("正在分享，请稍后....");
                new ShareAction(mActivity)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        wx_qq_z_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                view.toast("正在分享，请稍后....");
                new ShareAction(mActivity)
                        .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        wx_sina_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                view.toast("正在分享，请稍后....");
                new ShareAction(mActivity)
                        .setPlatform(SHARE_MEDIA.SINA)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(inflate);

        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(context, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, "取消了", Toast.LENGTH_LONG).show();

        }
    };
}
