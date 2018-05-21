package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/4.
 */

public class GoodsInfoData {
    private String goods_id;
    private String cat_id;
    private String goods_type;
    private String goods_name;
    private String market_price;
    private String shop_price;
    private String goods_desc;
    private String goods_brief;
    private String goods_thumb;
    private String give_integral;
    private String comments_number;
    private String zhekou;
    private String comment_count;
    private String audio;
    private Integer cart_count;
    private String audio_time;
    private String additional_desc;
    private Integer goods_be_collected;
    private List<String> picture;
    private List<Map<String, String>> comment_list;
    private List<GoodsAttr> goods_attr;
    private List<Map<String, String>> book_address_list;
    private Integer year_number_status;
    private Integer love_goods_status;
    private String love_number;

    public String getLove_number() {
        return love_number;
    }

    public void setLove_number(String love_number) {
        this.love_number = love_number;
    }

    public Integer getLove_goods_status() {
        return love_goods_status;
    }

    public void setLove_goods_status(Integer love_goods_status) {
        this.love_goods_status = love_goods_status;
    }

    public String getAdditional_desc() {
        return additional_desc;
    }

    public void setAdditional_desc(String additional_desc) {
        this.additional_desc = additional_desc;
    }

    public List<Map<String, String>> getBook_address_list() {
        return book_address_list;
    }

    public void setBook_address_list(List<Map<String, String>> book_address_list) {
        this.book_address_list = book_address_list;
    }

    public Integer getYear_number_status() {
        return year_number_status == null ? 0 : year_number_status;
    }

    public void setYear_number_status(Integer year_number_status) {
        this.year_number_status = year_number_status;
    }

    public List<GoodsAttr> getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(List<GoodsAttr> goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getAudio_time() {
        return audio_time;
    }

    public void setAudio_time(String audio_time) {
        this.audio_time = audio_time;
    }

    public Integer getGoods_be_collected() {
        return goods_be_collected;
    }

    public void setGoods_be_collected(Integer goods_be_collected) {
        this.goods_be_collected = goods_be_collected;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Integer getCart_count() {
        return cart_count;
    }

    public void setCart_count(Integer cart_count) {
        this.cart_count = cart_count;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getGoods_brief() {
        return goods_brief;
    }

    public void setGoods_brief(String goods_brief) {
        this.goods_brief = goods_brief;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(String give_integral) {
        this.give_integral = give_integral;
    }

    public String getComments_number() {
        return comments_number;
    }

    public void setComments_number(String comments_number) {
        this.comments_number = comments_number;
    }

    public String getZhekou() {
        return zhekou;
    }

    public void setZhekou(String zhekou) {
        this.zhekou = zhekou;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public List<Map<String, String>> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Map<String, String>> comment_list) {
        this.comment_list = comment_list;
    }
}
