package com.family.afamily.activity.mvp.presents;

import android.app.Activity;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public abstract class BasePresent<T> {
    public Activity context;
    public T view;

    public void attach(T view) {
        this.view = view;
    }

    public void detach() {
        this.view = null;
    }

}
