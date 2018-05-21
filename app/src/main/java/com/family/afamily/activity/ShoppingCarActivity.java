package com.family.afamily.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.ShoppingCarView;
import com.family.afamily.activity.mvp.presents.ShoppingCarPresenter;
import com.family.afamily.entity.ShoppingCarData;
import com.family.afamily.entity.ShoppingCarList;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.BaseDialog;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/11.
 */

public class ShoppingCarActivity extends BaseActivity<ShoppingCarPresenter> implements ShoppingCarView {

    @BindView(R.id.shopping_car_list_rv)
    RecyclerView shoppingCarListRv;
    @BindView(R.id.shopping_car_all_check)
    CheckBox shoppingCarAllCheck;
    @BindView(R.id.shopping_car_total)
    TextView shoppingCarTotal;
    @BindView(R.id.shopping_car_delete)
    TextView shoppingCarDelete;
    @BindView(R.id.shopping_car_pay)
    TextView shoppingCarPay;
    private CommonAdapter<ShoppingCarList> adapter;
    private List<ShoppingCarList> list = new ArrayList<>();

    private String token;
    private boolean isAll = false;
    private ShoppingCarData carData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_car);
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public ShoppingCarPresenter initPresenter() {
        return new ShoppingCarPresenter(this);
    }

    @OnClick(R.id.shopping_car_pay)
    public void clickPay() {
        if (list.size() > 0) {

            String cat_id = "";
            if (isAll) {
                for (int i = 0; i < list.size(); i++) {
                    cat_id += list.get(i).getRec_id() + ",";
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isCheck()) {
                        cat_id += list.get(i).getRec_id() + ",";
                    }
                }
            }
            if (TextUtils.isEmpty(cat_id)) {
                toast("请选择要结算的商品");
            } else {
                cat_id = cat_id.substring(0, cat_id.length() - 1);
                Intent intent = new Intent(mActivity, OrderPayListActivity.class);
                intent.putExtra("cat_id", cat_id);
                startActivityForResult(intent, 100);
                // startActivity(OrderPayListActivity.class);
            }
        } else {
            toast("您的购物车是空的");
        }
    }

    @OnClick(R.id.shopping_car_delete)
    public void clickDelet() {
        if (list.size() > 0) {
            String cat_id = "";
            if (isAll) {
                for (int i = 0; i < list.size(); i++) {
                    cat_id += list.get(i).getRec_id() + ",";
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isCheck()) {
                        cat_id += list.get(i).getRec_id() + ",";
                    }
                }
            }
            if (TextUtils.isEmpty(cat_id)) {
                toast("请选择您要删除的商品");
            } else {
                cat_id = cat_id.substring(0, cat_id.length() - 1);
                showDeleteDialog(cat_id);
            }
        } else {
            toast("您的购物车是空的");
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "购物车");
        if (Utils.isConnected(mActivity)) {
            presenter.getDataList(token);
        }

        shoppingCarListRv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider divider = new RecyclerViewDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        shoppingCarListRv.addItemDecoration(divider);
        adapter = new CommonAdapter<ShoppingCarList>(mActivity, R.layout.item_shopping_car, list) {
            @Override
            protected void convert(ViewHolder holder, final ShoppingCarList dataList, final int position) {
                final CheckBox item_car_check = holder.getView(R.id.item_car_check);
                ImageView item_car_img = holder.getView(R.id.item_car_img);
                //final TextView car_item_number_tv = holder.getView(R.id.car_item_number_tv);
                ImageView car_item_sub = holder.getView(R.id.car_item_sub);
                ImageView car_item_add = holder.getView(R.id.car_item_add);
                holder.setText(R.id.item_product_title, dataList.getGoods_name());
                holder.setText(R.id.item_car_price, dataList.getGoods_price());
                holder.setText(R.id.car_item_number_tv, dataList.getGoods_number() + "");

                RequestOptions options = new RequestOptions();
                options.error(R.drawable.error_pic);
                Glide.with(mActivity).load(dataList.getGoods_thumb()).apply(options).into(item_car_img);
                if (isAll) {
                    item_car_check.setChecked(true);
                    dataList.setCheck(true);
                    list.set(position, dataList);
                } else {
                    if (dataList.isCheck()) {
                        item_car_check.setChecked(true);
                    } else {
                        item_car_check.setChecked(false);
                    }
                }

                item_car_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean bl = item_car_check.isChecked();
                        dataList.setCheck(bl);
                        list.set(position, dataList);
                        boolean flag = true;
                        for (int i = 0; i < list.size(); i++) {
                            if (!list.get(i).isCheck()) {
                                flag = false;
                            }
                        }
                        isAll = false;
                        shoppingCarAllCheck.setChecked(flag);
                        subTotal();
                    }
                });
                //减1
                car_item_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = dataList.getGoods_number();
                        if (count > 1) {
                            presenter.updataCarNumber(token, dataList.getRec_id(), (count - 1) + "");
                        } else {
                            toast("已是最小数量");
                        }
                    }
                });
                //加1
                car_item_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = dataList.getGoods_number();
                        presenter.updataCarNumber(token, dataList.getRec_id(), (count + 1) + "");
                    }
                });
            }
        };
        shoppingCarListRv.setAdapter(adapter);


        shoppingCarAllCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAll = shoppingCarAllCheck.isChecked();
                L.e("tag", "--------------------->" + isAll);
                if (!isAll) {
                    for (int i = 0; i < list.size(); i++) {
                        ShoppingCarList dataList = list.get(i);
                        dataList.setCheck(false);
                        list.set(i, dataList);
                    }
                }
                adapter.notifyDataSetChanged();
                subTotal();
            }
        });
    }


    @Override
    public void successData(ShoppingCarData carData) {
        if (carData != null) {
            this.carData = carData;
            if (carData.getGoods_list() != null && carData.getGoods_list().size() > 0) {
                list.clear();
                list.addAll(carData.getGoods_list());
                adapter.notifyDataSetChanged();
            } else {
                list.clear();
                adapter.notifyDataSetChanged();
            }
            subTotal();
//            Map<String, String> map = carData.getTotal();
//            shoppingCarTotal.setText("合计：¥0.00");
//            if (map != null) {
//                shoppingCarTotal.setText("合计：" + map.get("goods_price"));
//            } else {
//                shoppingCarTotal.setText("合计：¥0.00");
//            }
        }
    }

    private void subTotal() {
        if (list.size() > 0) {
            isAll = shoppingCarAllCheck.isChecked();
            if (isAll) {
                Map<String, String> map = carData.getTotal();
                if (map != null) {
                    shoppingCarTotal.setText("合计：" + map.get("goods_price"));
                }
            } else {
                double price = 0.00;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isCheck()) {
                        price += list.get(i).getSubtotal();
                    }
                }
                shoppingCarTotal.setText("合计：¥" + String.format("%.2f", price) + "元");
            }
        } else {
            shoppingCarTotal.setText("合计：¥0.00元");
        }
    }


    private void showDeleteDialog(final String rec_id) {
        new BaseDialog(mActivity, R.layout.base_dialog_layout) {
            @Override
            protected void getMView(View view, final Dialog dialog) {
                TextView dialog_title_tv = view.findViewById(R.id.dialog_title_tv);
                TextView dialog_content_tv = view.findViewById(R.id.dialog_content_tv);
                TextView dialog_cancel_tv = view.findViewById(R.id.dialog_cancel_tv);
                TextView dialog_confirm_tv = view.findViewById(R.id.dialog_confirm_tv);

                dialog_title_tv.setText("提示");
                dialog_content_tv.setText("是否删除选中的商品？");
                dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog_confirm_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.delCarNumber(token, rec_id);
                    }
                });
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            // presenter.getDataList(token);
            setResult(10);
            finish();
        }
    }
}
