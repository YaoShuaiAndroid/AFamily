package com.family.afamily.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.family.afamily.MyApplication;
import com.family.afamily.R;
import com.family.afamily.activity.AllWebViewActivity;
import com.family.afamily.activity.CityListActivity;
import com.family.afamily.activity.CouponActivity;
import com.family.afamily.activity.EverydayTextActivity;
import com.family.afamily.activity.EverydayTextDetailsActivity;
import com.family.afamily.activity.LoginActivity;
import com.family.afamily.activity.MyYYueActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.activity.WaterAndSandDetailsActivity;
import com.family.afamily.activity.YuYueActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.adapters.Frag1Adapter;
import com.family.afamily.adapters.Frag1GalleryAdapter;
import com.family.afamily.adapters.Frag1SDAdapter;
import com.family.afamily.entity.Frag1SignData;
import com.family.afamily.entity.HomeData;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.fragment.interfaces.Fragment1View;
import com.family.afamily.fragment.presenters.Fragment1Presenter;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.ScaleTransformer;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Fragment1 extends BaseFragment<Fragment1Presenter> implements Fragment1View, BDLocationListener,EasyPermissions.PermissionCallbacks {

    @BindView(R.id.frag1_banner)
    Banner frag1Banner;
    @BindView(R.id.main_recycle1)
    RecyclerView mMainRecycle1;
    @BindView(R.id.frag1_yc_list_lv)
    MyListView frag1YcListLv;
    @BindView(R.id.frag1_sd_list_lv)
    MyListView frag1SdListLv;
    @BindView(R.id.frag1_yhj_list_rv)
    RecyclerView frag1YhjListRv;
    @BindView(R.id.frag1_sys_title)
    LinearLayout frag1SysTitle;
    @BindView(R.id.frag1_sign_red_tip)
    TextView frag1SignRedTip;
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.frag1_gallery_ll)
    LinearLayout frag1GalleryLl;
    @BindView(R.id.frag1_wdyy_ll)
    LinearLayout frag1WdyyLl;
    @BindView(R.id.frag1_city_tv)
    TextView frag1CityTv;
    @BindView(R.id.item_yyue_time)
    TextView itemYyueTime;
    @BindView(R.id.item_yyue_name)
    TextView itemYyueName;
    @BindView(R.id.item_yyue_type)
    TextView itemYyueType;
    @BindView(R.id.item_yyue_address)
    TextView itemYyueAddress;
    @BindView(R.id.frag1_yhj_title)
    LinearLayout frag1YhjTitle;
    Unbinder unbinder;
    private Frag1Adapter adapter;
    private Frag1SDAdapter sdAdapter;
    private CommonAdapter<Map<String, String>> commonAdapter;
    private List<Map<String, String>> couponList = new ArrayList<>();
    private List<Map<String, String>> galleryList = new ArrayList<>();

    private List<Map<String, String>> poolList = new ArrayList<>();
    private List<Map<String, String>> sandlList = new ArrayList<>();

    private List<String> images = new ArrayList<>();
    private Activity mActivity;
    private Frag1SignData signData;
    private Frag1GalleryAdapter demoAdapter1;
    private HomeData homeData;
    private String token;
    private MyApplication myApplication;
    private LocationClient mLocClient;//定位类
    private LocationClientOption option;
    //申请权限
    private static String[] PERMISSIONS_STORAGE = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Utils.getStatusHeight(getActivity(), view.findViewById(R.id.base_fragment_title));
        }
        myApplication = (MyApplication) getActivity().getApplication();
        unbinder = ButterKnife.bind(this, view);
        setData();
        verifyStoragePermissions();
        mLocation();
        return view;
    }

    //开启定位
    private void mLocation() {
        mLocClient = new LocationClient(mActivity);
        mLocClient.registerLocationListener(this);
        option = new LocationClientOption();
        option.setOpenGps(true);//打开GPS
        option.setCoorType("bd09ll"); //coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(10000);//单位毫秒，当<1000(1s)时，定时定位无效
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //   L.e("tag",bdLocation +"--------------定位结果---------------->");
        if (bdLocation != null) {
            stopLocation();
            if(frag1CityTv!=null) {
                frag1CityTv.setText(bdLocation.getCity());
                myApplication.setCity(bdLocation.getCity());
            }

        }
    }

    /**
     * 关闭定位
     */
    public void stopLocation() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
    }


    @OnClick(R.id.frag1_city_tv)
    public void clickCity() {
        startActivityForResult(new Intent(mActivity, CityListActivity.class), 100);
    }

    @OnClick(R.id.base_title_right_tv)
    public void clickSign() {
        if (signData != null) {
            presenter.showSignDialog(mActivity, signData, frag1SignRedTip);
        } else {
            toast("未获取到数据，正在重新获取...");
            String token = (String) SPUtils.get(mActivity, "token", "");
            presenter.getSignData(token, frag1SignRedTip, 0);
        }
    }

    @OnClick(R.id.frag1_yc_item_ll)
    public void clickYCItem() {

    }

    private void setData() {
        if (Utils.isConnected(mActivity)) {
            token = (String) SPUtils.get(mActivity, "token", "");
            if(TextUtils.isEmpty(token)){
                presenter.getHomeData(token);
            }else{
                presenter.checkMember(token);
                presenter.getSignData(token, frag1SignRedTip, 0);
                presenter.getHomeData(token);
            }


        }

        adapter = new Frag1Adapter(mActivity, poolList);
        frag1YcListLv.setAdapter(adapter);
        adapter.setOnclickItem(new Frag1Adapter.OnclickItem() {
            @Override
            public void onClickItem() {
                startActivityForResult(new Intent(getActivity(), YuYueActivity.class), 101);
            }
        });

        frag1YcListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WaterAndSandDetailsActivity.class);
                intent.putExtra("id", poolList.get(position).get("id"));
                startActivityForResult(intent, 101);
            }
        });

        sdAdapter = new Frag1SDAdapter(mActivity, sandlList);
        frag1SdListLv.setAdapter(sdAdapter);

        frag1SdListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, WaterAndSandDetailsActivity.class);
                intent.putExtra("id", sandlList.get(position).get("id"));
                intent.putExtra("sand", true);
                startActivity(intent);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        frag1YhjListRv.setLayoutManager(linearLayoutManager);
        commonAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_yhj_frag1_layout, couponList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> data, int position) {
                TextView item_voucher_tv = holder.getView(R.id.item_voucher_tv);
                TextView item_voucher_tv2 = holder.getView(R.id.item_voucher_tv2);
                /**
                 *   {
                 "type_id": "9",
                 "type_name": "泳池免费体验券",
                 "intro": "限0-8岁宝宝体验一次",
                 "number": "30"
                 },
                 */

                item_voucher_tv.setText(data.get("type_name"));
                item_voucher_tv2.setText(data.get("intro"));


                holder.setText(R.id.item_frag1_coupon_title, data.get("type_name") + "");
                holder.setText(R.id.item_frag1_coupon_count, "数量：" + data.get("number") + "");
                LinearLayout item_coupon_root = holder.getView(R.id.item_coupon_root);

                item_coupon_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.collarCoupon(getActivity(), token, data.get("type_id") + "");
                    }
                });

            }
        };
        frag1YhjListRv.setAdapter(commonAdapter);


        GalleryLayoutManager layoutManager1 = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager1.attach(mMainRecycle1, 10);
        layoutManager1.setItemTransformer(new ScaleTransformer());
        demoAdapter1 = new Frag1GalleryAdapter(mActivity, galleryList) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }
        };
        demoAdapter1.setOnItemClickListener(new Frag1GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mMainRecycle1.smoothScrollToPosition(position);

                Intent intent = new Intent(mActivity, EverydayTextDetailsActivity.class);
                intent.putExtra("id", galleryList.get(position).get("id"));
                startActivity(intent);

            }
        });
        mMainRecycle1.setAdapter(demoAdapter1);

    }

    @OnClick(R.id.frag1_sys_title)
    public void clickSYSTitle() {
        startActivity(new Intent(mActivity, EverydayTextActivity.class));
    }
   // frag1WdyyLl  frag1_my_yyue_title
    @OnClick({R.id.frag1_wdyy_ll,R.id.frag1_yc_item_ll})
    public void clickMyYYueTitle() {
        startActivityForResult(new Intent(mActivity, MyYYueActivity.class), 101);
    }

    @OnClick(R.id.frag1_yhj_title)
    public void clickYHJTitle() {
        startActivity(new Intent(mActivity, CouponActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        unbinder.unbind();
    }

    @Override
    public Fragment1Presenter initPresenter() {
        return new Fragment1Presenter(this);
    }

    @Override
    public void checkMemberCallback() {
        mActivity.finish();
        Intent intent1 = new Intent(mActivity, LoginActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

    @Override
    public void signDataSuccess(Frag1SignData signData) {
        this.signData = signData;
    }
    private boolean isShow = false;
    @Override
    public void homeDataSuccess(HomeData homeData) {
        this.homeData = homeData;
        if (homeData != null) {
            //轮播图
            if (homeData.getBanner() != null && homeData.getBanner().size() > 0) {
                images.clear();
                for (Map<String, String> data : homeData.getBanner()) {
                    images.add(data.get("picture"));
                }
                setBanner(homeData.getBanner());
            }

            Map<String,String> advData = homeData.getAdvertising();
            if(advData!=null&&!advData.isEmpty()){
                if(!isShow) {
                    isShow = true;
                    presenter.showAdvertDialog(mActivity, advData);
                    if (!TextUtils.isEmpty(token)) {
                        presenter.submitLook(token, advData.get("id"));
                    }
                }
            }

            //画廊数据
            if (homeData.getArticle() != null && homeData.getArticle().size() > 0) {
                frag1GalleryLl.setVisibility(View.VISIBLE);
                galleryList.clear();
                galleryList.addAll(homeData.getArticle());
                demoAdapter1.notifyDataSetChanged();
            } else {
                frag1GalleryLl.setVisibility(View.GONE);
            }
            //我的预约
            if (homeData.getAppointment_info() != null && homeData.getAppointment_info().size() > 0) {
                frag1WdyyLl.setVisibility(View.VISIBLE);
                Map<String, String> data = homeData.getAppointment_info();
                /**
                 *   "appointment_info": {
                 "pool_type": "婴儿游泳池",
                 "pool_name": "福田区呱呱太游泳馆",
                 "address": "深圳市福田区上梅林路多利公约园12-58座744室",
                 "start_time": "1970-01-01 08:00"
                 */
                itemYyueTime.setText(data.get("start_time"));
                itemYyueName.setText(data.get("pool_name"));
                itemYyueType.setText(data.get("pool_type"));
                itemYyueAddress.setText(data.get("address"));

            } else {
                frag1WdyyLl.setVisibility(View.GONE);
            }
            //泳池
            if (homeData.getPool_info() != null && homeData.getPool_info().size() > 0) {
                poolList.clear();
                poolList.addAll(homeData.getPool_info());
                adapter.notifyDataSetChanged();
            }
            //沙地
            if (homeData.getSand_info() != null && homeData.getSand_info().size() > 0) {
                sandlList.clear();
                sandlList.addAll(homeData.getSand_info());
                sdAdapter.notifyDataSetChanged();
            }
            //优惠劵

            if (homeData.getDiscount_list() != null && homeData.getDiscount_list().size() > 0) {
                couponList.clear();
                couponList.addAll(homeData.getDiscount_list());
                commonAdapter.notifyDataSetChanged();
                frag1YhjTitle.setVisibility(View.VISIBLE);
            }else{
                frag1YhjTitle.setVisibility(View.GONE);
            }

        }
    }

    private void setBanner(final List<Map<String, String>> data) {
        //设置banner样式
        frag1Banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        frag1Banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        frag1Banner.setImages(images);
        //设置banner动画效果
        frag1Banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        frag1Banner.isAutoPlay(true);
        //设置轮播时间
        frag1Banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        frag1Banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        frag1Banner.start();
        frag1Banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                /**
                 * 0.跳转连接1 沙池 2 水池 3:美文 4:视频 5:书房 6:玩中学
                 */
                String numerType = data.get(position).get("number_type");
                if(numerType.equals("0")){
                    Intent intent = new Intent(mActivity, AllWebViewActivity.class);
                    intent.putExtra("title", data.get(position).get("name"));
                    intent.putExtra("link", data.get(position).get("url"));
                    startActivity(intent);
                }else if(numerType.equals("1")||numerType.equals("2")){
                    Intent intent = new Intent(mActivity, WaterAndSandDetailsActivity.class);
                    intent.putExtra("id", data.get(position).get("url"));
                    startActivity(intent);
                }else if(numerType.equals("3")){
                    Intent intent = new Intent(mActivity, EverydayTextDetailsActivity.class);
                    intent.putExtra("id", data.get(position).get("url"));
                    startActivity(intent);
                }else if(numerType.equals("4")){
                    Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                    intent.putExtra("id",data.get(position).get("url"));
                    intent.putExtra("study", "1");
                    startActivity(intent);
                }else if(numerType.equals("5")){
                    Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                    intent.putExtra("goods_id", data.get(position).get("url"));
                    startActivity(intent);
                }else if(numerType.equals("6")){
                    Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                    intent.putExtra("goods_id", data.get(position).get("url"));
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("tag", "-------------------->" + requestCode + "------------->" + resultCode);
        if (requestCode == 100 && resultCode == 100) {
            if (data != null) {
                frag1CityTv.setText(data.getStringExtra("city"));
                myApplication.setCity(data.getStringExtra("city"));
            }
        } else if (requestCode == 101 && resultCode == 101) {
            presenter.getHomeData(token);
        }
    }

    private boolean verifyStoragePermissions() {
        if (!EasyPermissions.hasPermissions(mActivity, PERMISSIONS_STORAGE)) {
            EasyPermissions.requestPermissions(this, "APP需要定位权限", 1, PERMISSIONS_STORAGE);
            return false;
        }
        return true;
    }

    //请求权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Utils.showMToast(mActivity, "请开启定位权限");

    }

}
