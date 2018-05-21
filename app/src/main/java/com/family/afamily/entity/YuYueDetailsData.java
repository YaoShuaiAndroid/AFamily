package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/27.
 */

public class YuYueDetailsData {
    private Integer back_integral;
    private Integer user_integral;
    private List<Map<String, String>> pool_list;

    public Integer getBack_integral() {
        return back_integral;
    }

    public void setBack_integral(Integer back_integral) {
        this.back_integral = back_integral;
    }

    public Integer getUser_integral() {
        return user_integral;
    }

    public void setUser_integral(Integer user_integral) {
        this.user_integral = user_integral;
    }

    public List<Map<String, String>> getPool_list() {
        return pool_list;
    }

    public void setPool_list(List<Map<String, String>> pool_list) {
        this.pool_list = pool_list;
    }
}
