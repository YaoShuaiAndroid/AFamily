package com.family.afamily.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.mvp.presents.MyVideoPresenter;
import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.upload_db.UploadDao;
import com.family.afamily.upload_service.UploadVideoService;
import com.family.afamily.utils.SPUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class UploadVideoAdapter extends SuperBaseAdapter<UploadVideoData> {
    private Activity context;
    private String token;
    private UploadDao uploadDao;
    private MyVideoPresenter presenter;

    public UploadVideoAdapter(Activity context, List<UploadVideoData> data, MyVideoPresenter presenter) {
        super(context, data);
        this.context = context;
        token = (String) SPUtils.get(context, "token", "");
        uploadDao = new UploadDao(context);
        this.presenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder holder, final UploadVideoData item, int position) {
        holder.setText(R.id.item_content_tv, item.getTitle());

        ImageView item_img_tv = holder.getView(R.id.item_img_tv);
        final TextView item_issue_error = holder.getView(R.id.item_issue_error);
        final TextView item_re_issue_tv = holder.getView(R.id.item_re_issue_tv);

        final ProgressBar item_video_pro = holder.getView(R.id.item_video_pro);
        item_video_pro.setProgress(0);
        Glide.with(context).load(item.getFilePath()).into(item_img_tv);
        item_video_pro.setMax(item.getTotalSize());
        item_video_pro.setProgress(item.getCurrentSize());
        final String str = item.getUploadFlag() == 0 ? "等待上传" : item.getUploadFlag() == 1 || item.getUploadFlag() == 4 ? "正在上传" : item.getUploadFlag() == 2 ? "当前为移动网络" : "视频发布失败";
        item_issue_error.setText(str);
        if (item.getUploadFlag() == 0 || item.getUploadFlag() == 1 || item.getUploadFlag() == 4) {
            item_re_issue_tv.setVisibility(View.GONE);
        } else if (item.getUploadFlag() == 2) {
            item_re_issue_tv.setVisibility(View.VISIBLE);
            item_re_issue_tv.setText("继续上传");
        } else if (item.getUploadFlag() == 3) {
            item_re_issue_tv.setText("重新发布");
            item_video_pro.setMax(100);
            item_video_pro.setProgress(100);
            item_re_issue_tv.setVisibility(View.VISIBLE);
        }

        item_re_issue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getUploadFlag() == 3) {
                    if (item.getFlag() == 1) {
                        presenter.submitData(token, item.getVideoPath(), item.getTitle(), item.getType(), item, uploadDao);
                    } else {
                        presenter.submitVideo(token, item.getChild_id(), item.getTitle(), item.getVideoPath(),
                                item.getCreate_time(), item.getAddress(), item, uploadDao);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put("upload_flag", 4);
                    uploadDao.updateMsm(values, item.getId());
                    item_re_issue_tv.setVisibility(View.GONE);
                    item_issue_error.setText("正在上传");
                    Intent service = new Intent(context, UploadVideoService.class);
                    context.startService(service);
                }
            }
        });

        holder.setOnClickListener(R.id.item_del_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showUploadDialog(context, item, uploadDao);
            }
        });

        //  L.e("tag","------------------>"+item.toString());
    }

    @Override
    protected int getItemViewLayoutId(int position, UploadVideoData item) {
        return R.layout.item_upload_video;
    }

}
