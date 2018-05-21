package com.family.afamily.fragment.interfaces;

import com.family.afamily.activity.mvp.interfaces.BaseView;
import com.family.afamily.entity.Frag1SignData;
import com.family.afamily.entity.HomeData;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public interface Fragment1View extends BaseView {

    void checkMemberCallback();
    void signDataSuccess(Frag1SignData signData);

    void homeDataSuccess(HomeData homeData);

}
