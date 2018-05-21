package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.InnateIntelligenceModel;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface PhysiqueView extends BaseView {
    void successData(BasePageBean<InnateIntelligenceModel> data,int type);
}
