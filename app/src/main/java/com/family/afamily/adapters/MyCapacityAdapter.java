package com.family.afamily.adapters;

import android.content.Context;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.entity.PdfModel;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class MyCapacityAdapter extends SuperBaseAdapter<PdfModel> {

    public MyCapacityAdapter(Context context, List<PdfModel> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PdfModel item, int position) {
        TextView capacity_title=holder.getView(R.id.capacity_title);
        TextView capacity_date=holder.getView(R.id.capacity_date);

        capacity_title.setText(item.getPdf_name());
        capacity_date.setText(item.getTime());
    }

    @Override
    protected int getItemViewLayoutId(int position,PdfModel item) {
        return R.layout.list_capacity_item;
    }
}
