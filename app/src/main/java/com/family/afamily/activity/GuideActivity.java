package com.family.afamily.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.utils.SPUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private ArrayList<ImageView> mImageViewList;

    private int[] mImageIds = new int[]{R.mipmap.bg_root_01, R.mipmap.bg_root_02, R.mipmap.bg_root_03};
    private Activity mActivity;
    private ImageView[] indicator = null;// 下面的导航指示器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        mActivity = this;

        initData();
        GuideAdapter adapter = new GuideAdapter();
        vpGuide.setAdapter(adapter);
        vpGuide.setOnPageChangeListener(this);
        vpGuide.setCurrentItem(0);
        vpGuide.setOffscreenPageLimit(1);
    }

    @OnClick(R.id.start_btn)
    public void clickStart() {
        SPUtils.put(mActivity, "isShowGuide", false);
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initData() {
        mImageViewList = new ArrayList<>();
        indicator = new ImageView[mImageIds.length];
        for (int i = 0; i < mImageIds.length; i++) {
            //创建ImageView把mImgaeViewIds放进去
            ImageView view = new ImageView(this);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mActivity).load(mImageIds[i]).into(view);
            //添加到ImageView的集合中
            mImageViewList.add(view);

            ImageView imageView = new ImageView(mActivity);
            imageView.setPadding(70, 5, 70, 5);
            indicator[i] = imageView;
            if (i == 0) {
                indicator[i].setBackgroundColor(Color.parseColor("#A1C8F8"));
            } else {
                indicator[i].setBackgroundColor(Color.parseColor("#F6F2F1"));
            }
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            layoutParams.leftMargin = 10;
//            layoutParams.rightMargin = 10;
            llContainer.addView(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % mImageViewList.size());

        if (position == mImageViewList.size() - 1) {
            startBtn.setVisibility(View.VISIBLE);
        } else {
            startBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < indicator.length; i++) {
            if (i == selectItems) {
                indicator[i].setBackgroundColor(Color.parseColor("#A1C8F8"));
            } else {
                indicator[i].setBackgroundColor(Color.parseColor("#F6F2F1"));
            }
        }
    }


    class GuideAdapter extends PagerAdapter {

        //item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
