package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/5.
 */

public class ShoppingCarData {
    private List<ShoppingCarList> goods_list;
    private Map<String, String> total;

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
