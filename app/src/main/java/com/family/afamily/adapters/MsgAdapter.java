package com.family.afamily.adapters;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.family.afamily.R;
import com.family.afamily.entity.MsgData;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/8.
 */

public class MsgAdapter extends SuperBaseAdapter<MsgData> {
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public MsgAdapter(Context context, List<MsgData> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final MsgData item, int position) {
        holder.setText(R.id.msg_content_tv, item.getNote());
        final CheckBox item_msg_check = holder.getView(R.id.item_msg_check);
        if(isShow){
            item_msg_check.setVisibility(View.VISIBLE);
        }else{
            item_msg_check.setVisibility(View.GONE);
        }
        item_msg_check.setChecked(item.isCheck());
        item_msg_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = item.isCheck();
                item_msg_check.setChecked(!ischeck);
                item.setCheck(!ischeck);
            }
        });
    }

    @Override
    protected int getItemViewLayoutId(int position, MsgData item) {
        return R.layout.item_msg_list;
    }
}
