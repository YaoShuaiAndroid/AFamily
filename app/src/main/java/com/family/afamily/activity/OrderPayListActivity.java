package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.OrderPayListView;
import com.family.afamily.activity.mvp.presents.OrderPayListPresenter;
import com.family.afamily.adapters.OrderPayAdapter;
import com.family.afamily.entity.OrderPayData;
import com.family.afamily.entity.ShoppingCarList;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/11.
 */

public class OrderPayListActivity extends BaseActivity<OrderPayListPresenter> implements OrderPayListView {
    @BindView(R.id.order_pay_address_ic)
    ImageView orderPayAddressIc;
    @BindView(R.id.order_pay_tv)
    TextView orderPayTv;
    @BindView(R.id.order_pay_name)
    TextView orderPayName;
    @BindView(R.id.order_pay_mobile)
    TextView orderPayMobile;
    @BindView(R.id.order_pay_address)
    TextView orderPayAddress;
    @BindView(R.id.order_pay_list_lv)
    MyListView orderPayListLv;
    @BindView(R.id.order_pay_money_ic)
    ImageView orderPayMoneyIc;
    @BindView(R.id.order_pay_money_t)
    TextView orderPayMoneyT;
    @BindView(R.id.order_pay_money_tv)
    TextView orderPayMoneyTv;
    @BindView(R.id.order_pay_member_ic)
    ImageView orderPayMemberIc;
    @BindView(R.id.order_pay_member_t)
    TextView orderPayMemberT;
    @BindView(R.id.order_pay_member_tv)
    TextView orderPayMemberTv;
    @BindView(R.id.order_pay_juan_ic)
    ImageView orderPayJuanIc;
    @BindView(R.id.order_pay_juan_t)
    TextView orderPayJuanT;
    @BindView(R.id.order_pay_juan_tv)
    TextView orderPayJuanTv;
    @BindView(R.id.shopping_car_pay)
    TextView shoppingCarPay;
    @BindView(R.id.pay_bottom)
    LinearLayout payBottom;
    @BindView(R.id.order_pay_select_address)
    RelativeLayout orderPaySelectAddress;
    @BindView(R.id.order_pay_address_rl)
    RelativeLayout orderPayAddressRl;
    @BindView(R.id.order_pay_back_integral)
    TextView orderPayBackIntegral;
    @BindView(R.id.order_pay_total_tv)
    TextView orderPayTotalTv;
    @BindView(R.id.order_year_rl)
    RelativeLayout orderYearRl;
    @BindView(R.id.order_pay_mjq_rl)
    RelativeLayout orderPayMjqRl;//满减券
    @BindView(R.id.order_pay_member_open_c)
    ImageView orderPayMemberOpenC;
    @BindView(R.id.order_pay_money_open_c)
    ImageView orderPayMoneyOpenC;

    private double totalPrice = 0.00; //原始金额
    private double afterPrice = 0.00; //实付金额
    private double yearNeedMoney = 0.00;//年卡使用金额
    private double voucherPrice = 0.00; //满减金额
    private double voucher_price = 0.00;//满减实际金额
    private double balancePrice = 0.00; //使用余额金额

    private List<ShoppingCarList> list = new ArrayList<>();
    //书房
    private List<ShoppingCarList> bookList = new ArrayList<>();

    private OrderPayAdapter adapter;
    private String rec_id;
    private String token;
    private OrderPayData payData;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_pay_list);
        rec_id = getIntent().getStringExtra("cat_id");
        token = (String) SPUtils.get(mActivity, "token", "");
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public OrderPayListPresenter initPresenter() {
        return new OrderPayListPresenter(this);
    }

    @OnClick({R.id.order_pay_address_rl, R.id.order_pay_select_address})
    public void clickAddress() {
        Intent intent = new Intent(mActivity, AddressListActivity.class);
        intent.putExtra("isSelect", true);
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.order_pay_member_open_c)
    public void clickYearCar() {
        int flag = (int) orderPayMemberOpenC.getTag();
        if (flag == 0) {
            //使用年卡
            orderPayMemberOpenC.setImageResource(R.mipmap.ic_open);
            orderPayMemberOpenC.setTag(1);
            afterYearCar(1, 1);
        } else {
            //不用年卡
            orderPayMemberOpenC.setImageResource(R.mipmap.ic_close_p);
            orderPayMemberOpenC.setTag(0);
            afterYearCar(0, 1);
        }
    }

    @OnClick(R.id.order_pay_money_open_c)
    public void clickMoney() {
        int flag = (int) orderPayMoneyOpenC.getTag();
        if (flag == 0) {
            //余额
            orderPayMoneyOpenC.setImageResource(R.mipmap.ic_open);
            orderPayMoneyOpenC.setTag(1);
        } else {
            //不用余额
            orderPayMoneyOpenC.setImageResource(R.mipmap.ic_close_p);
            orderPayMoneyOpenC.setTag(0);
        }

        afterMoney();
    }

    @OnClick(R.id.order_pay_mjq_rl)
    public void clickVoucher() {
        startActivityForResult(VoucherActivity.class, 90);
    }

    @OnClick(R.id.shopping_car_pay)
    public void submitOrder() {
        String address_id = (String) orderPayName.getTag();
        String year_id = "";
        String juan_id = (String) orderPayJuanTv.getTag();
        if (TextUtils.isEmpty(juan_id)) {
            juan_id = "";
        }
        if (yearNeedMoney > 0) {
            year_id = (String) orderPayMemberTv.getTag();
        }
        L.e("tag", year_id + "-------------年卡使用额度---------------->" + yearNeedMoney);
        if (TextUtils.isEmpty(address_id)) {
            toast("请选择收货地址");
        } else {
            presenter.submitNext(token, address_id, balancePrice + "", year_id, juan_id, yearNeedMoney + "");
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "订单结算");
        adapter = new OrderPayAdapter(mActivity, list);
        orderPayListLv.setAdapter(adapter);
        orderPayMemberOpenC.setTag(0);
        orderPayMoneyOpenC.setTag(1);
        if (Utils.isConnected(mActivity)) {
            presenter.submitOrder(token, rec_id);
        }
    }

    /**
     * 使用年卡后
     *
     * @param type 1是用，0是不用
     */
    private void afterYearCar(int type, int flag) {
        if (bookList == null || bookList.size() == 0) {
            afterPrice = totalPrice;
            orderPayTotalTv.setText("实付款：¥" + String.format("%.2f", afterPrice));
        } else {
            Map<String, String> userInfo = payData.getUser_info();
            if (userInfo != null) {
                String card = userInfo.get("user_year_card");
                double card_money = Double.parseDouble(card);
                //年卡还有余额
                if (card_money > 0) {
                    if (type == 1) {
                        double bookMoney = 0;
                        for (int i = 0; i < bookList.size(); i++) {
                            bookMoney += bookList.get(i).getSubtotal();
                        }
                        //年卡余额大于书本金额，全部抵扣
                        if (card_money > bookMoney) {
                            afterPrice = totalPrice - bookMoney;
                            yearNeedMoney = bookMoney;
                        } else {
                            //年卡余额小于书本金额，年卡余额全部抵扣
                            afterPrice = totalPrice - card_money;
                            yearNeedMoney = card_money;
                        }

                        if (voucher_price > 0) {
                            if (voucher_price < afterPrice) {
                                afterPrice = afterPrice - voucher_price;
                                voucherPrice = voucher_price;
                            } else {
                                //优惠劵大于剩余金额
                                voucherPrice = afterPrice;
                                afterPrice = 0.00;
                            }
                        }

                    } else {
                        //不用年卡
                        afterPrice = totalPrice;
                        yearNeedMoney = 0.00;
                        if (voucher_price > 0) {
                            if (voucher_price < totalPrice) {
                                afterPrice = totalPrice - voucher_price;
                                voucherPrice = voucher_price;
                            } else {
                                //优惠劵大于剩余金额
                                voucherPrice = totalPrice;
                                afterPrice = 0.00;
                            }
                        }
                    }

                    L.e("tag", "-------------年卡使用额度---------------->" + yearNeedMoney);
                    orderPayTotalTv.setText("实付款：¥" + String.format("%.2f", afterPrice));
                    if (flag == 1) {
                        afterMoney();
                    }
                }
            }
        }
    }

    /**
     * 使用余额后
     */
    private void afterMoney() {
        int type = (int) orderPayMoneyOpenC.getTag();
        Map<String, String> userInfo = payData.getUser_info();
        if (userInfo != null) {
            String str = userInfo.get("user_money");
            double money = Double.parseDouble(str);
            if (money > 0) {
                if (type == 1) {
                    if (money > afterPrice) {
                        balancePrice = afterPrice;
                        afterPrice = 0.00;
                    } else {
                        afterPrice = afterPrice - money;
                        balancePrice = money;
                    }
                } else {
                    balancePrice = 0.00;
                    int flag = (int) orderPayMemberOpenC.getTag();
                    afterYearCar(flag, 0);
                }
                orderPayTotalTv.setText("实付款：¥" + String.format("%.2f", afterPrice));
            }
        }
    }

    @Override
    public void successData(OrderPayData payData) {
        if (payData != null) {
            this.payData = payData;
//            ---------------------地址-------------------------
            Map<String, String> address = payData.getUser_address();
            if (address != null) {
                orderPayName.setText(address.get("consignee"));
                orderPayMobile.setText(address.get("tel"));
                orderPayAddress.setText(address.get("province") + address.get("city") + address.get("district") + address.get("address"));
                orderPayName.setTag(address.get("address_id"));
                orderPaySelectAddress.setVisibility(View.GONE);
                orderPayAddressRl.setVisibility(View.VISIBLE);
            } else {
                orderPaySelectAddress.setVisibility(View.VISIBLE);
                orderPayAddressRl.setVisibility(View.GONE);
            }
//            ---------------------购物车集合-------------------------
            List<ShoppingCarList> dataList = payData.getGoods_list();
            if (dataList != null && dataList.size() > 0) {
                list.clear();
                list.addAll(dataList);
                for (int i = 0; i < dataList.size(); i++) {
                    String str = dataList.get(i).getLeixing();
                    if (!TextUtils.isEmpty(str) && str.equals("书房")) {
                        bookList.add(dataList.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                list.clear();
                adapter.notifyDataSetChanged();
            }
//            ---------------------总价-------------------------
            Map<String, String> total = payData.getTotal();
            if (total != null) {
                orderPayBackIntegral.setText("购买返" + total.get("will_get_integral") + "积分");
                orderPayTotalTv.setText("实付款：¥" + total.get("amount"));
                totalPrice = Double.parseDouble(total.get("amount"));
                afterPrice = totalPrice;
            }
//            ---------------------用户信息-------------------------
            Map<String, String> userInfo = payData.getUser_info();
            if (userInfo != null) {
                //余额
                double money = Double.parseDouble(userInfo.get("user_money"));
                if (money > 0) {
                    orderPayMoneyTv.setText("共" + money + "元");
                } else {
                    orderPayMoneyTv.setText("共0.00元");
                }
                if (bookList != null && bookList.size() > 0) {
                    //年卡
                    String user_year_card_id = userInfo.get("user_year_card_id");
                    if (TextUtils.isEmpty(user_year_card_id)) {
                        orderYearRl.setVisibility(View.GONE);
                    } else {
                        orderYearRl.setVisibility(View.VISIBLE);
                        orderPayMemberTv.setTag(user_year_card_id);
                        orderPayMemberTv.setText("共" + userInfo.get("user_year_card") + "元");
                    }
                } else {
                    orderYearRl.setVisibility(View.GONE);
                }
            }
//            ---------------------满减劵-------------------------
            List<Map<String, String>> user_bonus = payData.getUser_bonus();
            if (user_bonus != null && user_bonus.size() > 0) {
                myApplication.setUser_bonus(user_bonus);
                orderPayMjqRl.setVisibility(View.VISIBLE);
                orderPayJuanTv.setText("有可用优惠券");
            } else {
                orderPayMjqRl.setVisibility(View.GONE);
            }

            afterMoney();
        }
    }

    @Override
    public void submitSuccess(Map<String, String> data) {
        if (data != null) {
            String str = data.get("order_amount");
            double money = Double.parseDouble(str);
            if (money > 0) {
                Intent intent = new Intent(mActivity, PayOrderActivity.class);
                intent.putExtra("order_sn", data.get("order_sn"));
                intent.putExtra("order_money", data.get("order_amount"));
                startActivityForResult(intent, 101);
            } else {
                toast("订单支付完成");
                setResult(100);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApplication.setUser_bonus(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            if (data != null) {
                orderPayName.setText(data.getStringExtra("name"));
                orderPayMobile.setText(data.getStringExtra("phone"));
                orderPayAddress.setText(data.getStringExtra("address"));
                orderPayName.setTag(data.getStringExtra("address_id"));
                orderPaySelectAddress.setVisibility(View.GONE);
                orderPayAddressRl.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == 90 && resultCode == 90) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                List<Map<String, String>> user_bonus = payData.getUser_bonus();
                if (user_bonus != null && user_bonus.size() > position) {
                    Map<String, String> map = user_bonus.get(position);
                    orderPayJuanTv.setText("满" + map.get("min_goods_amount") + "减" + map.get("type_money"));
                    orderPayJuanTv.setTag(map.get("bonus_id"));
                    String str = map.get("type_money");
                    //优惠劵金额
                    voucher_price = Double.parseDouble(str);
                    //已使用会员年卡
                    if (yearNeedMoney > 0) {
                        afterPrice = totalPrice - yearNeedMoney;
                        if (voucher_price < afterPrice) {
                            afterPrice = afterPrice - voucher_price;
                            voucherPrice = voucher_price;
                        } else {
                            //优惠劵大于剩余金额
                            voucherPrice = afterPrice;
                            afterPrice = 0.00;
                        }
                    } else {
                        //还没使用年卡
                        if (voucher_price < totalPrice) {
                            afterPrice = totalPrice - voucher_price;
                            voucherPrice = voucher_price;
                        } else {
                            //优惠劵大于剩余金额
                            voucherPrice = totalPrice;
                            afterPrice = 0.00;
                        }
                    }
                    orderPayTotalTv.setText("实付款：¥" + String.format("%.2f", afterPrice));
                    afterMoney();
                }
            }
        } else if (requestCode == 101 && resultCode == 100) {
            setResult(100);
            finish();
        }
    }
}
