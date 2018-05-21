package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponsePageBean;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public interface MyYYueView extends BaseView {
    void successData(BasePageBean<Map<String, String>> data,int getType);
    void updateData();
}
