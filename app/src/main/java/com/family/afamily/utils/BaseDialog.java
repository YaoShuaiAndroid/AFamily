package com.family.afamily.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.family.afamily.R;


/**
 * Created by hp2015-7 on 2017/5/27.
 */
public abstract class BaseDialog {
    protected BaseDialog(Context context, int layout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(layout, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
        getMView(view, dialog);
    }

    protected abstract void getMView(View view, Dialog dialog);
}
