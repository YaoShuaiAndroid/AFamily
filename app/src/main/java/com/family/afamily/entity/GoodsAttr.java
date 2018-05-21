package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/6.
 */

public class GoodsAttr {
    private String attr_type;
    private String name;
    private List<GoodsAttrValue> values;

    public String getAttr_type() {
        return attr_type;
    }

    public void setAttr_type(String attr_type) {
        this.attr_type = attr_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsAttrValue> getValues() {
        return values;
    }

    public void setValues(List<GoodsAttrValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "GoodsAttr{" +
                "attr_type='" + attr_type + '\'' +
                ", name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
