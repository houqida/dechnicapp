package com.dechnic.omsdcapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseFragment;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.energy.fragment.BasicFactsFragment;
import com.dechnic.omsdcapp.energy.fragment.AllDataFragment;
import com.dechnic.omsdcapp.energy.fragment.AnalyseFragment;
import com.dechnic.omsdcapp.energy.fragment.RealDataFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class EnergyFragment extends BaseFragment{

    private TabLayout tabs;
    private ViewPager viewPager;
    private BasicFactsFragment basicFactsFragment;
    private RealDataFragment dataFragment;
    private AllDataFragment allDataFragment;
    private AnalyseFragment analyseFragment;
    private List<Fragment> list;

    private String[] tabTitle= {"能耗概况","实时数据","能耗报表","对比分析"};
    private MyAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_energy);
        initViews();
        return getContentView();
    }

    private void initViews() {
        tabs = (TabLayout) findViewById(R.id.energy_tabs);
        tabs.post(new Runnable() {
            @Override
            public void run() {
                Utils.setIndicator(tabs,10,10);

            }
        });
        viewPager = (ViewPager) findViewById(R.id.fragmentVp);
        setFragment();

    }

    private void setFragment() {
        list = new ArrayList<>();
        if (basicFactsFragment!=null) {
            basicFactsFragment.onDestroyView();
        }
        basicFactsFragment = new BasicFactsFragment();
        if (dataFragment!=null) {
            dataFragment.onDestroyView();
        }
        dataFragment = new RealDataFragment();

        if (allDataFragment!=null){
            allDataFragment.onDestroyView();
        }
        allDataFragment= new AllDataFragment();

        if (analyseFragment!=null) {
            analyseFragment.onDestroyView();
        }
        analyseFragment = new AnalyseFragment();

        list.add(basicFactsFragment);
        list.add(dataFragment);
        list.add(allDataFragment);
        list.add(analyseFragment);

        adapter = new MyAdapter(getChildFragmentManager());
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);


    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }
}
