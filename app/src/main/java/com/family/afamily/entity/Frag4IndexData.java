package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/6.
 */

public class Frag4IndexData {
    private List<Map<String, String>> banner;
    private List<Map<String, String>> new_goods;
    private List<Map<String, String>> hot_goods;
    private List<Map<String, String>> study_goods_list;
    private List<Map<String, String>> play_goods_list;

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

    public List<Map<String, String>> getStudy_goods_list() {
        return study_goods_list;
    }

    public void setStudy_goods_list(List<Map<String, String>> study_goods_list) {
        this.study_goods_list = study_goods_list;
    }

    public List<Map<String, String>> getPlay_goods_list() {
        return play_goods_list;
    }

    public void setPlay_goods_list(List<Map<String, String>> play_goods_list) {
        this.play_goods_list = play_goods_list;
    }
}
