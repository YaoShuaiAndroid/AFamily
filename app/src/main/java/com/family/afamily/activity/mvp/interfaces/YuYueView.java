package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.YuYueDetailsData;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/26.
 */

public interface YuYueView extends BaseView {
    void successData(YuYueDetailsData data);

    void selectData(Map<String, String> data);

    void selectTime(String time);

    void submitSuccess();
}
