package com.family.afamily.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.activity.mvp.presents.EvaluatePresenter;
import com.family.afamily.entity.OrderListChildData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.view.StarBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 评价
 * Created by hp2015-7 on 2018/1/11.
 */

public class EvaluateActivity extends BaseActivity<EvaluatePresenter> implements SubmitSuccessView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.evaluate_list_rl)
    RecyclerView evaluateListRl;
    private List<OrderListChildData> list = new ArrayList<>();
    private CommonAdapter<OrderListChildData> adapter;
    private int index = -1;
    private String token, order_id;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_evaluate);
        token = (String) SPUtils.get(mActivity, "token", "");
        order_id = getIntent().getStringExtra("order_id");
        list = myApplication.getListChildDatas();
        if (list == null || list.size() == 0) {
            toast("数据出错");
            finish();
        }
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public EvaluatePresenter initPresenter() {
        return new EvaluatePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "评价");
        baseTitleRightTv.setText("提交");

        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goods_id = "";
                String eval_mark = "";
                String eval_comm = "";
                boolean isOK = true;
                for (int i = 0; i < list.size(); i++) {
                    OrderListChildData data = list.get(i);
                    if (TextUtils.isEmpty(data.getEvaluate_str()) || data.getEvaluate() == 0) {
                        isOK = false;
                        break;
                    } else {
                        goods_id += data.getGoods_id() + ",";
                        eval_mark += data.getEvaluate() + ",";
                        eval_comm += data.getEvaluate_str() + ",";
                    }
                }
                if (!isOK) {
                    toast("请填写完所有评论跟评分后提交");
                } else {
                    goods_id = goods_id.substring(0, goods_id.length() - 1);
                    eval_mark = eval_mark.substring(0, eval_mark.length() - 1);
                    eval_comm = eval_comm.substring(0, eval_comm.length() - 1);
                    presenter.submitData(token, order_id, goods_id, eval_mark, eval_comm);
                }

            }
        });


        evaluateListRl.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CommonAdapter<OrderListChildData>(mActivity, R.layout.item_evaluate_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final OrderListChildData data, final int position) {
                ImageView item_eval_img = holder.getView(R.id.item_eval_img);
                StarBar item_eval_star = holder.getView(R.id.item_eval_star);
                EditText item_eval_content_et = holder.getView(R.id.item_eval_content_et);
                item_eval_star.setIntegerMark(true);

                item_eval_star.setStarMark(data.getEvaluate(), 3);
                item_eval_content_et.setText(data.getEvaluate_str());

                item_eval_star.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
                    @Override
                    public void onStarChange(float mark, int type) {
                        if (type == 3) {
                            data.setEvaluate((int) mark);
                            list.set(position, data);
                        }
                    }
                });

                RequestOptions options2 = new RequestOptions();
                options2.error(R.drawable.error_pic);
                Glide.with(mActivity).load(data.getGoods_thumb()).apply(options2).into(item_eval_img);
                holder.setText(R.id.item_eval_name_tv, data.getGoods_name());

                item_eval_content_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        index = position;
                    }
                });

                item_eval_content_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        data.setEvaluate_str(s.toString());
                        list.set(index, data);
                    }
                });

            }
        };
        evaluateListRl.setAdapter(adapter);
    }


    @Override
    public void submitSuccess(List<Map<String, String>> mapList) {
        setResult(100);
        finish();
    }
}
