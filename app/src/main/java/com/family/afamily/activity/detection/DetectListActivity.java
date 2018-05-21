package com.family.afamily.activity.detection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.family.afamily.activity.AddBodyActivity;
import com.family.afamily.activity.LoginActivity;
import com.family.afamily.activity.mvp.interfaces.DetectListView;
import com.family.afamily.activity.mvp.presents.DetectListPresenter;
import com.family.afamily.config.DetectType;
import com.family.afamily.entity.BodyModel;
import com.family.afamily.fragment.base.BaseFragment;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetectListActivity extends BaseFragment<DetectListPresenter> implements DetectListView {
    @BindView(R.id.detect_layout)
    TabLayout tabLayout;
    @BindView(R.id.detect_view_pager)
    ViewPager viewPager;
    @BindView(R.id.appBarLayout)
    LinearLayout appBarLayout;
    @BindView(R.id.detect_person_img)
    ImageView detectPersonImg;
    @BindView(R.id.detect_person_lin)
    LinearLayout detectPersonLin;
    @BindView(R.id.detect_change_body)
    LinearLayout detectChangeBody;
    @BindView(R.id.detect_change_rel)
    RelativeLayout detectChangeRel;
    @BindView(R.id.detect_add_rel)
    RelativeLayout detectAddRel;
    @BindView(R.id.detect_add_body_lin)
    LinearLayout detectAddBodyLin;
    @BindView(R.id.detect_name_text)
    TextView detectNameText;
    @BindView(R.id.detect_age_text)
    TextView detectAgeText;
    @BindView(R.id.detect_height_text)
    TextView detectHeightText;
    @BindView(R.id.detect_weight_text)
    TextView detectWeightText;

    private MyDetectAdapter myDetectAdapter;
    private int item=1;//当前选中第几个

    private Unbinder unbinder;

    private Activity mActivity;

    List<BodyModel> bodyModels = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detect_list, container, false);

        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();
        //设置状态栏高度
        appBarLayout.setPadding(0, StatusBarCompat.getStatusBarHeight(mActivity), 0, 0);

        getData();

        initData();
        return view;
    }

    public void init(){
    }


    public void getData() {
        String token = (String) SPUtils.get(mActivity, "token", "");

        if (TextUtils.isEmpty(token)) {
//            Intent intent = new Intent(mActivity, LoginActivity.class);
//            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mActivity)) {
            if(!TextUtils.isEmpty(token)) {
                presenter.getData(token);
            }
        } else {
            toast("网络异常");
        }
    }

    @Override
    public DetectListPresenter initPresenter() {
        return new DetectListPresenter(this);
    }

    public void initData() {
        myDetectAdapter = new MyDetectAdapter(getActivity().getSupportFragmentManager());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(myDetectAdapter.getCount() - 1);
        viewPager.setAdapter(myDetectAdapter);
        viewPager.setCurrentItem(item);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(DetectType.ONE.getCategory());
        tabLayout.getTabAt(1).setText(DetectType.TWO.getCategory());
        tabLayout.getTabAt(2).setText(DetectType.THREE.getCategory());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && requestCode == 1000) {
            //添加
            /*bodyModels.add((BodyModel) data.getParcelableExtra("body"));

            detectChangeRel.setVisibility(View.VISIBLE);
            detectAddRel.setVisibility(View.GONE);

            if (bodyModels.size() >= 2) {
                detectChangeBody.setVisibility(View.VISIBLE);
                detectAddBodyLin.setVisibility(View.GONE);
            } else {
                detectChangeBody.setVisibility(View.GONE);
                detectAddBodyLin.setVisibility(View.VISIBLE);

                setData(bodyModels.get(0));
            }*/

            getData();
        }else if (resultCode == 100 && requestCode == 1001) {
            //修改
            /*BodyModel bodyModel=(BodyModel) data.getParcelableExtra("body");

            for (int i = 0; i < bodyModels.size(); i++) {
                if(bodyModels.get(i).getId().equals(bodyModel.getId())){
                    bodyModels.set(i,bodyModel);

                    setData(bodyModel);

                    return;
                }
            }*/
            getData();
        }
    }

    @OnClick({R.id.detect_person_img, R.id.detect_person_lin, R.id.detect_list_add, R.id.detect_add_body_lin,
            R.id.detect_change_body})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detect_person_img:
                if(bodyModel==null){
                    toast("数据异常");
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("bodyModel", bodyModel);

                Intent intent = new Intent(mActivity, AddBodyActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
                break;
            case R.id.detect_person_lin:
                if(bodyModel==null){
                    toast("数据异常");
                }

                bundle = new Bundle();
                bundle.putParcelable("bodyModel", bodyModel);

                intent = new Intent(mActivity, AddBodyActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
                break;
            case R.id.detect_list_add:
                String token = (String) SPUtils.get(mActivity, "token", "");

                if (TextUtils.isEmpty(token)) {
                    intent = new Intent(mActivity, LoginActivity.class);
                     startActivity(intent);
                    return;
                }


                intent = new Intent(mActivity, AddBodyActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.detect_add_body_lin:
                intent = new Intent(mActivity, AddBodyActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.detect_change_body:
                //切换孩子
                if (bodyModels.size() < 2) {
                    return;
                }

                String currentName = detectNameText.getText().toString();
                if (bodyModels.get(0).getNickname().equals(currentName)) {
                    setData(bodyModels.get(1));
                } else {
                    setData(bodyModels.get(0));
                }
                break;
        }
    }

    @Override
    public void successData(List<BodyModel> data) {
        if (data != null && data.size() > 0) {
            bodyModels = data;

            detectChangeRel.setVisibility(View.VISIBLE);
            detectAddRel.setVisibility(View.GONE);
            if(TextUtils.isEmpty(currentId)){
                setData(bodyModels.get(0));
            }else{
                for (int i = 0; i <bodyModels.size() ; i++) {
                    if(bodyModels.get(i).getId().equals(currentId)){
                        setData(bodyModels.get(i));
                    }
                }
            }

            if (data.size() >= 2) {
                detectChangeBody.setVisibility(View.VISIBLE);
                detectAddBodyLin.setVisibility(View.GONE);
            } else {
                detectChangeBody.setVisibility(View.GONE);
                detectAddBodyLin.setVisibility(View.VISIBLE);
            }
        }
    }

    private int month = 0;
    private BodyModel bodyModel;
    //当前选择的baobaoid
    private String currentId;

    public void setData(BodyModel bodyModel) {
        this.bodyModel = bodyModel;
        currentId=bodyModel.getId();
        detectNameText.setText(bodyModel.getNickname());
        detectHeightText.setText(bodyModel.getHeight());
        detectWeightText.setText(bodyModel.getWeight());

        if(!bodyModel.getBirthday().contains("-")){
            bodyModel.setBirthday(AppUtil.getStrTime(bodyModel.getBirthday()+"000"));
        }

       // month = AppUtil.countMonths(bodyModel.getBirthday(), AppUtil.getStrTime(), "yyyy-MM-dd");
        month=bodyModel.getMonth();
        detectAgeText.setText(month / 12 + "岁" + month % 12 + "个月");
        //将宝宝的月份注入到fragment中
        setMonth(month);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideCircleTransform(mActivity));

        Glide.with(mActivity)
                .load(bodyModel.getIcon())
                .apply(options)
                .into(detectPersonImg);
    }

    public void setMonth(int month) {
        ConductTestNewFragment conductTestFragment = (ConductTestNewFragment) myDetectAdapter.getItem(1);
        conductTestFragment.setMonth(month,detectNameText.getText().toString());

        ParentingGuideFragment parentingGuideFragment = (ParentingGuideFragment) myDetectAdapter.getItem(2);
        parentingGuideFragment.setMonth(month);
    }
}
