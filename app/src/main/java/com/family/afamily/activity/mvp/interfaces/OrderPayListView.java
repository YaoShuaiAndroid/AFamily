package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.OrderPayData;

import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public interface OrderPayListView extends BaseView {

    void successData(OrderPayData payData);

    void submitSuccess(Map<String, String> data);
}
