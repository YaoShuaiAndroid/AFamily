package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.UrlUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/12.
 */

public class StudyRecordAdapter extends SuperBaseAdapter<Map<String, String>> {
    public StudyRecordAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_zaoj_img = holder.getView(R.id.item_zaoj_img);
        holder.setText(R.id.item_zaoj_title, item.get("intro"));
        holder.setText(R.id.item_time_tv, item.get("create_time"));

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(item_zaoj_img.getContext()).load(item.get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options2).into(item_zaoj_img);

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_study_record_layout;
    }
}
