package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BabyDetailsData;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public interface BabyDetailsView extends BaseView {

    void successData(BabyDetailsData detailsData, int getType, boolean isOk);
}
