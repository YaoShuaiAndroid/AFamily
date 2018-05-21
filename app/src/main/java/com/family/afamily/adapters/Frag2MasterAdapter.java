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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.MasterActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.utils.BaseViewHolder;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/6.
 */

public class Frag2MasterAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public Frag2MasterAdapter(Context context, List<Map<String, String>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_frag2_master_layout, null);
        }
        ImageView item_master_head_iv = BaseViewHolder.get(view, R.id.item_master_head_iv);
        TextView item_master_nick = BaseViewHolder.get(view, R.id.item_master_nick);
        TextView item_master_time = BaseViewHolder.get(view, R.id.item_master_time);
        TextView item_master_btn = BaseViewHolder.get(view, R.id.item_master_btn);
        TextView item_master_decs = BaseViewHolder.get(view, R.id.item_master_decs);
        TextView item_master_date = BaseViewHolder.get(view, R.id.item_master_date);
        TextView item_master_zan_tv = BaseViewHolder.get(view, R.id.item_master_zan_tv);
        ImageView item_master_zan_iv = BaseViewHolder.get(view, R.id.item_master_zan_iv);
        TextView item_master_pl_tv = BaseViewHolder.get(view, R.id.item_master_pl_tv);
        ImageView item_master_video = BaseViewHolder.get(view, R.id.item_master_video);
        RelativeLayout item_master_head_rl = BaseViewHolder.get(view, R.id.item_master_head_rl);
        LinearLayout item_master_root_ll = BaseViewHolder.get(view, R.id.item_master_root_ll);

        String focus = list.get(position).get("focus");
        if ("Y".equals(focus)) {
            item_master_btn.setText("已关注");
        } else {
            item_master_btn.setText("+关注");
        }
        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(context).load(list.get(position).get("video_url") + UrlUtils.VIDEO_4_4_PIC).apply(options2).into(item_master_video);

        item_master_head_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oneself = list.get(position).get("oneself");
                if ("Y".equals(oneself)) {
                    Utils.showMToast(context, "不能查看自己主页");
                } else {
                    Intent intent = new Intent(context, MasterActivity.class);
                    intent.putExtra("user_id", list.get(position).get("user_id"));
                    ((Activity) context).startActivityForResult(intent, 100);
                }
            }
        });

        item_master_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZaoJaoDetailsActivity.class);
                intent.putExtra("id", list.get(position).get("id"));
                intent.putExtra("study", list.get(position).get("look"));
                context.startActivity(intent);
            }
        });

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.tx)
                .transform(new GlideCircleTransform(context));
        Glide.with(context).load(list.get(position).get("images")).apply(options).into(item_master_head_iv);
        item_master_nick.setText(list.get(position).get("nick_name"));
        item_master_decs.setText(list.get(position).get("intro"));
        item_master_zan_tv.setText(list.get(position).get("like"));
        item_master_pl_tv.setText(list.get(position).get("comment"));
        item_master_date.setText(list.get(position).get("create_time"));

        item_master_time.setText(list.get(position).get("week_time") + " " + list.get(position).get("detail_time"));

        if (TextUtils.isEmpty(list.get(position).get("is_like")) || list.get(position).get("is_like").equalsIgnoreCase("N")) {
            item_master_zan_iv.setImageResource(R.mipmap.ic_follow);
            item_master_zan_tv.setTextColor(Color.parseColor("#999999"));
        } else {
            item_master_zan_iv.setImageResource(R.mipmap.ic_zan_i);
            item_master_zan_tv.setTextColor(ContextCompat.getColor(context, R.color.color_yellow));
        }
        return view;
    }
}
