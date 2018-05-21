package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.MasterHomeData;

import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public interface MasterView extends BaseView {

    void successData(MasterHomeData<Map<String, String>> data, int getType, boolean isOk);

    void followSuccess(int type);
}
