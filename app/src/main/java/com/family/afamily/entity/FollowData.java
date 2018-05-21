package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public class FollowData {
    private List<Map<String, String>> attention_list;
    private List<Map<String, String>> video_info;

    public List<Map<String, String>> getAttention_list() {
        return attention_list;
    }

    public void setAttention_list(List<Map<String, String>> attention_list) {
        this.attention_list = attention_list;
    }

    public List<Map<String, String>> getVideo_info() {
        return video_info;
    }

    public void setVideo_info(List<Map<String, String>> video_info) {
        this.video_info = video_info;
    }
}
