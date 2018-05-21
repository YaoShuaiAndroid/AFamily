package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class IntegralData {
    private List<Map<String, String>> banner;
    private List<Map<String, String>> goods_list;
    private String pay_points;

    public List<Map<String, String>> getBanner() {
        return banner;
    }

    public void setBanner(List<Map<String, String>> banner) {
        this.banner = banner;
    }

    public List<Map<String, String>> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Map<String, String>> goods_list) {
        this.goods_list = goods_list;
    }

    public String getPay_points() {
        return pay_points;
    }

    public void setPay_points(String pay_points) {
        this.pay_points = pay_points;
    }
}
