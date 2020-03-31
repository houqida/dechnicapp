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
import com.dechnic.omsdcapp.central_air.fragment.CentralAirPage1Fragment;
import com.dechnic.omsdcapp.commons.Utils;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 中央空调
 */

@SuppressWarnings("WrongConstant")
public class CentralAirFragment extends BaseFragment {
    private TabLayout central_air_tabs;
    private ViewPager fragmentVp;
    private List<Fragment> fragmentList;
    private CentralAirPage1Fragment centralAirPage1Fragment;
    private String[] tabTitle = {""};//每个页面顶部标签的名字
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_central_air);
        initViews();
        return getContentView();
    }

    private void initViews() {
        central_air_tabs = (TabLayout) findViewById(R.id.central_air_tabs);
        central_air_tabs.getLayoutParams().height = 0;
        central_air_tabs.post(new Runnable() {
            @Override
            public void run() {
                Utils.setIndicator(central_air_tabs,18,18);
            }
        });
        fragmentVp = (ViewPager) findViewById(R.id.fragmentVp);
        setFragment();

    }

    private void setFragment() {
        fragmentList = new ArrayList<>();
        if (centralAirPage1Fragment == null) {
            centralAirPage1Fragment = new CentralAirPage1Fragment();
        }
        fragmentList.add(centralAirPage1Fragment);

        adapter = new MyAdapter(getChildFragmentManager());
        adapter.notifyDataSetChanged();
        fragmentVp.setAdapter(adapter);

        central_air_tabs.setupWithViewPager(fragmentVp);
        central_air_tabs.setTabsFromPagerAdapter(adapter);

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