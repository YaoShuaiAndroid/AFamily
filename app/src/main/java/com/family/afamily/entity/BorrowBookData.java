package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class BorrowBookData {
    private Integer totle_page;
    private Integer page;
    private Map<String, String> unreturn;
    private List<Map<String, String>> list_data;

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

    public Map<String, String> getUnreturn() {
        return unreturn;
    }

    public void setUnreturn(Map<String, String> unreturn) {
        this.unreturn = unreturn;
    }

    public List<Map<String, String>> getList_data() {
        return list_data;
    }

    public void setList_data(List<Map<String, String>> list_data) {
        this.list_data = list_data;
    }
}
