package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.GoodsInfoData;

/**
 * Created by hp2015-7 on 2018/3/2.
 */

public interface ProductDetailsView extends BaseView {
    void successData(GoodsInfoData goodsInfoData);
    void submitCollectResult(int type);
}
