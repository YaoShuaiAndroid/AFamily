package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class BabyDetailsList {
    private Integer totle_page;
    private Integer page;
    private List<ItemBabyData> list_data;

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

    public List<ItemBabyData> getList_data() {
        return list_data;
    }

    public void setList_data(List<ItemBabyData> list_data) {
        this.list_data = list_data;
    }
}
