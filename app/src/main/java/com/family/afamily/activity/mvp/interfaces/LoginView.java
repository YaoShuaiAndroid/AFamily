package com.family.afamily.activity.mvp.interfaces;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public interface LoginView extends BaseView {
    /**
     * 提交失败
     */
    void loginFail(String msg);

    /**
     * 登录成功
     */
    void loginSuccess();
}
