package com.family.afamily.activity.mvp.presents;

import android.widget.TextView;

import com.family.afamily.activity.mvp.interfaces.AddBabyGrowUpView;
import com.family.afamily.entity.BabyChartData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by hp2015-7 on 2018/3/5.
 */

public class AddbabyGrowUpPresenter extends BasePresent<AddBabyGrowUpView> {
    public AddbabyGrowUpPresenter(AddBabyGrowUpView view){
        attach(view);
    }

    public void submitData(String token,String height,String weight,String head,String add_time){
        view.showProgress(3);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("height", height);
        params.put("weight", weight);
        params.put("head", head);
        params.put("add_time", add_time);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ADD_BABY_GROWUP_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("提交失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "获取失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.callbackData();
                }
            }
        }, params);
    }

    public void showDateDialog(final TextView view, int year, int month, int day) {
        DatePicker picker = new DatePicker(context);
        Calendar selectedDate = Calendar.getInstance();
        picker.setRangeStart(year,month,day);
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
