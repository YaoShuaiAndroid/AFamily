package com.family.afamily.entity;

import java.io.Serializable;

/**
 * Created by hp2015-7 on 2018/1/16.
 */

public class ConfigData implements Serializable {
    private String version_title;
    private String version_name;
    private Integer version_number;
    private String anzhuo_url;
    private String version_info;
    private String wx_code;
    private String reg_message;

    public String getReg_message() {
        return reg_message;
    }

    public void setReg_message(String reg_message) {
        this.reg_message = reg_message;
    }

    public String getVersion_title() {
        return version_title;
    }

    public void setVersion_title(String version_title) {
        this.version_title = version_title;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public Integer getVersion_number() {
        return version_number == null ? 1 : version_number;
    }

    public void setVersion_number(Integer version_number) {
        this.version_number = version_number;
    }

    public String getAnzhuo_url() {
        return anzhuo_url;
    }

    public void setAnzhuo_url(String anzhuo_url) {
        this.anzhuo_url = anzhuo_url;
    }

    public String getVersion_info() {
        return version_info;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }

    public String getWx_code() {
        return wx_code;
    }

    public void setWx_code(String wx_code) {
        this.wx_code = wx_code;
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                "version_title='" + version_title + '\'' +
                ", version_name='" + version_name + '\'' +
                ", version_number=" + version_number +
                ", anzhuo_url='" + anzhuo_url + '\'' +
                ", version_info='" + version_info + '\'' +
                ", wx_code='" + wx_code + '\'' +
                ", reg_message='" + reg_message + '\'' +
                '}';
    }
}
