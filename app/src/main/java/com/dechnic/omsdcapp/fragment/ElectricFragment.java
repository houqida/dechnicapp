package com.dechnic.omsdcapp.fragment;

import android.content.res.Resources;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseFragment;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.electric.fragment.EverydayFragment;
import com.dechnic.omsdcapp.electric.fragment.ElectricDetailFragment;
import com.dechnic.omsdcapp.electric.fragment.TrendFragment;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 * 电量
 */

@SuppressWarnings("WrongConstant")
public class ElectricFragment extends BaseFragment {
    private TabLayout electic_tabs;
    private ViewPager fragmentVp;
    private List<Fragment> fragmentList;
    private EverydayFragment everydayFragment;
    private TrendFragment trendFragment;
    private ElectricDetailFragment detailFragment;
    private String[] tabTitle = {"每日用电", "用电统计", "用电明细"};//每个页面顶部标签的名字
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_electric);
        initViews();
        return getContentView();
    }

    private void initViews() {
        electic_tabs = (TabLayout) findViewById(R.id.electic_tabs);
        electic_tabs.post(new Runnable() {
            @Override
            public void run() {
                Utils.setIndicator(electic_tabs,18,18);
            }
        });
        fragmentVp = (ViewPager) findViewById(R.id.fragmentVp);
        setFragment();

    }

    private void setFragment() {
        fragmentList = new ArrayList<>();
        if (everydayFragment == null) {
            everydayFragment = new EverydayFragment();
        }
        fragmentList.add(everydayFragment);
        if (trendFragment == null) {
            trendFragment = new TrendFragment();
        }
        fragmentList.add(trendFragment);
        if (detailFragment == null) {
            detailFragment = new ElectricDetailFragment();
        }
        fragmentList.add(detailFragment);

        adapter = new MyAdapter(getChildFragmentManager());
        adapter.notifyDataSetChanged();
        fragmentVp.setAdapter(adapter);

        electic_tabs.setupWithViewPager(fragmentVp);
        electic_tabs.setTabsFromPagerAdapter(adapter);

    }


    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}