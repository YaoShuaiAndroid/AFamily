package com.family.afamily.activity.mvp.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/2.
 */

public interface SubmitSuccessView extends BaseView {

    void submitSuccess(List<Map<String, String>> mapList);

}
