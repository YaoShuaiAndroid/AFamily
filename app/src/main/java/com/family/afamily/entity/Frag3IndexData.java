package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class Frag3IndexData {
    private List<Map<String, String>> banner;
    private List<Map<String, String>> new_goods;
    private List<Map<String, String>> hot_goods;
    private List<Map<String, String>> best_goods;
    private Integer is_year_number;

    public Integer getIs_year_number() {
        return is_year_number == null ? 0 : is_year_number;
    }

    public void setIs_year_number(Integer is_year_number) {
        this.is_year_number = is_year_number;
    }

    public List<Map<String, String>> getBanner() {
        return banner;
    }

    public void setBanner(List<Map<String, String>> banner) {
        this.banner = banner;
    }

    public List<Map<String, String>> getNew_goods() {
        return new_goods;
    }

    public void setNew_goods(List<Map<String, String>> new_goods) {
        this.new_goods = new_goods;
    }

    public List<Map<String, String>> getHot_goods() {
        return hot_goods;
    }

    public void setHot_goods(List<Map<String, String>> hot_goods) {
        this.hot_goods = hot_goods;
    }

    public List<Map<String, String>> getBest_goods() {
        return best_goods;
    }

    public void setBest_goods(List<Map<String, String>> best_goods) {
        this.best_goods = best_goods;
    }
}
