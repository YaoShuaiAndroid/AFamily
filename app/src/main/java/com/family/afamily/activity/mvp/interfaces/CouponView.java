package com.family.afamily.activity.mvp.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public interface CouponView extends BaseView {
    void successData(List<Map<String, String>> data);
}
