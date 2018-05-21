package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BorrowBookData;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public interface BorrowBookView extends BaseView {

    void successData(BorrowBookData bookData, int getType, boolean isOk);
}
