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
 * Created by hp2015-7 on 2018/1/15.
 */

public class SearchAdapter extends SuperBaseAdapter<Map<String, String>> {

    public SearchAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView item_zaoj_img = holder.getView(R.id.item_zaoj_img);
        holder.setText(R.id.item_zaoj_title, item.get("intro"));
        holder.setText(R.id.item_zj_play_count, "观看" + item.get("look_count"));
        holder.setText(R.id.item_zj_time, item.get("play_time"));

        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(item_zaoj_img.getContext()).load(item.get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options).into(item_zaoj_img);
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_zao_jiao_list_layout;
    }
}
