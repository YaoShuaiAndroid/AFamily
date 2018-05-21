package com.family.afamily.activity.mvp.interfaces;

import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/2.
 */

public interface EnrollActionView extends BaseView {
    void submitSuccess(Map<String,String> data);
}
