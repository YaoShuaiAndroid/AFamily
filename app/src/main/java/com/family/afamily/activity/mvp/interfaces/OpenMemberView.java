package com.family.afamily.activity.mvp.interfaces;

import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/16.
 */

public interface OpenMemberView extends BaseView {

    void successData(Map<String, String> data);

    void submitSuccess(Map<String, String> data);
}
