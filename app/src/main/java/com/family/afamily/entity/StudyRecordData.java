package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class StudyRecordData {
    private String study_day;
    private String rank;
    private String study_count;
    private Integer totle_page;
    private Integer page;
    private List<Map<String, String>> list_data;

    public String getStudy_day() {
        return study_day;
    }

    public void setStudy_day(String study_day) {
        this.study_day = study_day;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getStudy_count() {
        return study_count;
    }

    public void setStudy_count(String study_count) {
        this.study_count = study_count;
    }

    public Integer getTotle_page() {
        return totle_page;
    }

    public void setTotle_page(Integer totle_page) {
        this.totle_page = totle_page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Map<String, String>> getList_data() {
        return list_data;
    }

    public void setList_data(List<Map<String, String>> list_data) {
        this.list_data = list_data;
    }
}
