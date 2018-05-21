package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BabyChartData;

/**
 * Created by hp2015-7 on 2018/3/6.
 */

public interface BabyChartView extends BaseView {
    void successData(BabyChartData chartData);
}
