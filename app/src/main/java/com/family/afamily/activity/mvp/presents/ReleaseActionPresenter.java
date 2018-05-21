package com.family.afamily.activity.mvp.presents;

import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.family.afamily.activity.mvp.interfaces.ReleaseActionView;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by hp2015-7 on 2018/1/2.
 */

public class ReleaseActionPresenter extends BasePresent<ReleaseActionView> {

    public ReleaseActionPresenter(ReleaseActionView view) {
        attach(view);
    }


    public void initTimePicker1(final TextView textview1) {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11



        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DAY_OF_MONTH,startDate.get(Calendar.DAY_OF_MONTH)+1);
        Calendar endDate = Calendar.getInstance();
        int year_int = endDate.get(Calendar.YEAR);
        endDate.set(year_int+5, 0, 0,0,0);

        //时间选择器
        TimePickerView pvTime1 = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/

                textview1.setText(getTime(date));
            }
        }).setType(new boolean[]{true, true, true, true, true, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "时", "分", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
//                .setDividerColor(Color.RED)
//                .setTextColorCenter(Color.RED)//设置选中项的颜色
//                .setTextColorOut(Color.BLUE)//设置没有被选中项的颜色
                .setContentSize(21)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.5f)
              //  .setTextXOffset(-10, 0, 10, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime1.show();
    }


    public void showDateDialog(final TextView view) {

        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                view.setText(getTime(date));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();


//        DatePicker picker = new DatePicker(context);
//        Calendar selectedDate = Calendar.getInstance();
//        //picker.setLabel("年","月","日","时","分");
//        picker.setRangeStart(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH));
//        picker.setRangeEnd(selectedDate.get(Calendar.YEAR) + 1, selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH));
//        picker.setSelectedItem(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH));
//        picker.setDividerRatio(WheelView.DividerConfig.FILL);
//      //  picker.setOnDatePickListener(new On);
//        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
//            @Override
//            public void onDatePicked(String year, String month, String day) {
//                String selectTime = year + "-" + month + "-" + day;
//                view.setText(selectTime);
//            }
//        });

//        picker.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public void submitData(String token, String title, String obj, String number, String time, String address, String path, String decs, final TextView tv) {
        view.showProgress(2);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("title", title);
        params.put("who", obj);
        params.put("man_number", number);
        params.put("active_time", time);
        params.put("active_address", address);
        params.put("detail", decs);

        params = Utils.getParams(params);
        try {
            OkHttpClientManager.postAsyn(UrlUtils.ZJ_RELEASE_ACTION_URL, new OkHttpClientManager.ResultCallback<ResponseBean<Map<String, String>>>() {
                @Override
                public void onError(Request request, Exception e) {
                    view.hideProgress();
                    view.toast("发布活动失败");
                    tv.setEnabled(true);
                }

                @Override
                public void onResponse(ResponseBean<Map<String, String>> response) {
                    view.hideProgress();
                    if (response == null || response.getRet_code() != 1) {
                        String msg = response == null ? "发布活动失败" : response.getMsg();
                        view.toast(msg);
                        tv.setEnabled(true);
                    } else {
                        view.toast(response.getMsg());
                        view.submitSuccess();
                    }

                }
            }, new File(path), "picture", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
