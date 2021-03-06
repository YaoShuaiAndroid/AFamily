package com.family.afamily.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.AllWebViewActivity;
import com.family.afamily.activity.EverydayTextDetailsActivity;
import com.family.afamily.activity.OpenMemberActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.activity.SearchActivity;
import com.family.afamily.activity.StudyDetailsActivity;
import com.family.afamily.activity.StudyListActivity;
import com.family.afamily.activity.WaterAndSandDetailsActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.adapters.Frag3Adapter;
import com.family.afamily.adapters.Frag3SuperAdapter;
import com.family.afamily.adapters.StudyListAdapter;
import com.family.afamily.entity.Frag3IndexData;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.fragment.interfaces.Fragment3View;
import com.family.afamily.fragment.presenters.Fragment3Presenter;
import com.family.afamily.recycleview.CommonAdapter;
import com.family.afamily.recycleview.RecyclerViewLoadDivider;
import com.family.afamily.recycleview.ViewHolder;
import com.family.afamily.utils.GlideImageLoader;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.MyListView;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
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

/**
 * Created by hp2015-7 on 2017/11/30.
 */

public class Fragment3 extends BaseFragment<Fragment3Presenter> implements Fragment3View,SuperRecyclerView.LoadingListener {
    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    @BindView(R.id.base_title_right_iv)
    ImageView titleRightIv;
    Banner frag3Banner;
    RecyclerView frag3NewListLv;
    RecyclerView frag3HotListLv;
    ImageView frag3OpenMemberIv;
    TextView frag3OpenVipTv;
    ImageView frag3OpenVipIv;

    @BindView(R.id.frag3_list_rv)
    SuperRecyclerView frag3ListRv;
    Unbinder unbinder;

    private Frag3SuperAdapter frag3SuperAdapter;
    private Activity mActivity;
    private List<String> images = new ArrayList<>();
    private CommonAdapter<Map<String, String>> newAdapter;
    private CommonAdapter<Map<String, String>> hotAdapter;
    private List<Map<String, String>> newList = new ArrayList<>();
    private List<Map<String, String>> hotList = new ArrayList<>();
  //  private Frag3Adapter frag3Adapter;
    private List<Map<String, String>> list = new ArrayList<>();
    private String token;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout, container, false);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Utils.getStatusHeight(getActivity(), view.findViewById(R.id.base_fragment_title));
        }
        unbinder = ButterKnife.bind(this, view);
        token = (String) SPUtils.get(mActivity, "token", "");
        setData();
        return view;
    }

    private void setData() {
        baseTitleTv.setText("爸爸的书房");
        titleRightIv.setImageResource(R.mipmap.ic_search);
        titleRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("isZaoj",false);
                startActivity(intent);
            }
        });

        frag3ListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
        frag3ListRv.addItemDecoration(divider);
        frag3ListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        frag3ListRv.setLoadMoreEnabled(false);// 可以定制是否开启加载更多
        frag3ListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        frag3ListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        frag3ListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        frag3ListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        frag3SuperAdapter = new Frag3SuperAdapter(mActivity, list);
        frag3ListRv.setAdapter(frag3SuperAdapter);
        addHeadView();

        frag3OpenVipTv.setTag(-1);
        if (Utils.isConnected(mActivity)) {
            presenter.getHomeData(token,1);
        }

        frag3Banner.setFocusable(true);
        frag3Banner.setFocusableInTouchMode(true);
        frag3Banner.requestFocus();
//        frag3Adapter = new Frag3Adapter(mActivity, list);
//        frag3ListLv.setAdapter(frag3Adapter);

        GridLayoutManager mgr = new GridLayoutManager(mActivity, 3);
        GridLayoutManager mgr2 = new GridLayoutManager(mActivity, 3);
        frag3NewListLv.setLayoutManager(mgr);
        frag3HotListLv.setLayoutManager(mgr2);

        newAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag3_book_layout, newList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> s, int position) {
                ImageView item_book_img = holder.getView(R.id.item_book_img);
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.error_pic);
                Glide.with(mActivity).load(s.get("thumb")).apply(options).into(item_book_img);
                holder.setText(R.id.item_book_name, s.get("name"));
                holder.setText(R.id.item_book_price, s.get("shop_price"));

                holder.setOnClickListener(R.id.item_goods_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(mActivity, StudyDetailsActivity.class);
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", s.get("id"));
                        startActivity(intent);
                    }
                });
            }
        };
        frag3NewListLv.setAdapter(newAdapter);
        hotAdapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag3_book_layout, hotList) {
            @Override
            protected void convert(ViewHolder holder, final Map<String, String> s, int position) {
                ImageView item_book_img = holder.getView(R.id.item_book_img);
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.error_pic);
                Glide.with(mActivity).load(s.get("thumb")).apply(options).into(item_book_img);
                holder.setText(R.id.item_book_name, s.get("name"));
                holder.setText(R.id.item_book_price, s.get("shop_price"));

                holder.setOnClickListener(R.id.item_goods_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Intent intent = new Intent(mActivity, StudyDetailsActivity.class);
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", s.get("id"));
                        startActivity(intent);
                    }
                });
            }
        };
        frag3HotListLv.setAdapter(hotAdapter);
    }

    private void addHeadView() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.head_frag3_layout, (ViewGroup) frag3ListRv.getParent(), false);
        frag3Banner = headView.findViewById(R.id.frag3_banner);
        frag3NewListLv = headView.findViewById(R.id.frag3_new_list_lv);
        frag3HotListLv = headView.findViewById(R.id.frag3_hot_list_lv);
        frag3OpenMemberIv = headView.findViewById(R.id.frag3_open_member_iv);
        frag3OpenVipTv = headView.findViewById(R.id.frag3_open_vip_tv);
        frag3OpenVipIv = headView.findViewById(R.id.frag3_open_vip_iv);
        ImageView frag3_1_tab = headView.findViewById(R.id.frag3_1_tab);
        ImageView frag3_2_tab = headView.findViewById(R.id.frag3_2_tab);
        ImageView frag3_3_tab = headView.findViewById(R.id.frag3_3_tab);


        frag3_1_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab1();
            }
        });

        frag3_2_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab2();
            }
        });

        frag3_3_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab3();
            }
        });
        frag3OpenVipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // clickOpenVip();
            }
        });

        frag3OpenVipIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickOpenVip();
            }
        });

        frag3SuperAdapter.addHeaderView(headView);
    }


//    @OnClick(R.id.frag3_1_tab)
    public void clickTab1() {
        Intent intent = new Intent(mActivity, StudyListActivity.class);
        intent.putExtra("cat_id", "39");
        startActivity(intent);
    }
//
//    @OnClick(R.id.frag3_2_tab)
    public void clickTab2() {
        Intent intent = new Intent(mActivity, StudyListActivity.class);
        intent.putExtra("cat_id", "40");
        startActivity(intent);
    }
//
//    @OnClick(R.id.frag3_3_tab)
    public void clickTab3() {
        Intent intent = new Intent(mActivity, StudyListActivity.class);
        intent.putExtra("cat_id", "2");
        startActivity(intent);
    }
//
//    @OnClick({R.id.frag3_open_vip_tv, R.id.frag3_open_vip_iv})
    public void clickOpenVip() {
        String str = frag3OpenVipTv.getText().toString();
        int flag = (int) frag3OpenVipTv.getTag();
        if (flag == 1) {
            if (str.equals("开通会员")) {
                Intent intent = new Intent(mActivity, OpenMemberActivity.class);
                startActivity(intent);
            }
        } else {
            toast("未获取到数据");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public Fragment3Presenter initPresenter() {
        return new Fragment3Presenter(this);
    }

    @Override
    public void homeDataSuccess(Frag3IndexData homeData) {
        frag3ListRv.completeRefresh();
        if (homeData != null) {
            List<Map<String, String>> banner = homeData.getBanner();
            List<Map<String, String>> hotListD = homeData.getHot_goods();
            List<Map<String, String>> newListD = homeData.getNew_goods();
            List<Map<String, String>> baseList = homeData.getBest_goods();
            int is_Yesr = homeData.getIs_year_number();
            frag3OpenVipTv.setTag(1);
//            if (is_Yesr == 0) {
//                frag3OpenVipTv.setText("开通会员");
//                frag3OpenVipIv.setVisibility(View.VISIBLE);
//            } else {
//                frag3OpenVipTv.setText("书房会员");
//                frag3OpenVipIv.setVisibility(View.GONE);
//            }


            //banner图
            if (banner != null && banner.size() > 0) {
                images.clear();
                for (int i = 0; i < banner.size(); i++) {
                    images.add(banner.get(i).get("picture"));
                }
                setBannerData(banner);
            }
            //最新推荐
            if (newListD != null && newListD.size() > 0) {
                newList.clear();
                newList.addAll(newListD);
                newAdapter.notifyDataSetChanged();
            }
            //最热
            if (hotListD != null && hotListD.size() > 0) {
                hotList.clear();
                hotList.addAll(hotListD);
                hotAdapter.notifyDataSetChanged();
            }
            //商品列表
            if (baseList != null && baseList.size() > 0) {
                list.clear();
                list.addAll(baseList);
                frag3SuperAdapter.notifyDataSetChanged();
            }
        }

        frag3Banner.setFocusable(true);
        frag3Banner.setFocusableInTouchMode(true);
        frag3Banner.requestFocus();
    }

    private void setBannerData(final List<Map<String, String>> data) {
        //设置banner样式
        frag3Banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        frag3Banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        frag3Banner.setImages(images);
        //设置banner动画效果
        frag3Banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        frag3Banner.isAutoPlay(true);
        //设置轮播时间
        frag3Banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        frag3Banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        frag3Banner.start();
        frag3Banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String str = data.get(position).get("url");
                if(TextUtils.isEmpty(str)){
                    toast("未设置跳转链接");
                }else {
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

//                    if(str.contains("http")){
//                    Intent intent = new Intent(mActivity, AllWebViewActivity.class);
//                    intent.putExtra("title", data.get(position).get("name"));
//                    intent.putExtra("link", data.get(position).get("url"));
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
//                    intent.putExtra("goods_id", str);
//                    startActivity(intent);
//                }
            }
        });

    }

    @Override
    public void onRefresh() {
        if(Utils.isConnected(mActivity)){
            presenter.getHomeData(token,2);
        }else{
            frag3ListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {

    }
}
