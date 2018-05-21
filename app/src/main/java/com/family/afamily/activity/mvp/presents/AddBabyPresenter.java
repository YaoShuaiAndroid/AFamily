package com.family.afamily.activity.mvp.presents;

import android.text.TextUtils;
import android.widget.TextView;

import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.L;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class AddBabyPresenter extends BasePresent<SubmitSuccessView> {

    public AddBabyPresenter(SubmitSuccessView view) {
        attach(view);
    }

    /**
     * 添加宝宝
     *
     * @param token
     * @param nickname
     * @param birthday
     * @param file
     */
    public void submitData(String token, String id, int type, String nickname, String birthday, String file) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("token", token);
        params.put("birthday", birthday);
        L.e("Tag", id + "---------------->" + file);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(file)) {
            params.put("type", "1");
        }
        params.put("id", id);
        params = Utils.getParams(params);
        //不修改图片
        if (!TextUtils.isEmpty(id) && type == 1) {
            OkHttpClientManager.postAsyn(UrlUtils.ADD_CHILD_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("提交失败");
                }

                @Override
                public void onResponse(ResponseBean<String> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "提交失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        view.submitSuccess(null);
                    }
                }
            }, params);
        } else {
            try {
                OkHttpClientManager.postAsyn(UrlUtils.ADD_CHILD_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        view.hideProgress();
                        view.toast("提交失败");
                    }

                    @Override
                    public void onResponse(ResponseBean<String> response) {
                        view.hideProgress();
                        if (response == null || response.getRet_code() != 1) {
                            String msg = response == null ? "添加失败" : response.getMsg();
                            view.toast(msg);
                        } else {
                            view.toast(response.getMsg());
                            view.submitSuccess(null);
                        }

                    }
                }, new File(file), "avatar", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDateDialog(final TextView view) {
        DatePicker picker = new DatePicker(context);
        Calendar selectedDate = Calendar.getInstance();
        picker.setRangeStart(1990, 1, 1);
        picker.setRangeEnd(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH));
        picker.setSelectedItem(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH));
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                // Utils.showMToast(mActivity,year + "-" + month + "-" + day);
                String selectTime = year + "-" + month + "-" + day;
                view.setText(selectTime);

            }
        });
        picker.show();
    }
}
