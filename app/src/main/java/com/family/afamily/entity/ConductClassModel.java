package com.family.afamily.entity;

/**
 * Created by bt on 2018/4/13.
 */

public class ConductClassModel {
       private String id;//": "1",
    private String type;//": "体能"
    private boolean isSelect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
