package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/29.
 */

public interface ZaoJaoListView extends BaseView {

    void successTypeData(List<Map<String, String>> data);

    void successData(BasePageBean<Map<String, String>> data);

}
