package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public interface MyVideoView extends BaseView {

    void successData(BasePageBean pageBean);

    void delSuccess();

    void updateData();

}
