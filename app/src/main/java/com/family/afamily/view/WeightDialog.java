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

public class WeightDialog extends PopupWindow {
    Context context;
    Handler handler;
    String weight;

    public WeightDialog(Context mContext, View parent, Handler handler,String weight1) {
        this.handler=handler;
        context=mContext;
        weight=weight1;

        View view = View
                .inflate(mContext, R.layout.dialog_weight, null);

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
        final NumberPicker weidht_num1= (NumberPicker) view.findViewById(R.id.weidht_num1);
        final NumberPicker weidht_num2= (NumberPicker) view.findViewById(R.id.weidht_num2);

        weidht_num1.setMaxValue(50);
        weidht_num1.setMinValue(1);
        weidht_num2.setMaxValue(9);
        weidht_num2.setMinValue(0);

        if(null!=weight&&weight.contains(".")){
            weidht_num1.setValue(Integer.parseInt(weight.substring(0,weight.indexOf("."))));
            weidht_num2.setValue(Integer.parseInt(weight.substring(weight.indexOf(".")+1,weight.length())));
        }else{
            weidht_num1.setValue(50);
            weidht_num2.setValue(0);
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
                message.obj=weidht_num1.getValue()+"."+weidht_num2.getValue();
                message.what=1;
                handler.sendMessage(message);
                dismiss();
            }
        });
    }
}
