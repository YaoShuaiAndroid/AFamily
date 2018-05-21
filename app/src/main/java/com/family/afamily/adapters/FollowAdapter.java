package com.family.afamily.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.FollowActivity;
import com.family.afamily.activity.mvp.presents.FollowPresenter;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class FollowAdapter extends SuperBaseAdapter<Map<String, String>> {
    private FollowPresenter presenter;
    private String token;
    private FollowActivity activity;

    public FollowAdapter(FollowActivity context, List<Map<String, String>> data, FollowPresenter presenter) {
        super(context, data);
        this.presenter = presenter;
        this.activity = context;
        token = (String) SPUtils.get(context, "token", "");
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, int position) {
        ImageView item_follow_head = holder.getView(R.id.item_follow_head);
        TextView item_cancel_btn = holder.getView(R.id.item_cancel_btn);
        //images
        holder.setText(R.id.item_follow_nick_tv, item.get("nick_name"));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.tx)
                .transform(new GlideCircleTransform(item_cancel_btn.getContext()));
        Glide.with(item_cancel_btn.getContext()).load(item.get("images")).apply(options).into(item_follow_head);

        item_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = item.get("user_id");
                if (TextUtils.isEmpty(user_id)) {
                    Utils.showMToast(activity, "找不到用户ID");
                } else {
                    presenter.showCancelDialog(token, item.get("user_id"), activity);
                }
            }
        });
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_follow_layout;
    }
}
