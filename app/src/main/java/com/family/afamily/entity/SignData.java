package com.family.afamily.entity;

/**
 * Created by hp2015-7 on 2017/12/20.
 */

public class SignData {

    private boolean isClick;
    private int day;
    private String dayStr;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    @Override
    public String toString() {
        return "SignData{" +
                "isClick=" + isClick +
                ", day=" + day +
                ", dayStr='" + dayStr + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
