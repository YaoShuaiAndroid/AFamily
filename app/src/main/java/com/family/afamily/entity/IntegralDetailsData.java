package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/9.
 */

public class IntegralDetailsData {

    /**
     * "type_id": "12",
     * "type_money": "66.00",
     * "min_goods_amount": "100.00",
     * "type_name": "\u5e74\u5e95\u6253\u6298\u5238",
     * "goods_thumb": "http:\/\/yjlx.oss-cn-shenzhen.aliyuncs.com\/Uploads\/Picture\/2018-01-03\/5a4c7aaec3550.jpg",
     * "integral_count": "200",
     * "pay_points": "5043"
     */
    private String type_id;
    private String type_money;
    private String min_goods_amount;
    private String type_name;
    private String integral_count;
    private String goods_name;
    private String goods_thumb;
    private String shop_price;
    private String goods_number;
    private String goods_brief;
    private String exchange_integral;
    private String is_exchange;
    private String pay_points;
    private String goods_id;
    private List<String> picture;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_money() {
        return type_money;
    }

    public void setType_money(String type_money) {
        this.type_money = type_money;
    }

    public String getMin_goods_amount() {
        return min_goods_amount;
    }

    public void setMin_goods_amount(String min_goods_amount) {
        this.min_goods_amount = min_goods_amount;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getIntegral_count() {
        return integral_count;
    }

    public void setIntegral_count(String integral_count) {
        this.integral_count = integral_count;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_brief() {
        return goods_brief;
    }

    public void setGoods_brief(String goods_brief) {
        this.goods_brief = goods_brief;
    }

    public String getExchange_integral() {
        return exchange_integral;
    }

    public void setExchange_integral(String exchange_integral) {
        this.exchange_integral = exchange_integral;
    }

    public String getIs_exchange() {
        return is_exchange;
    }

    public void setIs_exchange(String is_exchange) {
        this.is_exchange = is_exchange;
    }

    public String getPay_points() {
        return pay_points;
    }

    public void setPay_points(String pay_points) {
        this.pay_points = pay_points;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }
}
