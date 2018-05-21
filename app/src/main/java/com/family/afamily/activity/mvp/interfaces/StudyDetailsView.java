package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.GoodsInfoData;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public interface StudyDetailsView extends BaseView {

    void successData(GoodsInfoData goodsInfoData);

    void successData(BasePageBean pageBean);

    void submitCollectResult(int type);

}
