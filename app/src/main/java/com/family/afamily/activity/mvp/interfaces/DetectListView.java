package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BodyModel;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface DetectListView extends BaseView {
    void successData(List<BodyModel> data);
}
