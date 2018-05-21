package com.family.afamily.activity.mvp.interfaces;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public interface BaseView {
    /**
     * 显示loading框
     *
     * @param showType 1,登录，2提交请求，3获取请求
     */
    void showProgress(int showType);

    /**
     * 隐藏loading框
     */
    void hideProgress();

    void toast(CharSequence s);


}
