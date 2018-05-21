package com.family.afamily.activity.mvp.interfaces;


/**
 * Created by hp2015-7 on 2017/12/18.
 */

public interface RegisterView extends BaseView {
    /**
     * 获取验证码失败
     */
    void getCodeFail();

    /**
     * 校验成功
     */
    void verifySuccess();

    void registerSuccess(String token);
}
