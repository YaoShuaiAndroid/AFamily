package com.family.afamily.activity.mvp.interfaces;

import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public interface FeedBackView extends BaseView {

    void submitSuccess();

    void getData(Map<String, String> data);
}
