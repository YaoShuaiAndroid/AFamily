package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class OrderPayData {
    private Map<String, String> user_info;
    private List<Map<String, String>> user_bonus;
    private Map<String, String> user_address;
    private List<ShoppingCarList> goods_list;
    private Map<String, String> total;

    public Map<String, String> getUser_info() {
        return user_info;
    }

    public void setUser_info(Map<String, String> user_info) {
        this.user_info = user_info;
    }

    public List<Map<String, String>> getUser_bonus() {
        return user_bonus;
    }

    public void setUser_bonus(List<Map<String, String>> user_bonus) {
        this.user_bonus = user_bonus;
    }

    public Map<String, String> getUser_address() {
        return user_address;
    }

    public void setUser_address(Map<String, String> user_address) {
        this.user_address = user_address;
    }

    public List<ShoppingCarList> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<ShoppingCarList> goods_list) {
        this.goods_list = goods_list;
    }

    public Map<String, String> getTotal() {
        return total;
    }

    public void setTotal(Map<String, String> total) {
        this.total = total;
    }
}
