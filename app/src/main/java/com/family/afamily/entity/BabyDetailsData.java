package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/11.
 */

public class BabyDetailsData {
    private Map<String, String> child_info;
    private List<Map<String, String>> other_child;
    private BabyDetailsList message_list;

    public Map<String, String> getChild_info() {
        return child_info;
    }

    public void setChild_info(Map<String, String> child_info) {
        this.child_info = child_info;
    }

    public List<Map<String, String>> getOther_child() {
        return other_child;
    }

    public void setOther_child(List<Map<String, String>> other_child) {
        this.other_child = other_child;
    }

    public BabyDetailsList getMessage_list() {
        return message_list;
    }

    public void setMessage_list(BabyDetailsList message_list) {
        this.message_list = message_list;
    }
}
