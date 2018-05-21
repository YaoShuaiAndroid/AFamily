package com.family.afamily.activity.mvp.interfaces;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface ActionDetailsView extends BaseView {
    void successData(Map<String, String> data);

    void submitSuccess(int type);
}
