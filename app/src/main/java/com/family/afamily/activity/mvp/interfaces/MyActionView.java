package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public interface MyActionView extends BaseView {

    void successData(BasePageBean pageBean);

    void submitSuccess();
}
