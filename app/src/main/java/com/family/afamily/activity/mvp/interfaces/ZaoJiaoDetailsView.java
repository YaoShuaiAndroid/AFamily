package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/29.
 */

public interface ZaoJiaoDetailsView extends BaseView {

    void successData(Map<String, String> data);

    void successCommentList(BasePageBean<Map<String, String>> data, int type);

    void submitCollectResult(int type);

    void successComment();

}
