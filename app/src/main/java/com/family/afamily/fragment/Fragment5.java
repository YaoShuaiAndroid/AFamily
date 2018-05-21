package com.family.afamily.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.AddBabyActivity;
import com.family.afamily.activity.BabyChartActivity;
import com.family.afamily.activity.BorrowBookActivity;
import com.family.afamily.activity.CollectionActivity;
import com.family.afamily.activity.CouponListActivity;
import com.family.afamily.activity.FollowActivity;
import com.family.afamily.activity.IntegralActivity;
import com.family.afamily.activity.IntegralListActivity;
import com.family.afamily.activity.MsgActivity;
import com.family.afamily.activity.MyActionActivity;
import com.family.afamily.activity.MyAudioActivity;
import com.family.afamily.activity.MyCapacityActivity;
import com.family.afamily.activity.MyExtensionActivity;
import com.family.afamily.activity.MyFinanceActivity;
import com.family.afamily.activity.MyInfoActivity;
import com.family.afamily.activity.MyVideoActivity;
import com.family.afamily.activity.MyYYueActivity;
import com.family.afamily.activity.OpenMemberActivity;
import com.family.afamily.activity.OrderListActivity;
import com.family.afamily.activity.SettingActivity;
import com.family.afamily.activity.ShoppingCarActivity;
import com.family.afamily.activity.StudyRecordActivity;
import com.family.afamily.entity.UserInfoData;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.fragment.interfaces.Fragment5View;
import com.family.afamily.fragment.presenters.Fragment5Presenter;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Fragment5 extends BaseFragment<Fragment5Presenter> implements Fragment5View, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.frag5_user_head)
    ImageView frag5UserHead;
    @BindView(R.id.frag5_user_nick)
    TextView frag5UserNick;
    @BindView(R.id.frag5_head_bottom)
    LinearLayout frag5HeadBottom;
    @BindView(R.id.frag5_head_bg)
    RelativeLayout frag5HeadBg;
    @BindView(R.id.frag5_not_tv)
    TextView frag5NotTv;
    @BindView(R.id.frag5_not_open)
    RelativeLayout frag5NotOpen;
    @BindView(R.id.frag5_vip_tv)
    TextView frag5VipTv;
    @BindView(R.id.frag5_is_vip)
    RelativeLayout frag5IsVip;
    @BindView(R.id.frag5_my_scroll)
    MyScrollView frag5MyScroll;
    @BindView(R.id.frag5_title)
    LinearLayout frag5Title;
    @BindView(R.id.frag5_follow_count)
    TextView frag5FollowCount;
    @BindView(R.id.frag5_collection_count)
    TextView frag5CollectionCount;
    @BindView(R.id.frag5_integral_count)
    TextView frag5IntegralCount;
    @BindView(R.id.frag5_money_tl)
    TextView frag5MoneyTl;
    @BindView(R.id.frag5_member_data)
    TextView frag5MemberData;
    @BindView(R.id.frag5_member_money)
    TextView frag5MemberMoney;
    @BindView(R.id.frag5_member_over_tv)
    TextView frag5MemberOverTv;
    @BindView(R.id.frag5_service_tv)
    TextView frag5ServiceTv;
    @BindView(R.id.frag5_service_item)
    RelativeLayout frag5ServiceItem;
    @BindView(R.id.frag5_qr_code_iv)
    ImageView frag5QrCodeIv;
    @BindView(R.id.frag5_my_finance_item)
    RelativeLayout frag5MyFinanceItem;
    @BindView(R.id.frag5_open_vip)
    TextView frag5OpenVip;
    @BindView(R.id.frag5_user_car)
    TextView frag5UserCar;
    @BindView(R.id.frag5_user_end)
    TextView frag5UserEnd;
    Unbinder unbinder;
    Unbinder unbinder1;
    private Activity mActivity;
    private int alpha = 0;
    private String token;
    private UserInfoData userInfoData;
    private String have_child = "-1";
    private boolean isFrist = true;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment5_new_layout, null);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Utils.getStatusHeight(getActivity(), view.findViewById(R.id.frag5_title2));
        }
        unbinder = ButterKnife.bind(this, view);
        token = (String) SPUtils.get(mActivity, "token", "");
        setData();
        return view;
    }

    private void setData() {
//        if (Utils.isConnected(mActivity)) {
//            presenter.getUserinfoData(token, 1);
//        }
//        //设置背景为全透明
//        frag5Title.getBackground().setAlpha(alpha);
//         //measure的参数为0即可
//        frag5HeadBg.measure(0, 0);
//        //获取组件的高度
//        final int height = frag5HeadBg.getMeasuredHeight() - frag5Title.getMeasuredHeight();
//        //监听scrollview 滑动变化 ，实现标题透明的渐变效果
//        frag5MyScroll.setScrollViewChanged(new MyScrollView.ScrollViewChanged() {
//            @Override
//            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                try {
//                    float value = (float) 255 / height;
//                    float alp = (float) t * value;
//                    alpha = (int) (alp > 255 ? 255 : alp);
//                    frag5Title.getBackground().setAlpha(alpha);
//                } catch (Exception e) {
//                    alpha = 0;
//                    frag5Title.getBackground().setAlpha(alpha);
//                }
//            }
//        });
    }

    @OnClick({R.id.frag5_user_head, R.id.frag5_user_nick})
    public void clickHead() {
        //点击头像
        startActivity(new Intent(mActivity, MyInfoActivity.class));
    }

    @OnClick(R.id.frag5_setting_tv2)
    public void clickSetting() {
        //点击设置
        startActivity(new Intent(mActivity, SettingActivity.class));
    }

    @OnClick(R.id.frag5_msg_ic2)
    public void clickMsg() {
        //点击消息
        startActivity(new Intent(mActivity, MsgActivity.class));
    }

    @OnClick(R.id.frag5_follow_ll)
    public void clickFollow() {
        //点击关注
        startActivity(new Intent(mActivity, FollowActivity.class));
    }

    @OnClick(R.id.frag5_collection_ll)
    public void clickCollection() {
        //点击收藏
        startActivity(new Intent(mActivity, CollectionActivity.class));
    }

    @OnClick(R.id.frag5_integral_ll)
    public void clickIntegral() {
        //点击积分
        startActivity(new Intent(mActivity, IntegralListActivity.class));
    }

    @OnClick(R.id.frag5_money_ll)
    public void clickCash() {
        //点击现金
        //  startActivity(new Intent(mActivity, CashActivity.class));
        startActivity(new Intent(mActivity, CouponListActivity.class));
    }

    @OnClick(R.id.frag5_integral_tab)
    public void clickIntegralAreaTab() {
        //点击积分专区
        startActivity(new Intent(mActivity, IntegralActivity.class));

    }

    @OnClick(R.id.frag5_order_tab)
    public void clickOrderTab() {
        //点击我的订单
        startActivity(new Intent(mActivity, OrderListActivity.class));
    }

    @OnClick(R.id.frag5_shopping_car_tab)
    public void clickShoppingCarTab() {
        //点击购车
        startActivity(new Intent(mActivity, ShoppingCarActivity.class));
    }


    @OnClick(R.id.frag5_coupon_tab)
    public void clickCouponTab() {
        //点击优惠劵
        startActivity(new Intent(mActivity, CouponListActivity.class));
    }

    @OnClick(R.id.frag5_baby_record_item)
    public void clickBabyRecord() {
        //点击宝宝成长
        if (have_child.equals("-1")) {
            toast("未获取到用户信息");
        } else if (have_child.equals("Y")) {
            startActivity(new Intent(mActivity, BabyChartActivity.class));
            //startActivity(new Intent(mActivity, BabyDetailsActivity.class));
        } else {
            startActivity(new Intent(mActivity, AddBabyActivity.class));
        }
    }

    @OnClick(R.id.frag5_my_finance_item)
    public void clickFinanceItem() {
        //点击我的财务
        startActivity(new Intent(mActivity, MyFinanceActivity.class));
    }

    @OnClick(R.id.frag5_study_record_item)
    public void clickStudyRecordItem() {
        //点击学习记录
        startActivity(new Intent(mActivity, StudyRecordActivity.class));
    }

    @OnClick(R.id.frag5_my_yuyue_item)
    public void clickYuYueItem() {
        //点击我的预约
        startActivity(new Intent(mActivity, MyYYueActivity.class));
    }

    @OnClick(R.id.frag5_jieyue_item)
    public void clickJieYueItem() {
        //点击我的借阅
        startActivity(new Intent(mActivity, BorrowBookActivity.class));
    }

    @OnClick(R.id.frag5_video_item)
    public void clickVideoItem() {
        //点击我的视频
        startActivity(new Intent(mActivity, MyVideoActivity.class));
    }

    @OnClick(R.id.frag5_audio_item)
    public void clickAudioItem() {
        //点击我的音频
        startActivity(new Intent(mActivity, MyAudioActivity.class));
    }

    @OnClick(R.id.frag5_my_action_item)
    public void clickActionItem() {
        //点击我的活动
        startActivity(new Intent(mActivity, MyActionActivity.class));
    }

    @OnClick(R.id.frag5_my_zhineng_item)
    public void onViewClicked() {
        //我的先天智能
        startActivity(new Intent(mActivity, MyCapacityActivity.class));
    }

    @OnClick(R.id.frag5_share_item)
    public void clickExtensionItem() {
        //点击我的推广
        startActivity(new Intent(mActivity, MyExtensionActivity.class));
    }

    @OnClick({R.id.frag5_member_over_tv, R.id.frag5_open_vip})
    public void clickOpenVip() {
        //点击去续费
        startActivity(new Intent(mActivity, OpenMemberActivity.class));
    }

    @OnClick(R.id.frag5_service_item)
    public void clickServiceItem() {
        if (userInfoData != null) {
            String call_tv = userInfoData.getContact_tel();
            if (TextUtils.isEmpty(call_tv)) {
                toast("服务电话错误");
            } else {
                if (verifyCallPermissions()) {
                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call_tv));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




    @Override
    public void onResume() {
        super.onResume();
        if(!isHidden()) {
            if(!TextUtils.isEmpty(token)) {
                presenter.getUserinfoData(token, 2);
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(!TextUtils.isEmpty(token)) {
                presenter.getUserinfoData(token, 2);
            }
        }
    }

    @Override
    public Fragment5Presenter initPresenter() {
        return new Fragment5Presenter(this);
    }



    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#AB6F08"));
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    @Override
    public void successData(UserInfoData data) {
        if (data != null) {
            userInfoData = data;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.tx)
                    .error(R.mipmap.tx)
                    .transform(new GlideCircleTransform(mActivity));
            Glide.with(mActivity).load(data.getImages()).apply(options).into(frag5UserHead);

            RequestOptions options2 = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.error_pic);
            Glide.with(mActivity).load(data.getQr_code()).apply(options2).into(frag5QrCodeIv);

//            if(!TextUtils.isEmpty(data.getUser_grade())){
//                frag5UserCar.setVisibility(View.VISIBLE);
//                frag5UserCar.setText(data.getUser_grade());
//            }
//
//            if(!TextUtils.isEmpty(data.getGrade_end_time())){
//                frag5UserEnd.setVisibility(View.VISIBLE);
//                frag5UserEnd.setText(data.getGrade_end_time()+"到期");
//            }


            frag5UserNick.setText(data.getNick_name());
            frag5FollowCount.setText(data.getSubscribe_count());
            frag5CollectionCount.setText(data.getCollect_count());
            frag5IntegralCount.setText(data.getPay_points());
            // frag5MoneyTl.setText(data.getUser_money());//现金
            frag5MoneyTl.setText(data.getUser_bonus_count());//优惠劵
            frag5ServiceTv.setText(data.getContact_tel());

            have_child = data.getHave_child();
            if (!TextUtils.isEmpty(data.getShare()) && data.getShare().equals("1")) {
                frag5MyFinanceItem.setVisibility(View.VISIBLE);
            } else {
                frag5MyFinanceItem.setVisibility(View.GONE);
            }
            if (data.getIs_year_card().equals("0")) {
                frag5NotOpen.setVisibility(View.VISIBLE);
                frag5IsVip.setVisibility(View.GONE);
                frag5MemberOverTv.setVisibility(View.GONE);
            } else {
                frag5NotOpen.setVisibility(View.GONE);
                frag5IsVip.setVisibility(View.VISIBLE);

                frag5MemberMoney.setText(data.getYear_card_money());
                frag5MemberData.setText(data.getYear_card_start_time() + "--" + data.getYear_card_end_time());

                if (data.getYear_card_guoqi().equals("1")) {
                    frag5MemberOverTv.setVisibility(View.VISIBLE);
                    String str = "您的会员卡于" + data.getYear_card_end_time() + "已过期，去续费";
                    SpannableStringBuilder style = new SpannableStringBuilder(str);
                    style.setSpan(new Clickable(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, OpenMemberActivity.class);
                            startActivity(intent);
                        }
                    }), str.length() - 3, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    frag5MemberOverTv.setText(style);
                    frag5MemberOverTv.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    if (data.getYear_card_xufei().equals("1")) {
                        frag5MemberOverTv.setVisibility(View.VISIBLE);
                        String str = "您的会员卡于" + data.getYear_card_end_time() + "到期，去续费";
                        SpannableStringBuilder style = new SpannableStringBuilder(str);
                        style.setSpan(new Clickable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mActivity, OpenMemberActivity.class);
                                startActivity(intent);
                            }
                        }), str.length() - 3, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        frag5MemberOverTv.setText(style);
                        frag5MemberOverTv.setMovementMethod(LinkMovementMethod.getInstance());

                    } else {
                        frag5MemberOverTv.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private boolean verifyCallPermissions() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(mActivity, perms)) {
            EasyPermissions.requestPermissions(this, "本次操作需要打电话权限", 1, perms);
            return false;
        }
        return true;
    }
}
