package com.family.afamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.activity.ZaoJiaoListActivity;
import com.family.afamily.entity.ZJRecommendData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.MultiItemTypeAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.BaseViewHolder;
import com.family.afamily.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/6.
 */

public class Frag2RecommendAdapter extends BaseAdapter {
    private Context context;
    private CommonAdapter<Map<String, String>> childAdapter;
    private List<ZJRecommendData> mList = new ArrayList<>();

    public Frag2RecommendAdapter(Context context, List<ZJRecommendData> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_frag2_recommend, null);
        }
        RelativeLayout item_recommend_rl = BaseViewHolder.get(view, R.id.item_recommend_rl);
        TextView item_recommend_name = BaseViewHolder.get(view, R.id.item_recommend_name);
        TextView item_recommend_enl = BaseViewHolder.get(view,R.id.item_recommend_enl);
        item_recommend_name.setText(mList.get(position).getType());

        item_recommend_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZaoJiaoListActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                intent.putExtra("type", mList.get(position).getType());
                context.startActivity(intent);
            }
        });
        item_recommend_enl.setText(mList.get(position).getEnglish_name());


        RecyclerView item_frag2_recommend = BaseViewHolder.get(view, R.id.item_frag2_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        item_frag2_recommend.setLayoutManager(linearLayoutManager);
        childAdapter = new CommonAdapter<Map<String, String>>(context, R.layout.item_recommend_child, mList.get(position).getVideo()) {
            @Override
            protected void convert(ViewHolder holder, Map<String, String> s, int position) {
                ImageView item_recommend_iv = holder.getView(R.id.item_recommend_iv);

                ImageView item_recommend_dz_iv = holder.getView(R.id.item_recommend_dz_iv);
                TextView item_recommend_dz = holder.getView(R.id.item_recommend_dz);

                RequestOptions options = new RequestOptions();
                options.error(R.drawable.error_pic);
                Glide.with(context).load(s.get("video_url") + UrlUtils.VIDEO_8_5_PIC).apply(options).into(item_recommend_iv);

                if (s.get("is_like").equalsIgnoreCase("N")) {
                    item_recommend_dz_iv.setImageResource(R.mipmap.ic_follow);
                    item_recommend_dz.setTextColor(Color.parseColor("#999999"));
                } else {
                    item_recommend_dz_iv.setImageResource(R.mipmap.ic_zan_i);
                    item_recommend_dz.setTextColor(ContextCompat.getColor(mContext, R.color.color_yellow));
                }

                item_recommend_dz.setText(s.get("like"));
                holder.setText(R.id.item_recommend_pl, s.get("comment"));
                holder.setText(R.id.item_recommend_title, s.get("intro"));
            }
        };
        item_frag2_recommend.setAdapter(childAdapter);

        childAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int p) {
                Intent intent = new Intent(context, ZaoJaoDetailsActivity.class);
                intent.putExtra("id", mList.get(position).getVideo().get(p).get("id"));
                intent.putExtra("study", mList.get(position).getVideo().get(p).get("look"));
                context.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return view;
    }
}
