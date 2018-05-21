package com.family.afamily.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.activity.mvp.presents.AddressListPresenter;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/15.
 */

public class AddressListActivity extends BaseActivity<AddressListPresenter> implements SubmitSuccessView {
    @BindView(R.id.address_list_rv)
    RecyclerView addressListRv;
    @BindView(R.id.address_add_btn)
    TextView addressAddBtn;
    private CommonAdapter<Map<String, String>> adapter;
    private List<Map<String, String>> mList = new ArrayList<>();

    private boolean isSelect;
    private String token;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_list);
        token = (String) SPUtils.get(mActivity, "token", "");
        isSelect = getIntent().getBooleanExtra("isSelect", false);
    }

    @Override
    public void netWorkConnected() {

    }

    @OnClick(R.id.address_add_btn)
    public void clickAddAddress() {
        startActivityForResult(CreateAddressActivity.class, 100);
    }

    @Override
    public AddressListPresenter initPresenter() {
        return new AddressListPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "地址管理");

        if (Utils.isConnected(mActivity)) {
            presenter.getData(token);
        }
        addressListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, Utils.dp2px(10), Color.parseColor("#f8f8f8"));
        addressListRv.addItemDecoration(divider);
        adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_address_list_layout, mList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> s, final int position) {
                holder.setText(R.id.item_address_name, s.get("consignee"));
                holder.setText(R.id.item_address_mobile, s.get("tel"));
                holder.setText(R.id.item_address_tv, s.get("province") + s.get("city") + s.get("district") + s.get("address"));

                holder.setOnClickListener(R.id.item_address_body_rl, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSelect) {
                            Intent data = new Intent();
                            data.putExtra("name", s.get("consignee"));
                            data.putExtra("address_id", s.get("address_id"));
                            data.putExtra("phone", s.get("tel"));
                            data.putExtra("address", s.get("province") + s.get("city") + s.get("district") + s.get("address"));
                            setResult(100, data);
                            finish();
                        }
                    }
                });


                final CheckBox item_address_default = holder.getView(R.id.item_address_default);
                String isd = s.get("is_default");
                if (isd.equals("0")) {
                    item_address_default.setChecked(false);
                } else {
                    item_address_default.setChecked(true);
                }

                item_address_default.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int str = item_address_default.isChecked() ? 2 : 3;
                        showDeletDialog(mActivity, token, s.get("address_id"), str);
                    }
                });

                holder.setOnClickListener(R.id.item_address_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, CreateAddressActivity.class);
                        intent.putExtra("address_id", s.get("address_id"));
                        intent.putExtra("province_id", s.get("province2"));
                        intent.putExtra("city_id", s.get("city2"));
                        intent.putExtra("area_id", s.get("district2"));
                        intent.putExtra("address", s.get("province") + s.get("city") + s.get("district") + s.get("address"));
                        intent.putExtra("decs", s.get("address"));
                        intent.putExtra("name", s.get("consignee"));
                        intent.putExtra("phone", s.get("tel"));
                        startActivityForResult(intent, 100);
                    }
                });

                holder.setOnClickListener(R.id.item_address_del, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeletDialog(mActivity, token, s.get("address_id"), 1);
                    }
                });

            }
        };
        addressListRv.setAdapter(adapter);
    }


    public void showDeletDialog(Activity mActivity, final String token, final String address_id, final int type) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);
                String str = type == 1 ? "是否删除该地址？" : type == 2 ? "是否设置该地址为默认地址" : "是否取消该地址的默认地址？";
                dialog_title_tv.setText("提示");
                dialog_content_tv.setText(str);
                dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });

                dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        submitAddress(token, address_id, type);
                    }
                });
            }
        };
    }

    public void submitAddress(final String token, String address_id, int type) {
        showProgress(3);
        Map<String, String> params = new HashMap<>();
        if (type == 1) {
            params.put("delete", "1");
        } else if (type == 2) {
            params.put("is_default", "1");
        } else {
            params.put("is_default", "2");
        }
        params.put("address_id", address_id);
        params.put("token", token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.ADD_ADDRESS_URL, new OkHttpClientManager.ResultCallback<ResponseBean<String>>() {
            @Override
            public void onError(Request request, Exception e) {
                hideProgress();
                toast("提交失败");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onResponse(ResponseBean<String> response) {
                hideProgress();
                if (response == null || response.getRet_code() != 1) {
                    String msg = response == null ? "提交失败" : response.getMsg();
                    toast(msg);
                    adapter.notifyDataSetChanged();
                } else {
                    toast(response.getMsg());
                    presenter.getData(token);
                }
            }
        }, params);
    }


    @Override
    public void submitSuccess(List<Map<String, String>> mapList) {
        mList.clear();
        if (mapList != null && mapList.size() > 0) {
            mList.addAll(mapList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            presenter.getData(token);
        }
    }
}
