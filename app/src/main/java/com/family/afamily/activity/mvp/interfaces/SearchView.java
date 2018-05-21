package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.entity.SearchData;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/15.
 */

public interface SearchView extends BaseView {

    void successData(SearchData data);

    void searchSuccess(BasePageBean<Map<String, String>> data, int getType);
}
