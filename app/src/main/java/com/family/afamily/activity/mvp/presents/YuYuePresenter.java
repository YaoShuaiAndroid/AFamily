package com.family.afamily.activity.mvp.presents;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.interfaces.YuYueView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.entity.YuYueDetailsData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/26.
 */

public class YuYuePresenter extends BasePresent<YuYueView> {
    private CommonAdapter<String> strAdapter;

    public YuYuePresenter(YuYueView view) {
        attach(view);
    }

    public void getData(String token, String city) {
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("city", city);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.POOL_LIST_URL, new OkHttpClientManager.ResultCallback<ResponseBean<YuYueDetailsData>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<YuYueDetailsData> response) {
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

    public void showDiaog(Activity mActivity, String city, List<Map<String, String>> list) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_yyue_layout, null);
        View dialog_yyue_main_rl = inflate.findViewById(R.id.dialog_yyue_main_rl);
        TextView dialog_title_tv = inflate.findViewById(R.id.dialog_title_tv);
        TextView dialog_cancel_tv = inflate.findViewById(R.id.dialog_cancel_tv);
        RecyclerView dialog_list_rv = inflate.findViewById(R.id.dialog_list_rv);
        dialog_title_tv.setText(city);

        dialog_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        CommonAdapter<Map<String, String>> adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_yyue_dialog_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> stringStringMap, int position) {
                holder.setText(R.id.item_yyue_dialog_name, stringStringMap.get("pool_name"));

                holder.setOnClickListener(R.id.item_yyue_dialog_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        view.selectData(stringStringMap);
                    }
                });
            }
        };
        dialog_list_rv.setAdapter(adapter);
        dialog.setContentView(inflate);

        dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_yyue_main_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    public void showDateDialog(final Activity mActivity, final List<Map<String, String>> list, final List<String> dataList) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_yyue_date, null);
        RecyclerView dialog_yyue_list_rv = inflate.findViewById(R.id.dialog_yyue_list_rv);
        View dialog_yyue_root_rl = inflate.findViewById(R.id.dialog_yyue_root_rl);

        //dialog_yyue_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dialog_yyue_list_rv.setLayoutManager(linearLayoutManager);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final long nowTime = System.currentTimeMillis();
        CommonAdapter<Map<String, String>> adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_yyue_date_dialog, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> stringStringMap, final int position) {
                holder.setText(R.id.item_day_tv, stringStringMap.get("day"));
                holder.setText(R.id.item_week_tv, stringStringMap.get("week"));
                RecyclerView item_child_list_rv = holder.getView(R.id.item_child_list_rv);
                item_child_list_rv.setLayoutManager(new LinearLayoutManager(mActivity));
                strAdapter = new CommonAdapter<String>(mActivity, R.layout.item_yyue_child_dialog, dataList) {
                    @Override
                    protected void convert(ViewHolder holder, final String s, int p) {
                        TextView item_child_day_tv = holder.getView(R.id.item_child_day_tv);
                        holder.setText(R.id.item_child_day_tv, s);

                        if (position == 0) {
                            String str = stringStringMap.get("date") + " " + s;
                            // format.format(str);
                            try {
                                long time = format.parse(str).getTime();
                                if (nowTime > time) {
                                    item_child_day_tv.setEnabled(false);
                                } else {
                                    item_child_day_tv.setEnabled(true);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            item_child_day_tv.setEnabled(true);
                        }


                        item_child_day_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                view.selectTime(stringStringMap.get("date") + " " + s);
                            }
                        });

                    }
                };
                item_child_list_rv.setAdapter(strAdapter);
            }
        };
        dialog_yyue_list_rv.setAdapter(adapter);

        dialog_yyue_root_rl.setOnClickListener(new View.OnClickListener() {
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

    public void showYuYueDialog(final Activity mActivity, final String token, String k_integral, String h_integral, String b_integral,
                                final String time, final String id,final String store_id) {
        final Dialog dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_submit_layout, null);
        ImageView dialog_close_iv = inflate.findViewById(R.id.dialog_close_iv);
        TextView dialog_ks_integral_tv = inflate.findViewById(R.id.dialog_ks_integral_tv);
        TextView dialog_have_integral_tv = inflate.findViewById(R.id.dialog_have_integral_tv);
        TextView dialog_back_integral = inflate.findViewById(R.id.dialog_back_integral);
        TextView dialog_submit_tv = inflate.findViewById(R.id.dialog_submit_tv);

        dialog_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_ks_integral_tv.setText(k_integral);

        dialog_have_integral_tv.setText("剩余" + h_integral + "积分");
        dialog_back_integral.setText("准时消费后可获得" + b_integral + "积分");

        dialog_submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                submitData(token, id, time,store_id);
            }
        });

        dialog.setContentView(inflate);

        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    public void submitData(String token, String id, String time,String store_id) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("start_time", time);
        params.put("store_id", store_id);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.PUT_APPOINTMENT_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交数据失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交预约失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.submitSuccess();
                }
            }
        }, params);
    }

}
