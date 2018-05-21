package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ConductClassListModel;
import com.family.afamily.entity.ConductClassModel;
import com.family.afamily.entity.ConductDetailModel;
import com.family.afamily.entity.InnateIntelligenceModel;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface ConductTestView extends BaseView {
    void successClassData(ConductClassListModel data);

    void successData(BasePageBean<InnateIntelligenceModel> data);
}
