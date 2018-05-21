package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.GlideCircleTransform;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class MyExtensionAdapter extends SuperBaseAdapter<Map<String, String>> {

    public MyExtensionAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> item, int position) {
        ImageView my_extension_img = holder.getView(R.id.my_extension_img);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.tx)
                .transform(new GlideCircleTransform(my_extension_img.getContext()));
        Glide.with(my_extension_img.getContext()).load(item.get("images")).apply(options).into(my_extension_img);

        holder.setText(R.id.my_extension_name, item.get("nick_name"));
        holder.setText(R.id.my_extension_time, item.get("parent_time"));
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_my_extension_layout;
    }
}
