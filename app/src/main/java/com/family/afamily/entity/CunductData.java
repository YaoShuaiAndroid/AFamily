package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/4/28.
 */

public class CunductData {
    private List<ConductClassModel> top_type;
    private List<InnateIntelligenceModel> data_list;
    private List<ConductClassModel> type_next;

    public List<ConductClassModel> getTop_type() {
        return top_type;
    }

    public void setTop_type(List<ConductClassModel> top_type) {
        this.top_type = top_type;
    }

    public List<InnateIntelligenceModel> getData_list() {
        return data_list;
    }

    public void setData_list(List<InnateIntelligenceModel> data_list) {
        this.data_list = data_list;
    }

    public List<ConductClassModel> getType_next() {
        return type_next;
    }

    public void setType_next(List<ConductClassModel> type_next) {
        this.type_next = type_next;
    }
}
