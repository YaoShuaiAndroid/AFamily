package com.family.afamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.entity.GoodsAttr;
import com.family.afamily.flowtag.FlowTagLayout;
import com.family.afamily.flowtag.OnTagClickListener;
import com.family.afamily.flowtag.OnTagSelectListener;
import com.family.afamily.utils.BaseViewHolder;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/6.
 */

public class Frag4GoodsAttrAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsAttr> list;
    private SelectTag selectTag;

    public Frag4GoodsAttrAdapter(Context context, List<GoodsAttr> list, SelectTag selectTag) {
        this.context = context;
        this.list = list;
        this.selectTag = selectTag;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_goods_attr, null);
        }
        TextView item_goods_attr_name = BaseViewHolder.get(view, R.id.item_goods_attr_name);
        FlowTagLayout item_attr_list = BaseViewHolder.get(view, R.id.item_attr_list);

        item_goods_attr_name.setText(list.get(position).getName());
        item_attr_list.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        TagAdapter mMobileTagAdapter = new TagAdapter(context);
        item_attr_list.setAdapter(mMobileTagAdapter);
        mMobileTagAdapter.onlyAddAll(list.get(position).getValues());

        item_attr_list.setOnTagSelectListener(new OnTagSelectListener() {
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList, int p) {
//                if (selectedList != null && selectedList.size() > 0) {
//                    //选中
//                    selectTag.clickTag(position,selectedList.get(0),p);
//                }else{
//                    //取消选中
//                    selectTag.clickTag(position,-1,p);
//                }

                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        for (int j = 0; j < list.get(position).getValues().size(); j++) {
                            if (i == j) {
                                list.get(position).getValues().get(j).setCheck(true);
                            } else {
                                list.get(position).getValues().get(j).setCheck(false);
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < list.get(position).getValues().size(); j++) {
                        list.get(position).getValues().get(j).setCheck(false);
                    }
                }
                selectTag.clickTag();
            }
        });

        item_attr_list.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
            }
        });

        return view;
    }

    public interface SelectTag {
        void clickTag();
    }
}
