package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.entity.PdfModel;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface MyCapacityView extends BaseView {
    void successData(BasePageBean<PdfModel> data, int type);
}
