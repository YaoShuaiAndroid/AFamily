package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/20.
 */

public class HomeData {
    private List<Map<String, String>> banner;
    private List<Map<String, String>> article;
    private Map<String, String> appointment_info;
    private List<Map<String, String>> pool_info;
    private List<Map<String, String>> sand_info;
    private List<Map<String, String>> discount_list;
    private Map<String,String> advertising;

    public Map<String, String> getAdvertising() {
        return advertising;
    }

    public void setAdvertising(Map<String, String> advertising) {
        this.advertising = advertising;
    }

    public List<Map<String, String>> getBanner() {
        return banner;
    }

    public void setBanner(List<Map<String, String>> banner) {
        this.banner = banner;
    }

    public List<Map<String, String>> getArticle() {
        return article;
    }

    public void setArticle(List<Map<String, String>> article) {
        this.article = article;
    }

    public Map<String, String> getAppointment_info() {
        return appointment_info;
    }

    public void setAppointment_info(Map<String, String> appointment_info) {
        this.appointment_info = appointment_info;
    }

    public List<Map<String, String>> getPool_info() {
        return pool_info;
    }

    public void setPool_info(List<Map<String, String>> pool_info) {
        this.pool_info = pool_info;
    }

    public List<Map<String, String>> getSand_info() {
        return sand_info;
    }

    public void setSand_info(List<Map<String, String>> sand_info) {
        this.sand_info = sand_info;
    }

    public List<Map<String, String>> getDiscount_list() {
        return discount_list;
    }

    public void setDiscount_list(List<Map<String, String>> discount_list) {
        this.discount_list = discount_list;
    }
}
