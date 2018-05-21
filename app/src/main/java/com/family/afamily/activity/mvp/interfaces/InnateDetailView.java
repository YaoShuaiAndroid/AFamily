package com.family.afamily.activity.mvp.interfaces;

import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.CommentModel;
import com.family.afamily.entity.InnateIntelligenceModel;

import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/30.
 */

public interface InnateDetailView extends BaseView {
    void successData(BasePageBean<CommentModel> data,int type);

    void successDetailData(InnateIntelligenceModel data);

    void submitLike(int result);

    void submitComment(CommentModel commentModel);
}
