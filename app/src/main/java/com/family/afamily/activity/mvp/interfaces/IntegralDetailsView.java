package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.IntegralDetailsData;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public interface IntegralDetailsView extends BaseView {

    void successData(IntegralDetailsData detailsData);

    void submitSuccess();
}
