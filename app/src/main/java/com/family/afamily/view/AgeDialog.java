package com.family.afamily.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
 * Created by yaos on 2017/8/2.
 */

public class AgeDialog extends PopupWindow {
    Context context;
    Handler handler;
    int weight;

    public AgeDialog(Context mContext, View parent, Handler handler, int weight1) {
        this.handler=handler;
        context=mContext;
        weight=weight1;

        View view = View
                .inflate(mContext, R.layout.dailog_age, null);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
        //实例化
        init(view);
    }

    public void init(View view) {
        TextView mCancelText= (TextView) view.findViewById(R.id.weight_cancel_text);
        TextView mCommitText= (TextView) view.findViewById(R.id.weight_commit_text);
        final NumberPicker weidht_num1= (NumberPicker) view.findViewById(R.id.ageNum1);
        final NumberPicker weidht_num2= (NumberPicker) view.findViewById(R.id.ageNum2);

        weidht_num1.setMaxValue(6);
        weidht_num1.setMinValue(0);
        weidht_num2.setMaxValue(12);
        weidht_num2.setMinValue(1);

       /* if(null!=weight&&weight.contains(".")){
            weidht_num1.setValue(Integer.parseInt(weight.substring(0,weight.indexOf("."))));
            weidht_num2.setValue(Integer.parseInt(weight.substring(weight.indexOf(".")+1,weight.length())));
        }else{
            weidht_num1.setValue(0);
            weidht_num2.setValue(12);
        }*/

        mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(2);

                dismiss();
            }
        });

        mCommitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.arg1=weidht_num1.getValue()*12+weidht_num2.getValue();
                message.what=1;
                handler.sendMessage(message);
                dismiss();
            }
        });
    }
}
