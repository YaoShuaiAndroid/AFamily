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
import com.family.afamily.activity.Frag4DetailsActivity;
import com.family.afamily.activity.Frag4ListActivity;
import com.family.afamily.activity.ProductDetailsActivity;
import com.family.afamily.activity.SearchActivity;
import com.family.afamily.activity.WaterAndSandDetailsActivity;
import com.family.afamily.activity.ZaoJaoDetailsActivity;
import com.family.afamily.adapters.Frag4Adapter;
import com.family.afamily.adapters.Frag4SuperAdapter;
import com.family.afamily.entity.Frag4IndexData;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.fragment.interfaces.Fragment4View;
import com.family.afamily.fragment.presenters.Fragment4Presenter;
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

public class Fragment4 extends BaseFragment<Fragment4Presenter> implements Fragment4View, SuperRecyclerView.LoadingListener {
    @BindView(R.id.base_title_right_iv)
    ImageView titleRightIv;
    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    //    @BindView(R.id.base_title_right_iv)
//    ImageView baseTitleRightIv;
    Banner frag4Banner;
    RecyclerView frag4NewListLv;
    //    @BindView(R.id.frag4_child_list_lv)
    RecyclerView frag4ChildListLv;
    //    @BindView(R.id.frag4_jj_list_lv)
    MyListView frag4JjListLv;
//    @BindView(R.id.frag4_wj_list_lv)
//    MyListView frag4WjListLv;


    @BindView(R.id.frag4_wj_list_rv)
    SuperRecyclerView wjListRv;

    Unbinder unbinder;
    private Activity mActivity;
    private List<String> images = new ArrayList<>();
    private List<Map<String, String>> newList = new ArrayList<>();
    private List<Map<String, String>> hotList = new ArrayList<>();
    private CommonAdapter<Map<String, String>> adapter;
    private CommonAdapter<Map<String, String>> adapter2;

    private List<Map<String, String>> studyList = new ArrayList<>();
    private List<Map<String, String>> playList = new ArrayList<>();

    private Frag4Adapter frag4Adapter;
    private Frag4SuperAdapter frag4Adapter2;
    private String token;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4_layout, container, false);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Utils.getStatusHeight(getActivity(), view.findViewById(R.id.base_fragment_title));
        }
        unbinder = ButterKnife.bind(this, view);
        token = (String) SPUtils.get(mActivity, "token", "");
        setData();
        return view;
    }


    // @OnClick({R.id.frag4_jj_tab,R.id.frag4_jiao_ju_iv})
    public void clickJjTab() {
        Intent intent = new Intent(mActivity, Frag4ListActivity.class);
        intent.putExtra("cat_id", "42");
        startActivity(intent);
    }

    // @OnClick({R.id.frag4_wj_tab,R.id.frag4_wan_ju_iv})
    public void clickWjTab() {
        Intent intent = new Intent(mActivity, Frag4ListActivity.class);
        intent.putExtra("cat_id", "43");
        startActivity(intent);
    }

    private void setData() {
        baseTitleTv.setText("玩中学");
        if (Utils.isConnected(mActivity)) {
            presenter.getHomeData(token, 1);
        }
        titleRightIv.setImageResource(R.mipmap.ic_search);
        titleRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("isZaoj", false);
                startActivity(intent);
            }
        });

        wjListRv.setLayoutManager(new LinearLayoutManager(mActivity));
//        RecyclerViewLoadDivider divider = new RecyclerViewLoadDivider(mActivity, LinearLayout.HORIZONTAL, 2, Color.parseColor("#e8e8e8"));
//        wjListRv.addItemDecoration(divider);
        wjListRv.setRefreshEnabled(true);// 可以定制是否开启下拉刷新
        wjListRv.setLoadMoreEnabled(false);// 可以定制是否开启加载更多
        wjListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        wjListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        wjListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        wjListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        frag4Adapter2 = new Frag4SuperAdapter(mActivity, playList);
        wjListRv.setAdapter(frag4Adapter2);
        addHeadView();

        frag4Adapter = new Frag4Adapter(mActivity, studyList);
        frag4JjListLv.setAdapter(frag4Adapter);
        GridLayoutManager mgr = new GridLayoutManager(mActivity, 3);
        GridLayoutManager mgr2 = new GridLayoutManager(mActivity, 3);
        frag4NewListLv.setLayoutManager(mgr);
        frag4ChildListLv.setLayoutManager(mgr2);

        adapter = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag3_book_layout, newList) {
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
                        // Intent intent = new Intent(mActivity, Frag4DetailsActivity.class);
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", s.get("id"));
                        startActivity(intent);
                    }
                });
            }
        };
        frag4NewListLv.setAdapter(adapter);
        adapter2 = new CommonAdapter<Map<String, String>>(mActivity, R.layout.item_frag3_book_layout, hotList) {
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
                        //Intent intent = new Intent(mActivity, Frag4DetailsActivity.class);
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", s.get("id"));
                        startActivity(intent);
                    }
                });
            }
        };
        frag4ChildListLv.setAdapter(adapter2);
    }

    private void addHeadView() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.head_frag4_layout, (ViewGroup) wjListRv.getParent(), false);
        frag4Banner = headView.findViewById(R.id.frag4_banner);
        frag4NewListLv = headView.findViewById(R.id.frag4_new_list_lv);
        frag4ChildListLv = headView.findViewById(R.id.frag4_child_list_lv);
        frag4JjListLv = headView.findViewById(R.id.frag4_jj_list_lv);
        ImageView jj_tab = headView.findViewById(R.id.frag4_jj_tab);
        ImageView frag4_jiao_ju_iv = headView.findViewById(R.id.frag4_jiao_ju_iv);

        ImageView frag4_wj_tab = headView.findViewById(R.id.frag4_wj_tab);
        ImageView frag4_wan_ju_iv = headView.findViewById(R.id.frag4_wan_ju_iv);
        jj_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickJjTab();
            }
        });

        frag4_jiao_ju_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickJjTab();
            }
        });

        frag4_wj_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickWjTab();
            }
        });
        frag4_wan_ju_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickWjTab();
            }
        });


        frag4Adapter2.addHeaderView(headView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public Fragment4Presenter initPresenter() {
        return new Fragment4Presenter(this);
    }

    private void setBannerData(final List<Map<String, String>> data) {
        //设置banner样式
        frag4Banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        frag4Banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        frag4Banner.setImages(images);
        //设置banner动画效果
        frag4Banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        frag4Banner.isAutoPlay(true);
        //设置轮播时间
        frag4Banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        frag4Banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        frag4Banner.start();
        frag4Banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Intent intent = new Intent(mActivity, AllWebViewActivity.class);
//                intent.putExtra("title", data.get(position).get("name"));
//                intent.putExtra("link", data.get(position).get("url"));
//                startActivity(intent);
                String str = data.get(position).get("url");
                if (TextUtils.isEmpty(str)) {
                    toast("未设置跳转链接");
                } else {
                    /**
                     * 0.跳转连接1 沙池 2 水池 3:美文 4:视频 5:书房 6:玩中学
                     */
                    String numerType = data.get(position).get("number_type");
                    if (numerType.equals("0")) {
                        Intent intent = new Intent(mActivity, AllWebViewActivity.class);
                        intent.putExtra("title", data.get(position).get("name"));
                        intent.putExtra("link", data.get(position).get("url"));
                        startActivity(intent);
                    } else if (numerType.equals("1") || numerType.equals("2")) {
                        Intent intent = new Intent(mActivity, WaterAndSandDetailsActivity.class);
                        intent.putExtra("id", data.get(position).get("url"));
                        startActivity(intent);
                    } else if (numerType.equals("3")) {
                        Intent intent = new Intent(mActivity, EverydayTextDetailsActivity.class);
                        intent.putExtra("id", data.get(position).get("url"));
                        startActivity(intent);
                    } else if (numerType.equals("4")) {
                        Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                        intent.putExtra("id", data.get(position).get("url"));
                        intent.putExtra("study", "1");
                        startActivity(intent);
                    } else if (numerType.equals("5")) {
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", data.get(position).get("url"));
                        startActivity(intent);
                    } else if (numerType.equals("6")) {
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
    public void successData(Frag4IndexData frag4IndexData) {
        wjListRv.completeRefresh();
        if (frag4IndexData != null) {
            List<Map<String, String>> banner = frag4IndexData.getBanner();
            List<Map<String, String>> hotListD = frag4IndexData.getHot_goods();
            List<Map<String, String>> newListD = frag4IndexData.getNew_goods();
            List<Map<String, String>> baseList = frag4IndexData.getStudy_goods_list();
            List<Map<String, String>> playList_ = frag4IndexData.getPlay_goods_list();

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
                adapter.notifyDataSetChanged();
            }
            //最热
            if (hotListD != null && hotListD.size() > 0) {
                hotList.clear();
                hotList.addAll(hotListD);
                adapter2.notifyDataSetChanged();
            }
            //商品列表
            if (baseList != null && baseList.size() > 0) {
                studyList.clear();
                studyList.addAll(baseList);
                frag4Adapter.notifyDataSetChanged();
            }

            if (playList_ != null && playList_.size() > 0) {
                playList.clear();
                playList.addAll(playList_);
                frag4Adapter2.notifyDataSetChanged();
            }
            frag4Banner.setFocusable(true);
            frag4Banner.setFocusableInTouchMode(true);
            frag4Banner.requestFocus();
        }
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.getHomeData(token, 2);
        } else {
            wjListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {

    }
}
