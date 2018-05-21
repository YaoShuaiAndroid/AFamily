package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class OrderListData {
    private Integer order_id;
    private String order_sn;
    private String order_time;
    private String order_status;
    private String total_fee;
    private Integer exists;
    private String wuliu_url;
    private String total_fee_new;

    public String getTotal_fee_new() {
        return total_fee_new;
    }

    public void setTotal_fee_new(String total_fee_new) {
        this.total_fee_new = total_fee_new;
    }

    private List<OrderListChildData> goods_list;

    public String getWuliu_url() {
        return wuliu_url;
    }

    public void setWuliu_url(String wuliu_url) {
        this.wuliu_url = wuliu_url;
    }


    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getExists() {
        return exists;
    }

    public void setExists(Integer exists) {
        this.exists = exists;
    }

    public List<OrderListChildData> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<OrderListChildData> goods_list) {
        this.goods_list = goods_list;
    }
}
