package com.family.afamily.entity;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class OrderListChildData {
    /**
     * "goods_attr_id": "",
     * "rec_id": "73",
     * "goods_id": "19",
     * "goods_name": "\u9752\u5c9b\u5564\u9152\uff08Tsingtao\uff09\u7ecf\u517811\u5ea6330ml*24\u542c \u6574\u7bb1\u88c5 \u4f20\u4e16\u7cbe\u917f \u53e3\u611f\u9187\u539a",
     * "goods_sn": "ECS000019",
     * "market_price": "96.00",
     * "goods_number": "1",
     * "goods_price": "80.00",
     * "goods_attr": "",
     * "is_real": "1",
     * "parent_id": "0",
     * "is_gift": "0",
     * "subtotal": "80.00",
     * "extension_code": "",
     * "goods_thumb": "http:\/\/yjlx.oss-cn-shenzhen.aliyuncs.com\/Uploads\/Picture\/2018-01-05\/5a4f45090183e.jpg"
     */
    private String goods_attr_id;
    private String rec_id;
    private String goods_id;
    private String goods_name;
    private String goods_sn;
    private String market_price;
    private String goods_number;
    private String goods_price;
    private String goods_attr;
    private String is_real;
    private String parent_id;
    private String is_gift;
    private String subtotal;
    private String extension_code;
    private String goods_thumb;
    private String audio;
    private String give_integral;
    private int evaluate = 5;
    private String evaluate_str;

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluate_str() {
        return evaluate_str;
    }

    public void setEvaluate_str(String evaluate_str) {
        this.evaluate_str = evaluate_str;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(String give_integral) {
        this.give_integral = give_integral;
    }

    public String getGoods_attr_id() {
        return goods_attr_id;
    }

    public void setGoods_attr_id(String goods_attr_id) {
        this.goods_attr_id = goods_attr_id;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getIs_gift() {
        return is_gift;
    }

    public void setIs_gift(String is_gift) {
        this.is_gift = is_gift;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getExtension_code() {
        return extension_code;
    }

    public void setExtension_code(String extension_code) {
        this.extension_code = extension_code;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }
}
