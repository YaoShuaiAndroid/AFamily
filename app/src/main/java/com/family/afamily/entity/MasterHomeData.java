package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class MasterHomeData<T> {
    private Integer totle_page;
    private Integer page;
    private List<T> list_data;
    private Map<String, String> user_info;

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

    public List<T> getList_data() {
        return list_data;
    }

    public void setList_data(List<T> list_data) {
        this.list_data = list_data;
    }

    public Map<String, String> getUser_info() {
        return user_info;
    }

    public void setUser_info(Map<String, String> user_info) {
        this.user_info = user_info;
    }
}
