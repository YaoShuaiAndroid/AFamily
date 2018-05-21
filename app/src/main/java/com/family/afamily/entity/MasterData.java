package com.family.afamily.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/28.
 */

public class MasterData {
    private List<Map<String, String>> video_info;
    private List<Map<String, String>> expert;

    public List<Map<String, String>> getVideo_info() {
        return video_info;
    }

    public void setVideo_info(List<Map<String, String>> video_info) {
        this.video_info = video_info;
    }

    public List<Map<String, String>> getExpert() {
        return expert;
    }

    public void setExpert(List<Map<String, String>> expert) {
        this.expert = expert;
    }
}
