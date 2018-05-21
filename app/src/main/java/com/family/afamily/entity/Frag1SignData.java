package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2017/12/20.
 */

public class Frag1SignData {
    private List<String> day_check_list;
    private Integer check_day;
    private String remind;
    private Integer is_check;
    private List<SignData> dayList;

    public List<SignData> getDayList() {
        return dayList;
    }

    public void setDayList(List<SignData> dayList) {
        this.dayList = dayList;
    }

    public Integer getIs_check() {
        return is_check;
    }

    public void setIs_check(Integer is_check) {
        this.is_check = is_check;
    }

    public List<String> getDay_check_list() {
        return day_check_list;
    }

    public void setDay_check_list(List<String> day_check_list) {
        this.day_check_list = day_check_list;
    }

    public Integer getCheck_day() {
        return check_day;
    }

    public void setCheck_day(Integer check_day) {
        this.check_day = check_day;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    @Override
    public String toString() {
        return "Frag1SignData{" +
                "day_check_list=" + day_check_list +
                ", check_day=" + check_day +
                ", remind='" + remind + '\'' +
                ", is_check=" + is_check +
                '}';
    }
}
