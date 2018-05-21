package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class CashData {
    private Integer totle_page;
    private Integer page;
    private Double user_money;
    private Integer bind_bank;
    private Integer audit_status;

    public Integer getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(Integer audit_status) {
        this.audit_status = audit_status;
    }

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

    public Double getUser_money() {
        return user_money;
    }

    public void setUser_money(Double user_money) {
        this.user_money = user_money;
    }

    public Integer getBind_bank() {
        return bind_bank;
    }

    public void setBind_bank(Integer bind_bank) {
        this.bind_bank = bind_bank;
    }

    public List<Map<String, String>> getList_data() {
        return list_data;
    }

    public void setList_data(List<Map<String, String>> list_data) {
        this.list_data = list_data;
    }
}
