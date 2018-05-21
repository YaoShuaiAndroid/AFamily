package com.family.afamily.activity.detection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.family.afamily.config.DetectType;

import java.util.ArrayList;
import java.util.List;


public class MyDetectAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    private String[] mTabNames = {DetectType.ONE.getCategory(), DetectType.TWO.getCategory(),
            DetectType.THREE.getCategory()};

    public MyDetectAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(InnateIntelligenceFragment.newInstance(DetectType.ONE.getCategory(), DetectType.getMultiType(DetectType.ONE)));
        //mFragmentList.add(ConductTestFragment.newInstance(DetectType.TWO.getCategory(), DetectType.getMultiType(DetectType.TWO)));
        mFragmentList.add(ConductTestNewFragment.newInstance(DetectType.TWO.getCategory(), DetectType.getMultiType(DetectType.TWO)));
        mFragmentList.add(ParentingGuideFragment.newInstance(DetectType.THREE.getCategory(), DetectType.getMultiType(DetectType.THREE)));
    }

    @Override
    public int getCount() {
        return mTabNames.length;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
