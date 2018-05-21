package com.family.afamily.activity.mvp.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public interface ReplyView extends BaseView {
    void successData(List<Map<String, String>> data);
}
