package com.family.afamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.MasterActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.utils.BaseViewHolder;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.UrlUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/6.
 */

public class Frag2FollowAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public Frag2FollowAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_frag2_follow_layout, null);
        }
        ImageView item_follow_head_iv = BaseViewHolder.get(view, R.id.item_follow_head_iv);
        TextView item_follow_nick_tv = BaseViewHolder.get(view, R.id.item_follow_nick_tv);
        TextView item_follow_decs_tv = BaseViewHolder.get(view, R.id.item_follow_decs_tv);
        ImageView item_follow_video = BaseViewHolder.get(view, R.id.item_follow_video);
        TextView item_follow_time = BaseViewHolder.get(view, R.id.item_follow_time);
        ImageView item_follow_zan_iv = BaseViewHolder.get(view, R.id.item_follow_zan_iv);
        TextView item_follow_zan_tv = BaseViewHolder.get(view, R.id.item_follow_zan_tv);
        TextView item_follow_pl_tv = BaseViewHolder.get(view, R.id.item_follow_pl_tv);
        LinearLayout item_follow_root = BaseViewHolder.get(view, R.id.item_follow_root);
        LinearLayout item_follow_head_ll = BaseViewHolder.get(view, R.id.item_follow_head_ll);

        item_follow_head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MasterActivity.class);
                intent.putExtra("user_id", list.get(position).get("user_id"));
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });
        item_follow_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.tx)
                .transform(new GlideCircleTransform(context));
        Glide.with(context).load(list.get(position).get("images")).apply(options).into(item_follow_head_iv);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options2).into(item_follow_video);

        if (TextUtils.isEmpty(list.get(position).get("is_like")) || list.get(position).get("is_like").equalsIgnoreCase("N")) {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_follow);
            item_follow_zan_tv.setTextColor(Color.parseColor("#999999"));
        } else {
            item_follow_zan_iv.setImageResource(R.mipmap.ic_zan_i);
            item_follow_zan_tv.setTextColor(ContextCompat.getColor(context, R.color.color_yellow));
        }

        item_follow_nick_tv.setText(list.get(position).get("nick_name"));
        item_follow_decs_tv.setText(list.get(position).get("intro"));
        item_follow_time.setText(list.get(position).get("create_time"));
        item_follow_zan_tv.setText(list.get(position).get("like"));
        item_follow_pl_tv.setText(list.get(position).get("comment"));
        item_follow_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZaoJaoDetailsActivity.class);
                intent.putExtra("id", list.get(position).get("id"));
                intent.putExtra("study", list.get(position).get("look"));
                context.startActivity(intent);
            }
        });
        return view;
    }
}
