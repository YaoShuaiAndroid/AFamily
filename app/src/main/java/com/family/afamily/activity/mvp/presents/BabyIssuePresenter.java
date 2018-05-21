package com.family.afamily.activity.mvp.presents;

import android.widget.TextView;

import com.family.afamily.activity.mvp.interfaces.BabyIssueView;
import com.family.afamily.entity.ResponseBean;
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
 * Created by hp2015-7 on 2018/1/12.
 */

public class BabyIssuePresenter extends BasePresent<BabyIssueView> {
    public BabyIssuePresenter(BabyIssueView view) {
        attach(view);
    }

    /**
     * 发布视频
     *
     * @param token
     * @param child_id
     * @param content
     * @param video
     * @param create_time
     * @param address
     */
    public void submitVideo(String token, String child_id, String content, String video, String create_time, String address) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("child_id", child_id);
        params.put("content", content);
        params.put("video", video);
        params.put("create_time", create_time);
        params.put("address", address);
        params.put("type", "2");
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.BABY_ISSUE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideProgress();
                view.toast("发布失败");
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                view.hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    view.toast(msg);
                } else {
                    view.toast(response.getMsg());
                    view.successData();
                }
            }
        }, params);
    }

    /**
     * 发布图片
     *
     * @param token
     * @param child_id
     * @param content
     * @param create_time
     * @param address
     * @param files
     * @param keys
     */
    public void submitPic(String token, String child_id, String content, String create_time, String address, File[] files, String[] keys) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("child_id", child_id);
        params.put("content", content);
        params.put("create_time", create_time);
        params.put("address", address);
        params.put("type", "1");
        params = Utils.getParams(params);
        try {
            OkHttpClientManager.postAsyn(UrlUtils.BABY_ISSUE_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("发布失败");
                }

                @Override
                public void onResponse(ResponseBean<String> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "发布失败" : response.getMsg();
                        view.toast(msg);
                    } else {
                        view.toast(response.getMsg());
                        view.successData();
                    }
                }
            }, files, keys, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDateDialog(final TextView view) {
        DatePicker picker = new DatePicker(context);
        Calendar selectedDate = Calendar.getInstance();
        picker.setRangeStart(2000, 1, 1);
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
