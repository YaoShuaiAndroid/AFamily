package com.family.afamily.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/29.
 */

public class MyPopUtils {

    public static void showZjTypeDialog(final Activity mActivity, List<Map<String, String>> list, final View root, final ClickItem clickItem) {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_zj_type_dialog, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.pop_list_type_rv);
        LinearLayout date_root_ll = contentView.findViewById(R.id.pop_root_ll);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(root, 0, 0);

        date_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#f6f6f6"));
        recyclerView.addItemDecoration(divider);
        CommonAdapter<Map<String, String>> adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_text_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> stringStringMap, final int position) {
                TextView item_text_tv = holder.getView(R.id.item_text_tv);
                item_text_tv.setText(stringStringMap.get("type"));
                item_text_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                        if (clickItem != null) {
                            clickItem.clickItem(stringStringMap.get("type"), position);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public interface ClickItem {
        void clickItem(String str, int p);
    }

}
