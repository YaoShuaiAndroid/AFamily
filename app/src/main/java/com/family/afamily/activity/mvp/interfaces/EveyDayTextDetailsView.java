package com.family.afamily.activity.mvp.interfaces;


import java.util.Map;


/**
 * Created by hp2015-7 on 2017/12/21.
 */

public interface EveyDayTextDetailsView extends BaseView {
    void dataCallback(Map<String, Object> dataBean);

    void submitCollectResult(int type);
}
