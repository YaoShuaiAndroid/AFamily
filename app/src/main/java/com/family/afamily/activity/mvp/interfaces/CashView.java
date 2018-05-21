package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.CashData;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public interface CashView extends BaseView {

    void successData(CashData cashData, int getType, boolean isOK);
}
