package com.family.afamily.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.AddBabyGrowUpView;
import com.family.afamily.activity.mvp.presents.AddbabyGrowUpPresenter;
import com.family.afamily.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/3/5.
 */

public class AddBabyGrowUpActivity extends BaseActivity<AddbabyGrowUpPresenter> implements AddBabyGrowUpView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.add_input_cm)
    EditText addInputCm;
    @BindView(R.id.add_input_weight)
    EditText addInputWeight;
    @BindView(R.id.add_input_head)
    EditText addInputHead;
    @BindView(R.id.add_date_tv)
    TextView addDateTv;
    private int year,month,day;
    private String birthday;
    private String token;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    /** 输入框小数的位数*/
    private static final int DECIMAL_DIGITS = 1;
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_baby_growup);
        birthday = getIntent().getStringExtra("birthday");
        token = (String) SPUtils.get(mActivity,"token","");
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.add_date_tv)
    public void clickDate(){
        presenter.showDateDialog(addDateTv,year,month,day);
    }
    @Override
    public AddbabyGrowUpPresenter initPresenter() {
        return new AddbabyGrowUpPresenter(this);
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        setTitle(this,"增加成长记录");
        baseTitleRightTv.setText("提交");
        String data[] = birthday.split("-");
        if(data.length != 3){
            toast("宝宝生日格式有误");
            finish();
        }else{
            year = Integer.parseInt(data[0]);
            month = Integer.parseInt(data[1]);
            day = Integer.parseInt(data[2]);
        }

        setEditDouble(addInputCm);
        setEditDouble(addInputWeight);
        setEditDouble(addInputHead);

        addDateTv.setText(format.format(new Date()));
        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String height = addInputCm.getText().toString();
                String weight = addInputWeight.getText().toString();
                String head = addInputHead.getText().toString();
                String add_time = addDateTv.getText().toString();
                if(TextUtils.isEmpty(height)){
                    toast("请输入身高");
                }else if(TextUtils.isEmpty(weight)){
                    toast("请输入体重");
                }else if(TextUtils.isEmpty(head)){
                    toast("请输入头围");
                }else{
                    presenter.submitData(token,height,weight,head,add_time);
                }
            }
        });

    }


    private void setEditDouble(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    @Override
    public void callbackData() {
        setResult(100);
        finish();
    }
}
