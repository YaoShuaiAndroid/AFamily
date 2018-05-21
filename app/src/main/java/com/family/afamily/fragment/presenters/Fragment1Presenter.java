package com.family.afamily.fragment.presenters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.family.afamily.activity.AllWebViewActivity;
import com.family.afamily.activity.IntegralActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.Frag1SignData;
import com.family.afamily.entity.HomeData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.SignData;
import com.family.afamily.fragment.interfaces.Fragment1View;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.GlideRoundTransform;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public class Fragment1Presenter extends BasePresent<Fragment1View> {
    private BaseDialog bset;
    private CommonAdapter<SignData> adapter;
    private TextView sign_count_tv;
    private TextView sign_ok_tv;
    private TextView sign_ts_tv;
    private List<SignData> list = new ArrayList<>();

    public Fragment1Presenter(Fragment1View view1) {
        attach(view1);
    }

    /**
     * 获取首页数据
     *
     * @param token
     */
    public void checkMember(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CHECK_MEMBER_OVER_URL, new OkHttpClientManager.ResultCallback<ResponseBean<HomeData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
            }

            @Override
            public void onResponse(ResponseBean<HomeData> response) {
                view.hideProgress();
                if (response != null && response.getRet_code() == 0) {
                    view.toast(response.getMsg());
                    view.checkMemberCallback();
                }
            }
        }, params);
    }

    /**
     * 获取签到数据
     *
     * @param token
     * @param red_tip
     * @param type
     */
    public void getSignData(String token, final TextView red_tip, final int type) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SIGN_DATA_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Frag1SignData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取签到数据失败");
            }

            @Override
            public void onResponse(ResponseBean<Frag1SignData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取签到数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    // view.toast(response.getMsg());
                    Frag1SignData data = response.getData();
                    if (data != null) {
                        if (data.getIs_check() != null && data.getIs_check() == 1) {
                            red_tip.setVisibility(View.INVISIBLE);
                        } else {
                            red_tip.setVisibility(View.VISIBLE);
                        }
                        List<SignData> list = Utils.getSignData();
                        List<String> dayList = data.getDay_check_list();
                        if (dayList != null && dayList.size() > 0) {
                            for (int i = 0; i < dayList.size(); i++) {
                                for (int j = 0; j < list.size(); j++) {
                                    SignData signData = list.get(j);
                                    // L.e("tag",dayList.get(i) +"-------->"+signData.getDayStr() +"----------->"+(dayList.get(i).equals(signData.getDayStr())));
                                    if (dayList.get(i).equals(signData.getDayStr())) {
                                        signData.setClick(false);
                                        signData.setCheck(true);
                                        list.set(j, signData);
                                    }
                                }
                            }
                            data.setDayList(list);
                        } else {
                            data.setDayList(list);
                        }
                        if (type == 1) {
                            showSignDialog(context, data, red_tip);
                        }
                        view.signDataSuccess(data);
                    } else {
                        view.toast("服务器数据异常");
                    }
                }
            }
        }, params);
    }

    /**
     * 获取首页数据
     *
     * @param token
     */
    public void getHomeData(String token) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            params.put("token", token);
        }
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.HOME_DATA_URL, new OkHttpClientManager.ResultCallback<ResponseBean<HomeData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<HomeData> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取数据失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    // view.toast(response.getMsg());
                    view.homeDataSuccess(response.getData());
                }
            }
        }, params);
    }

    public void submitLook(String token,String advertising_id){
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("advertising_id", advertising_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ADV_LOOK_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(ResponseBean<String> response) {

            }
        }, params);
    }

    /**
     * 显示签到弹窗
     *
     * @param mActivity
     * @param signData
     * @param red_tip
     */
    public void showSignDialog(final Activity mActivity, final Frag1SignData signData, final TextView red_tip) {
        if (bset == null) {
            bset = new BaseDialog(mActivity, R.layout.dialog_sign_layout) {
                @Override
                protected void getMView(View view, final Dialog dialog) {
                    ImageView sign_close_iv = view.findViewById(R.id.sign_close_iv);
                    RecyclerView sign_list_rl = view.findViewById(R.id.sign_list_rl);
                    sign_count_tv = view.findViewById(R.id.sign_count_tv);
                    sign_ok_tv = view.findViewById(R.id.sign_ok_tv);
                    sign_ts_tv = view.findViewById(R.id.sign_ts_tv);
                    TextView btn_integral = view.findViewById(R.id.btn_integral);

                    sign_count_tv.setText(signData.getCheck_day() + "");
                    if (signData.getIs_check() == 1) {
                        sign_ok_tv.setText("签到成功");
                    } else {
                        sign_ok_tv.setText("今日未签到");
                    }
                    sign_ts_tv.setText(signData.getRemind());

                    list.clear();
                    list.addAll(signData.getDayList());

                    btn_integral.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, IntegralActivity.class);
                            mActivity.startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    Calendar data = Calendar.getInstance();
                    final int td = data.get(Calendar.DAY_OF_MONTH);
                    GridLayoutManager mgr = new GridLayoutManager(mActivity, 7);
                    sign_list_rl.setLayoutManager(mgr);
                    adapter = new CommonAdapter<SignData>(mActivity, R.layout.item_sign_layout, list) {
                        @SuppressLint("ResourceType")
                        @Override
                        protected void convert(ViewHolder holder, final SignData signData, int position) {
                            final TextView item_sign_tv = holder.getView(R.id.item_sign_tv);


                            item_sign_tv.setText(signData.getDay() + "");
                            L.e("tag", signData.toString() + "-------");
                            if (signData.isCheck()) {
                                //选中
                                item_sign_tv.setEnabled(false);
                            } else {
                                //未选中
                                item_sign_tv.setEnabled(true);

                                if (td > signData.getDay()) {
                                    item_sign_tv.setBackgroundResource(R.drawable.circle_hs_bg);
                                    item_sign_tv.setTextColor(Color.parseColor("#ffffff"));
                                } else {
                                    item_sign_tv.setBackgroundResource(R.drawable.item_sign_bg);

                                    item_sign_tv.setTextColor(mActivity.getResources().getColorStateList(R.drawable.item_sign_text));


                                   // item_sign_tv.setTextColor(mActivity.getResources().getColorStateList(R.drawable.item_sign_text, null));
                                }

                            }
                            item_sign_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (signData.isClick()) {
                                        if (Utils.isConnected(mActivity)) {
                                            String token = (String) SPUtils.get(mActivity, "token", "");
                                            submitSign(token, red_tip);
                                        }
                                    }
                                }
                            });
                        }
                    };
                    sign_list_rl.setAdapter(adapter);

                    sign_close_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
//                            if(dialog.)
                            bset = null;
                            L.e("tag", dialog + "---------------->");
                        }
                    });

                    //设置dialog 边距
                    Window win = dialog.getWindow();
                    win.getDecorView().setPadding(Utils.dp2px(7), 0, Utils.dp2px(7), 0);
                    WindowManager.LayoutParams lp = win.getAttributes();
                    lp.width = WindowManager.LayoutParams.FILL_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    win.setAttributes(lp);

                }
            };
        } else {

            sign_count_tv.setText(signData.getCheck_day() + "");
            if (signData.getIs_check() == 1) {
                sign_ok_tv.setText("签到成功");
            } else {
                sign_ok_tv.setText("今日未签到");
            }
            sign_ts_tv.setText(signData.getRemind());
            list.clear();
            list.addAll(signData.getDayList());
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 提交签到
     *
     * @param token
     * @param red_tip
     */
    public void submitSign(final String token, final TextView red_tip) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.SUBMIT_SIGN_URL, new OkHttpClientManager.ResultCallback<ResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("签到失败");
            }

            @Override
            public void onResponse(ResponseBean response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "签到失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    getSignData(token, red_tip, 1);
                }
            }
        }, params);
    }

    /**
     * 领取优惠券
     *
     * @param token
     * @param id
     */
    public void collarCoupon(final Activity activity, final String token, String id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.GET_COUNPON_URL, new OkHttpClientManager.ResultCallback<ResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("领取失败");
            }

            @Override
            public void onResponse(ResponseBean response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "领取失败" : response.getMsg();
                    view.toast(msg);
                } else {
//                    view.toast(response.getMsg());
                    // Utils.myToas(activity,"恭喜您，领取成功！",1);
                    showCollarSuccess(activity);
                    getHomeData(token);
                }
            }
        }, params);
    }

    private void showCollarSuccess(Activity activity) {
        new BaseDialog(activity, R.layout.toas_coupon_ok) {
            @Override
            protected void getMView(View view, Dialog dialog) {
            }
        };
    }

    public void showAdvertDialog(final Activity mActivity,final Map<String,String> data){
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_home_advert_layout, null);
        ImageView home_adv_img = inflate.findViewById(R.id.home_adv_img);
        ImageView home_adv_close_iv = inflate.findViewById(R.id.home_adv_close_iv);

        GlideRoundTransform transform = new GlideRoundTransform(mActivity,3);
        RequestOptions options = new RequestOptions();
        options.transform(transform);
        Glide.with(mActivity).load(data.get("photo")).apply(options).into(home_adv_img);
        home_adv_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        home_adv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AllWebViewActivity.class);
                dialog.dismiss();
                intent.putExtra("title", "");
                intent.putExtra("link", data.get("url"));
                mActivity.startActivity(intent);
            }
        });

        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }
}
