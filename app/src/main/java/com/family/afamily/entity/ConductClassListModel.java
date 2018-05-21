package com.family.afamily.entity;

import java.util.List;

/**
 * Created by bt on 2018/4/25.
 */

public class ConductClassListModel {
    List<ConductClassModel> type_top;

    List<ConductClassModel> type_next;

    List<InnateIntelligenceModel> data_list;

    public List<ConductClassModel> getType_top() {
        return type_top;
    }

    public void setType_top(List<ConductClassModel> type_top) {
        this.type_top = type_top;
    }

    public List<ConductClassModel> getType_next() {
        return type_next;
    }

    public void setType_next(List<ConductClassModel> type_next) {
        this.type_next = type_next;
    }

    public List<InnateIntelligenceModel> getData_list() {
        return data_list;
    }

    public void setData_list(List<InnateIntelligenceModel> data_list) {
        this.data_list = data_list;
    }
}
