package com.family.afamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.MyApplication;
import com.family.afamily.R;
import com.family.afamily.activity.AllWebViewActivity;
import com.family.afamily.activity.EvaluateActivity;
import com.family.afamily.activity.MyAudioActivity;
import com.family.afamily.activity.PayOrderActivity;
import com.family.afamily.activity.mvp.presents.OrderListPresenter;
import com.family.afamily.entity.AudioData;
import com.family.afamily.entity.OrderListChildData;
import com.family.afamily.entity.OrderListData;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.upload_db.AudioDao;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/10.
 */

public class OrderListAdapter extends SuperBaseAdapter<OrderListData> {
    private CommonAdapter<OrderListChildData> adapter;
    private Context context;
    private String token;
    private OrderListPresenter presenter;
    private MyApplication myApplication;
    private AudioDao audioDao;
    private String user_id;

    public OrderListPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(OrderListPresenter presenter) {
        this.presenter = presenter;
    }

    public OrderListAdapter(Context context, List<OrderListData> data) {
        super(context, data);
        this.context = context;
        token = (String) SPUtils.get(context, "token", "");
        myApplication = (MyApplication) context.getApplicationContext();
        audioDao = new AudioDao(context);
        user_id = (String) SPUtils.get(context, "user_id", "");
    }

    @Override
    protected void convert(BaseViewHolder holder, final OrderListData item, int position) {
        RecyclerView item_order_child_list = holder.getView(R.id.item_order_child_list);
        TextView item_order_comm_btn = holder.getView(R.id.item_order_comm_btn);
        TextView item_order_look_btn = holder.getView(R.id.item_order_look_btn);
        TextView item_order_exit_btn = holder.getView(R.id.item_order_exit_btn);
        TextView item_order_pay_btn = holder.getView(R.id.item_order_pay_btn);
        TextView item_order_cd_btn = holder.getView(R.id.item_order_cd_btn);
        TextView item_order_status_tv = holder.getView(R.id.item_order_status_tv);
        TextView item_order_cancel_btn = holder.getView(R.id.item_order_cancel_btn);
        TextView item_order_ok_btn = holder.getView(R.id.item_order_ok_btn);

        holder.setText(R.id.item_order_sn, "订单编号：" + item.getOrder_sn());
        holder.setText(R.id.item_order_time, item.getOrder_time());
        holder.setText(R.id.item_order_total_price, item.getTotal_fee());
        //1,待付款，2.待发货，3.已发货，4，已完成，5，已评价，6.已退货
        item_order_child_list.setNestedScrollingEnabled(false);
        //item_order_child_list.set

        final String status = item.getOrder_status();
        if (status.equals("1")) {
            item_order_cd_btn.setVisibility(View.GONE);
            item_order_comm_btn.setVisibility(View.GONE);
            item_order_look_btn.setVisibility(View.GONE);
            item_order_exit_btn.setVisibility(View.GONE);
            item_order_ok_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.VISIBLE);
            item_order_cancel_btn.setSelected(true);
            item_order_pay_btn.setVisibility(View.VISIBLE);
            item_order_pay_btn.setSelected(true);
            item_order_status_tv.setText("待付款");
        } else if (status.equals("2")) {
            item_order_cd_btn.setVisibility(View.VISIBLE);
            item_order_cd_btn.setSelected(true);
            item_order_comm_btn.setVisibility(View.GONE);
            item_order_look_btn.setVisibility(View.GONE);
            item_order_exit_btn.setVisibility(View.GONE);
            item_order_ok_btn.setVisibility(View.GONE);
            item_order_pay_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.GONE);
            item_order_status_tv.setText("待发货");
        } else if (status.equals("3")) {
            item_order_cd_btn.setVisibility(View.GONE);
            item_order_comm_btn.setVisibility(View.GONE);
            item_order_look_btn.setVisibility(View.VISIBLE);
            item_order_ok_btn.setVisibility(View.VISIBLE);
            item_order_ok_btn.setSelected(true);
            item_order_look_btn.setSelected(true);
            item_order_exit_btn.setVisibility(View.GONE);
            item_order_pay_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.GONE);
            item_order_status_tv.setText("已发货");
        } else if (status.equals("4")) {
            item_order_cd_btn.setVisibility(View.GONE);
            item_order_ok_btn.setVisibility(View.GONE);
            item_order_comm_btn.setVisibility(View.VISIBLE);
            item_order_comm_btn.setSelected(true);
            item_order_look_btn.setVisibility(View.VISIBLE);
            item_order_look_btn.setSelected(true);
            item_order_exit_btn.setVisibility(View.VISIBLE);
            item_order_exit_btn.setSelected(true);
            item_order_pay_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.GONE);
            item_order_status_tv.setText("已完成");
        } else if (status.equals("5")) {
            item_order_cd_btn.setVisibility(View.GONE);
            item_order_comm_btn.setVisibility(View.GONE);
            item_order_look_btn.setVisibility(View.VISIBLE);
            item_order_ok_btn.setVisibility(View.GONE);
            item_order_look_btn.setSelected(true);
            item_order_exit_btn.setVisibility(View.VISIBLE);
            item_order_exit_btn.setSelected(true);
            item_order_pay_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.GONE);
            item_order_status_tv.setText("已完成");
        } else if (status.equals("6")) {
            item_order_cd_btn.setVisibility(View.GONE);
            item_order_ok_btn.setVisibility(View.GONE);
            item_order_comm_btn.setVisibility(View.GONE);
            item_order_look_btn.setVisibility(View.GONE);
            item_order_exit_btn.setVisibility(View.GONE);
            item_order_pay_btn.setVisibility(View.GONE);
            item_order_cancel_btn.setVisibility(View.GONE);
            item_order_status_tv.setText("已退货");
        }
        //支付订单
        item_order_pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayOrderActivity.class);
                intent.putExtra("order_sn", item.getOrder_sn());
                intent.putExtra("order_money", item.getTotal_fee_new());
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });
        //取消订单
        item_order_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showDialog(context, token, item.getOrder_id() + "", 1);
            }
        });
        //催单
        item_order_cd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitReminder(token, item.getOrder_id() + "");
            }
        });
        //查看物流
        item_order_look_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "查看物流");
                bundle.putString("link", item.getWuliu_url());
                L.e("Tag",item.getWuliu_url() +"---->");
                Intent intent = new Intent(context, AllWebViewActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //确认收货
        item_order_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showDialog(context, token, item.getOrder_id() + "", 3);
            }
        });
        //申请退货
        item_order_exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showDialog(context, token, item.getOrder_id() + "", 2);
            }
        });
        //评论
        item_order_comm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApplication.setListChildDatas(item.getGoods_list());
                Intent intent = new Intent(context, EvaluateActivity.class);
                intent.putExtra("order_id", item.getOrder_id() + "");
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });

        item_order_child_list.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewDivider divider = new RecyclerViewDivider(context, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        item_order_child_list.addItemDecoration(divider);
        adapter = new CommonAdapter<OrderListChildData>(context, R.layout.item_order_child_layout, item.getGoods_list()) {
            @Override
            protected void convert(ViewHolder holder, final OrderListChildData data, int position) {
                ImageView item_order_img = holder.getView(R.id.item_order_img);
                LinearLayout item_order_download = holder.getView(R.id.item_order_download);
                TextView item_order_back_jf = holder.getView(R.id.item_order_back_jf);

                if (status.equals("1")) {
                    item_order_download.setVisibility(View.GONE);
                    item_order_back_jf.setVisibility(View.VISIBLE);
                    item_order_back_jf.setText("购买返" + data.getGive_integral() + "积分");
                } else if (status.equals("2")) {
                    item_order_download.setVisibility(View.GONE);
                    item_order_back_jf.setVisibility(View.GONE);
                } else if (status.equals("3")) {
                    item_order_download.setVisibility(View.GONE);
                    item_order_back_jf.setVisibility(View.GONE);
                } else if (status.equals("4")) {
                    if (TextUtils.isEmpty(data.getAudio())) {
                        item_order_download.setVisibility(View.GONE);
                    } else {
                        item_order_download.setVisibility(View.VISIBLE);
                    }
                    item_order_back_jf.setVisibility(View.GONE);
                } else if (status.equals("5")) {
                    if (TextUtils.isEmpty(data.getAudio())) {
                        item_order_download.setVisibility(View.GONE);
                    } else {
                        item_order_download.setVisibility(View.VISIBLE);
                    }
                    item_order_back_jf.setVisibility(View.GONE);
                } else if (status.equals("6")) {
                    item_order_download.setVisibility(View.GONE);
                    item_order_back_jf.setVisibility(View.GONE);
                }

                RequestOptions options2 = new RequestOptions();
                options2.error(R.drawable.error_pic);
                Glide.with(context).load(data.getGoods_thumb()).apply(options2).into(item_order_img);

                holder.setText(R.id.item_order_price, "¥" + data.getGoods_price());
                holder.setText(R.id.item_order_name, data.getGoods_name());
                holder.setText(R.id.item_order_count, "x" + data.getGoods_number());
                //下载音频
                item_order_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AudioData audioData = audioDao.selectData(user_id, item.getOrder_sn()+data.getGoods_id());
                        if (audioData == null) {
                            AudioData mData = new AudioData();
                            mData.setCurrentSize(0);
                            mData.setDownloadFlag(0);
                            mData.setImg(data.getGoods_thumb());
                            mData.setAudioDownload(data.getAudio());
                            mData.setAudio_name(data.getGoods_name());
                            mData.setOrder_sn(item.getOrder_sn()+data.getGoods_id());
                            mData.setTotalSize(0);
                            mData.setUser_id(user_id);
                            audioDao.insertDB(mData);
                        }
                        Intent intent = new Intent(mContext, MyAudioActivity.class);
                        mContext.startActivity(intent);
                    }
                });

            }
        };
        item_order_child_list.setAdapter(adapter);
    }

    @Override
    protected int getItemViewLayoutId(int position, OrderListData item) {
        return R.layout.item_order_list_layout;
    }
}
