package com.family.afamily.fragment.interfaces;

import com.family.afamily.activity.mvp.interfaces.BaseView;
import com.family.afamily.entity.FollowData;
import com.family.afamily.entity.MasterData;
import com.family.afamily.entity.ZJRecommendData;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public interface Fragment2View extends BaseView {
    void successRecommend(List<ZJRecommendData> data);

    void successFollow(FollowData data);

    void successMaster(MasterData data);

    void successAction(List<Map<String, String>> data);

    void submitFollow();
}
