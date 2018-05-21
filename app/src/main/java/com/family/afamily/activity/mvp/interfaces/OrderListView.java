package com.family.afamily.activity.mvp.interfaces;


import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.OrderListData;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public interface OrderListView extends BaseView {

    void successData(BasePageBean<OrderListData> basePageBean);

    void submitSuccess(boolean isRefresh);
}
