package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class BorrowBookAdapter extends SuperBaseAdapter<Map<String, String>> {
    public BorrowBookAdapter(Context context, List<Map<String, String>> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, String> data, int position) {
        ImageView borrow_img = holder.getView(R.id.borrow_img);//picture
        holder.setText(R.id.borrow_name, data.get("goods_name"));
        holder.setText(R.id.borrow_time,"时间："+ data.get("borrow_time"));

        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        Glide.with(borrow_img.getContext()).load(data.get("picture")).apply(options2).into(borrow_img);
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_borrow_book;
    }
}
