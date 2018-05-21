package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/3/6.
 */

public class BabyChartData {
    private String birthday;
    private List<String> month;
    private ChartMax max;
    private List<BabyChart> user_arr;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public ChartMax getMax() {
        return max;
    }

    public void setMax(ChartMax max) {
        this.max = max;
    }

    public List<BabyChart> getUser_arr() {
        return user_arr;
    }

    public void setUser_arr(List<BabyChart> user_arr) {
        this.user_arr = user_arr;
    }
}
