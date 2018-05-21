package com.family.afamily.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public abstract class CommonChaAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    boolean isAutoSize = true;

    public CommonChaAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public CommonChaAdapter(int layoutResId, List<T> data, boolean isAutoSize) {
        super(layoutResId, data);
        this.isAutoSize = isAutoSize;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, T item) {
        convertViewItem(baseViewHolder, item);
    }

    public abstract void convertViewItem(BaseViewHolder baseViewHolder, T item);
}
