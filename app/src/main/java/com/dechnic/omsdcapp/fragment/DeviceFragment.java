package com.dechnic.omsdcapp.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.device_controller.fragment.SocketFragment;
import com.dechnic.omsdcapp.device_controller.fragment.SwitchFragment;
import com.dechnic.omsdcapp.device_controller.fragment.TempControlFragment;
import com.dechnic.omsdcapp.entity.TabEntity;
import com.dechnic.omsdcapp.entity.UserInfo;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.DragGridView;
import com.dechnic.omsdcapp.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 * 设备
 */

public class DeviceFragment extends BaseFragment {

    private TabLayout tabs;//导航栏
    private RelativeLayout moreTabs;
    private ViewPager fragmentVp;
    private List<Fragment> fragmentList;
    private List<TabEntity> tabss = new ArrayList<>();
    private TabEntity entity;

    private MyAdapter adapter;
    private TempControlFragment tempControlFragment;
    private SwitchFragment switchFragment;
    private SocketFragment socketFragment;

    private PopupWindow popupWindow;
    private List<HashMap<String, Object>> dataSourceList = new ArrayList<>();
    private SimpleAdapter mSimpleAdapter;
    private List<String> tabLists;
    UserInfo userInfo;

    String device_controller, device_socket, device_switch;

    int device_con, device_sw, device_sok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_device);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.post(new Runnable() {
            @Override
            public void run() {
                Utils.setIndicator(tabs,18,18);

            }
        });
        userInfo = AppUtils.getUserInfo(getActivity());
        Log.e("==onCreateView", "onCreateView");
        setTabs();
        initTabsPopup();
        initViews();
        return getContentView();
    }

    private void setTabs() {
        device_controller = userInfo.getDevice_control();
        device_socket = userInfo.getDevice_socket();
        device_switch = userInfo.getDevice_switch();
        // 屏蔽掉主页“智能温控”tab页，add by houqida 20191231
        for (int i = 1; i < 3; i++) {
            entity = new TabEntity();
            if ((i + "").equals(device_switch)) {
                entity.setTab("智能开关");
            } else {
                entity.setTab("智能插座");
            }
            tabss.add(entity);
        }

//        for (int i = 1; i < 4; i++) {
//            entity = new TabEntity();
//            if ((i + "").equals(device_controller)) {
//                entity.setTab("智能温控");
//            } else if ((i + "").equals(device_switch)) {
//                entity.setTab("智能开关");
//            } else {
//                entity.setTab("智能插座");
//            }
//            tabss.add(entity);
//        }
        Log.e("==tabss", tabss.toString());
    }

    private void changeTabs() {
        tabss = new ArrayList<>();
        entity = new TabEntity();
        entity.setTab(tabLists.get(0).toString());
        tabss.add(entity);

        entity = new TabEntity();
        entity.setTab(tabLists.get(1).toString());
        tabss.add(entity);

        entity = new TabEntity();
        entity.setTab(tabLists.get(2).toString());
        tabss.add(entity);
        Log.e("==tablists", tabLists.toString());
        userInfo.setDevice_control(device_con+"");
        userInfo.setDevice_socket(device_sok+"");
        userInfo.setDevice_switch(device_sw+"");
        AppUtils.setUserInfo(getActivity(),userInfo);

        updateFragment();
    }

    private void initTabsPopup() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_tabs_popup, null);
        RelativeLayout close_popup_Rel = (RelativeLayout) view.findViewById(R.id.close_popup_Rel);
        DragGridView dragGridView = (DragGridView) view.findViewById(R.id.dragGridView);
        LinearLayout close_linlay = (LinearLayout) view.findViewById(R.id.close_linlay);

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);

        for (int i = 0; i < tabss.size(); i++) {
            HashMap<String, Object> itemHashMap = new HashMap<>();
            itemHashMap.put("text", tabss.get(i).getTab());
            dataSourceList.add(itemHashMap);
        }

        mSimpleAdapter = new SimpleAdapter(getActivity(), dataSourceList, R.layout.item_drag_gridview, new String[]{"text"},
                new int[]{R.id.item_text});

        dragGridView.setAdapter(mSimpleAdapter);
        tabLists = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            if ((i + "").equals(device_controller)) {
                tabLists.add("智能温控");
            } else if ((i + "").equals(device_switch)) {
                tabLists.add("智能开关");
            } else {
                tabLists.add("智能插座");
            }
        }

        dragGridView.setOnChangeListener(new DragGridView.OnChanageListener() {

            @Override
            public void onChange(int form, int to) {
                HashMap<String, Object> temp = dataSourceList.get(form);

                //这里的处理需要注意下
                if (form < to) {
                    for (int i = form; i < to; i++) {
                        Collections.swap(dataSourceList, i, i + 1);
                    }
                } else if (form > to) {
                    for (int i = form; i > to; i--) {
                        Collections.swap(dataSourceList, i, i - 1);
                    }
                }

                dataSourceList.set(to, temp);

                tabLists.clear();
                for (int i = 0; i < dataSourceList.size(); i++) {
                    String tabs = String.valueOf(dataSourceList.get(i).get("text"));
                    tabLists.add(tabs);
                }


                Log.e("======", tabLists.get(0).toString());
                for (int i = 0; i < tabLists.size(); i++) {
                    if (tabLists.get(i).toString().equals("智能温控")) {
                        device_con = i + 1;
                    } else if (tabLists.get(i).toString().equals("智能开关")) {
                        device_sw = i + 1;
                    } else {
                        device_sok = i + 1;
                    }
                }
                Log.e("==device_con",device_con+"");
                Log.e("==device_sw",device_sw+"");
                Log.e("==device_sok",device_sok+"");

                mSimpleAdapter.notifyDataSetChanged();
            }
        });

        close_linlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        close_popup_Rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                setOrder();
            }
        });

    }

    private void setOrder(){
        RequestParams params = new RequestParams(HttpURL.Url(getActivity())+HttpURL.DEVICETAB_ORDER);
        params.addHeader("access_token",AppUtils.getAccessToken(getActivity()));
        params.addQueryStringParameter("device_controller",device_con+"");
        params.addQueryStringParameter("device_switch",device_sw+"");
        params.addQueryStringParameter("device_socket",device_sok+"");
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.optString("result");
                    if ("1".equals(res)) {
                        //修改成功
                        Message msg = myHandler.obtainMessage();
                        msg.what = 1;
                        myHandler.sendMessage(msg);
                    }else {
                        Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
             }
        });

    }

    private void initViews() {
        moreTabs = (RelativeLayout) findViewById(R.id.moreTabs);
        fragmentVp = (ViewPager) findViewById(R.id.fragmentVp);
        setFragment();

        moreTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "执行排序操作", Toast.LENGTH_SHORT).show();
                popupWindow.showAsDropDown(v, 0, -100);
            }
        });
    }

    private void setFragment() {
        fragmentList = new ArrayList<>();
        if (tempControlFragment != null) {
            tempControlFragment.onDestroyView();
        }
        if (switchFragment != null) {
            switchFragment.onDestroyView();
        }
        if (socketFragment != null) {
            socketFragment.onDestroyView();
        }
        for (int i = 0; i < tabss.size(); i++) {
            Log.e("==tabss.get" + i, tabss.get(i).getTab());
            if (tabss.get(i).getTab().equals("智能温控")) {
                fragmentList.add( new TempControlFragment());
                Log.e("==智能", "温控");
            } else if (tabss.get(i).getTab().equals("智能开关")) {
                fragmentList.add(new SwitchFragment());
                Log.e("==智能", "开关");
            } else {
                fragmentList.add(new SocketFragment());
                Log.e("==智能", "插座");
            }
        }

        Log.e("==fragmentList", fragmentList.toString());

        adapter = new MyAdapter(getChildFragmentManager(), fragmentList);
        adapter.notifyDataSetChanged();
        fragmentVp.setAdapter(adapter);
        tabs.setupWithViewPager(fragmentVp);
    }

    private void updateFragment() {
        fragmentList = new ArrayList<>();
        if (tempControlFragment != null) {
            tempControlFragment.onDestroyView();
        }
        if (switchFragment != null) {
            switchFragment.onDestroyView();
        }
        if (socketFragment != null) {
            socketFragment.onDestroyView();
        }
        for (int i = 0; i < tabss.size(); i++) {
            Log.e("==tabss.get" + i, tabss.get(i).getTab());
            if (tabss.get(i).getTab().equals("智能温控")) {
                fragmentList.add(new TempControlFragment());
                Log.e("==智能", "温控");
            } else if (tabss.get(i).getTab().equals("智能开关")) {
                fragmentList.add(new SwitchFragment());
                Log.e("==智能", "开关");
            } else {
                fragmentList.add(new SocketFragment());
                Log.e("==智能", "插座");
            }
        }

        Log.e("==fragmentList", fragmentList.toString());

        adapter.setFragments(fragmentList);
        adapter.notifyDataSetChanged();
        fragmentVp.setAdapter(adapter);
        tabs.setupWithViewPager(fragmentVp);

    }

    private MyHandler myHandler = new MyHandler();
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tabs.post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setIndicator(tabs,18,18);
                        }
                    });
                    changeTabs();
                    break;
            }
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        private List<Fragment> fgs = null;
        FragmentManager fm;

        public MyAdapter(FragmentManager fm, List<Fragment> fgs) {
            super(fm);
            this.fgs = fgs;
            this.fm = fm;
        }

        public void setFragments(List<Fragment> fragments) {
            if (this.fgs != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.fgs) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.fgs = fragments;
            notifyDataSetChanged();
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
//            return titles[position];
            return tabss.get(position).getTab();
        }
    }

}
