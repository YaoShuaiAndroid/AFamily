package com.family.afamily.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.afamily.R;


/**
 * 网络请求等待加载框
 * Created by hp2015-7 on 2016/6/1.
 */
public class DialogLoading extends Dialog {
    private Context context;
    private Dialog dialog;
    private Animation mRotate;
    private TextView content_tv;
    private ImageView load;
    public DialogLoading(Context context) {
        super(context);
        this.context = context;
        loading();
    }

    public void setDialogContext(String content) {
        // loading();
        content_tv.setText(content);
        mRotate = AnimationUtils.loadAnimation(context, R.anim.rotating);
        load.startAnimation(mRotate);
        dialog.show();
    }

    public void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void loading() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
        content_tv = view.findViewById(R.id.dialog_loading_tv);
        load = view.findViewById(R.id.dialog_loading_img);
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);

    }
}
