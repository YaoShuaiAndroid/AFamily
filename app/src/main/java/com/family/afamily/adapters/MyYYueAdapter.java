package com.family.afamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.mvp.presents.MyYYuePresenter;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/3/14.
 */

public class MyYYueAdapter extends SuperBaseAdapter<Map<String, String>> {

    private MyYYuePresenter presenter;
    private Activity mActivity;
    private String token;
    public MyYYuePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(MyYYuePresenter presenter) {
        this.presenter = presenter;
    }

    public MyYYueAdapter(Activity mActivity, String token,List<Map<String, String>> data) {
        super(mActivity, data);
        this.mActivity = mActivity;
        this.token = token;
    }

    @Override
    protected void convert(BaseViewHolder holder,final Map<String, String> item, int position) {
        holder.setText(R.id.item_yyue_time, item.get("start_time"));
        holder.setText(R.id.item_yyue_name, item.get("pool_name"));
        holder.setText(R.id.item_yyue_type, item.get("pool_type"));
        holder.setText(R.id.item_yyue_address, item.get("pool_address"));
        TextView item_cancel_btn = holder.getView(R.id.item_cancel_btn);
       // 0:预约，1：取消预约，2：按时到达，4：不到

       // Integer status = Integer.parseInt(item.get("status"));
        String state = item.get("state");
        if(state.equals("已预约")){
            //取消
            holder.setOnClickListener(R.id.item_cancel_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showCancelDialog(mActivity, token, item.get("id"));
                    mActivity.setResult(101);
                }
            });
            item_cancel_btn.setBackgroundResource(R.drawable.fillet_15_yellow_bg);
            item_cancel_btn.setTextColor(Color.parseColor("#fb9927"));
        }else if(state.equals("已取消")){
            item_cancel_btn.setText("已取消");
            item_cancel_btn.setBackgroundResource(R.drawable.fillet_25_huis_line_bg);
            item_cancel_btn.setTextColor(Color.parseColor("#999999"));
        }else if(state.equals("已赴约")){
            item_cancel_btn.setText("已赴约");
            item_cancel_btn.setBackgroundResource(R.drawable.fillet_25_huis_line_bg);
            item_cancel_btn.setTextColor(Color.parseColor("#999999"));
        }else if(state.equals("已过期")){
            item_cancel_btn.setText("已过期");
            item_cancel_btn.setBackgroundResource(R.drawable.fillet_25_huis_line_bg);
            item_cancel_btn.setTextColor(Color.parseColor("#999999"));
        }else if(state.equals("已迟到")){
            item_cancel_btn.setText("已迟到");
            item_cancel_btn.setBackgroundResource(R.drawable.fillet_25_huis_line_bg);
            item_cancel_btn.setTextColor(Color.parseColor("#999999"));
        }


    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return  R.layout.item_my_yyue_layout;
    }
}
