package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public class ZJRecommendData {
    private String type;
    private String english_name;
    private String id;
    private List<Map<String, String>> video;

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

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public List<Map<String, String>> getVideo() {
        return video;
    }

    public void setVideo(List<Map<String, String>> video) {
        this.video = video;
    }
}
