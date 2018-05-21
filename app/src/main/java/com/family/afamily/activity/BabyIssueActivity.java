package com.family.afamily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.family.afamily.R;
import com.family.afamily.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hp2015-7 on 2017/12/14.
 */

public class BabyIssueActivity extends Activity {

    public void back(View v) {
        finish();
    }

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_issue);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Window win = getWindow();
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(this, findViewById(R.id.baby_issue_title));
        }
    }

    @OnClick(R.id.issue_video)
    public void clickIssueVideo() {
        Intent intent = new Intent(BabyIssueActivity.this, BabyIssueVideoActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 100);
        finish();
    }

    @OnClick(R.id.issue_pic)
    public void clickIssuePic() {
        Intent intent = new Intent(BabyIssueActivity.this, BabyIssuePicActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 100);
        finish();
    }
}
