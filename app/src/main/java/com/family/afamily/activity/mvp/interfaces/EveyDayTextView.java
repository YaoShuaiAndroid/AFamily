package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;

import java.util.Map;


/**
 * Created by hp2015-7 on 2017/12/21.
 */

public interface EveyDayTextView extends BaseView {
    void dataCallback(BasePageBean<Map<String, Object>> dataBean);
}
