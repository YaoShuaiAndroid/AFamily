package com.family.afamily.entity;

/**
 * Created by hp2015-7 on 2018/1/6.
 */

public class GoodsAttrValue {
    private String label;
    private Double price;
    private String format_price;
    private String id;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getPrice() {
        return price == null ? 0.00 : price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFormat_price() {
        return format_price;
    }

    public void setFormat_price(String format_price) {
        this.format_price = format_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GoodsAttrValue{" +
                "label='" + label + '\'' +
                ", price='" + price + '\'' +
                ", format_price='" + format_price + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
