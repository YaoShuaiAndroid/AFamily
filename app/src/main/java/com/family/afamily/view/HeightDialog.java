package com.family.afamily.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.family.afamily.R;

/**
 * Created by bt on 2017/8/2.
 */

public class HeightDialog extends PopupWindow {
    Context context;
    Handler handler;
    String height;

    public HeightDialog(Context mContext, View parent, Handler handler, String height1) {
        this.handler=handler;
        context=mContext;
        height=height1;

        View view = View
                .inflate(mContext, R.layout.dialog_height, null);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);
        setContentView(view);

        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
        }

        //实例化
        init(view);
    }

    public void init(View view) {
        TextView mCancelText= (TextView) view.findViewById(R.id.weight_cancel_text);
        TextView mCommitText= (TextView) view.findViewById(R.id.weight_commit_text);
        final NumberPicker height_num1= (NumberPicker) view.findViewById(R.id.height_num1);
        final NumberPicker height_num2= (NumberPicker) view.findViewById(R.id.height_num2);
        height_num1.setMaxValue(150);
        height_num1.setMinValue(1);
        height_num2.setMaxValue(9);
        height_num2.setMinValue(0);

        if(null!=height&&height.contains(".")){
            height_num1.setValue(Integer.parseInt(height.substring(0,height.indexOf("."))));
            height_num2.setValue(Integer.parseInt(height.substring(height.indexOf(".")+1,height.length())));
        }else{
            height_num1.setValue(160);
            height_num2.setValue(0);
        }

        mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mCommitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.obj=height_num1.getValue()+"."+height_num2.getValue();
                message.what=2;
                handler.sendMessage(message);
                dismiss();
            }
        });
    }
}
