package com.family.afamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.ActionDetailsActivity;
import com.family.afamily.activity.ActionSignActivity;
import com.family.afamily.activity.mvp.presents.MyActionPresenter;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class MyActionAdapter extends SuperBaseAdapter<Map<String, String>> {
    private int type = 1;
    private Context context;
    private MyActionPresenter presenter;
    private String token;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyActionAdapter(Context context, List<Map<String, String>> data, MyActionPresenter presenter) {
        super(context, data);
        this.context = context;
        this.presenter = presenter;
        token = (String) SPUtils.get(context, "token", "");
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, int position) {
        TextView item_action_ckbm_tv = holder.getView(R.id.item_action_ckbm_tv);
        TextView item_action_del = holder.getView(R.id.item_action_del);
        ImageView item_action_img = holder.getView(R.id.item_action_img);
        RelativeLayout item_action_root = holder.getView(R.id.item_action_root);
        TextView item_action_state = holder.getView(R.id.item_action_state);

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(context).load(item.get("picture")).apply(options2).into(item_action_img);

        holder.setText(R.id.item_action_name, item.get("title"));
        holder.setText(R.id.item_action_time, item.get("active_time"));


        item_action_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActionDetailsActivity.class);
                intent.putExtra("id", item.get("id"));
                ((Activity)context).startActivityForResult(intent,100);
            }
        });

        if (type == 2) {
            item_action_del.setText("删除");
            item_action_state.setVisibility(View.VISIBLE);
            item_action_state.setText(item.get("status"));

            if(item.get("status").equals("已审核")){
                item_action_ckbm_tv.setVisibility(View.VISIBLE);
            }else{
                item_action_ckbm_tv.setVisibility(View.GONE);
            }

        } else {
            item_action_del.setText("取消报名");
            item_action_ckbm_tv.setVisibility(View.GONE);
            item_action_state.setVisibility(View.GONE);
        }

        item_action_ckbm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActionSignActivity.class);
                intent.putExtra("id", item.get("id"));
                context.startActivity(intent);
            }
        });

        item_action_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.get("id");
                if (TextUtils.isEmpty(id)) {
                    Utils.showMToast(context, "该活动数据异常，请联系客服处理");
                } else {
//                    if(type == 1){
//                        String str = item.get("is_apply");
//                        if("1".equals(str)){
//
//                        }
//                    }
                    presenter.showDialog(context, token, id, type);
                }
            }
        });

    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_my_action;
    }
}
