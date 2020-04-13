package com.dechnic.omsdcapp.device_controller.switchs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.entity.DevicesDetailsTime;
import com.dechnic.omsdcapp.entity.TimerEntity;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.SuccinctProgress;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AddSwitchTimerActivity extends BaseActivity {
    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.saveTimerRel)
    RelativeLayout saveTimerRel;
    @Bind(R.id.loopView)
    LoopView loopView;
    @Bind(R.id.loopView2)
    LoopView loopView2;
    @Bind(R.id.oneCb)
    CheckBox oneCb;
    @Bind(R.id.twoCb)
    CheckBox twoCb;
    @Bind(R.id.threeCb)
    CheckBox threeCb;
    @Bind(R.id.fourCb)
    CheckBox fourCb;
    @Bind(R.id.fiveCb)
    CheckBox fiveCb;
    @Bind(R.id.sixCb)
    CheckBox sixCb;
    @Bind(R.id.sevenCb)
    CheckBox sevenCb;
    @Bind(R.id.timerCloseIv)
    ImageView timerCloseIv;
    @Bind(R.id.timerCloseRel)
    RelativeLayout timerCloseRel;
    @Bind(R.id.timerOpenIv)
    ImageView timerOpenIv;
    @Bind(R.id.timerOpenRel)
    RelativeLayout timerOpenRel;
    List<String> times = new ArrayList<>();
    List<String> times2 = new ArrayList<>();

    String lp1, lp2;

    Bundle bundle = new Bundle();
    private List<TimerEntity> list;
    private List<DevicesDetailsTime> detailsTimeList;
    String controlId = "";
    int position;//
    String from;//来源 lv 来自列表   add来自添加新的

    String timer = "";// true  定时开机   false  定时关机   默认为true

    String hour = "";
    String minute = "";

    int order;//定时列表的排序
    int size;//定时器列表的长度
    String weeks = "";//周期

    String wes = "";//截断的周期

    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;
    private String seven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_switch_timer);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        controlId = bundle.getString("controlId");
        detailsTimeList = (List<DevicesDetailsTime>) bundle.getSerializable("list");
        from = bundle.getString("from");
        Log.e("===来自", from);
        size = bundle.getInt("size");
        Log.e("===size", size + "");
        divider();
        initLoopView();
        initOpenOrClose();
    }

    private void divider() {
        if ("lv".equals(from)) {
            list = (List<TimerEntity>) bundle.getSerializable("timeList");
            position = bundle.getInt("position");
            timer = list.get(position).getTimer();
            hour = list.get(position).getHour();
            minute = list.get(position).getMinute();
            order = list.get(position).getOrder();
//            weeks = list.get(position).getOrg_deviceWeek();
//            if (weeks.length() > 0) {
//                for (int i = 0; i < weeks.length(); i++) {
//                    wes = weeks.substring(i, i + 1);
//                    if ("1".equals(wes)) {
//                        oneCb.setChecked(true);
//                    } else if ("2".equals(wes)) {
//                        twoCb.setChecked(true);
//                    } else if ("3".equals(wes)) {
//                        threeCb.setChecked(true);
//                    } else if ("4".equals(wes)) {
//                        fourCb.setChecked(true);
//                    } else if ("5".equals(wes)) {
//                        fiveCb.setChecked(true);
//                    } else if ("6".equals(wes)) {
//                        sixCb.setChecked(true);
//                    } else if ("7".equals(wes)) {
//                        sevenCb.setChecked(true);
//                    }
//                }
//            }
        } else {
            order = size + 1;
        }
    }

    private void initLoopView() {
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                times.add("0" + i);
            } else {
                times.add(i + "");
            }
        }

        loopView.setItems(times);

        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                times2.add("0" + i);
            } else {
                times2.add(i + "");
            }
        }
        loopView2.setItems(times2);

        if (!"".equals(hour)) {
            loopView.setInitPosition(Integer.valueOf(hour));
            lp1 = hour;
        } else {
            loopView.setInitPosition(0);
            lp1 = "00";
        }

        if (!"".equals(minute)) {
            loopView2.setInitPosition(Integer.valueOf(minute));
            lp2 = minute;
        } else {
            loopView2.setInitPosition(0);
            lp2 = "00";
        }


        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index < 10) {
                    lp1 = "0" + index;
                } else {
                    lp1 = index + "";
                }

                Log.e("==lp1", lp1);
            }
        });

        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index < 10) {
                    lp2 = "0" + index;
                } else {
                    lp2 = index + "";
                }

                Log.e("==lp2", lp2);
            }
        });

        Log.e("-----lp1", lp1);
        Log.e("-----lp2", lp2);
    }

    private void initOpenOrClose() {
        if ("1".equals(timer)) {
            timerOpenIv.setVisibility(View.VISIBLE);
            timerCloseIv.setVisibility(View.GONE);
        } else if ("2".equals(timer)){
            timerCloseIv.setVisibility(View.VISIBLE);
            timerOpenIv.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.backRel, R.id.saveTimerRel, R.id.timerCloseRel, R.id.timerOpenRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                Intent intent0 = new Intent();
                setResult(1, intent0);
                finish();
                break;
            case R.id.saveTimerRel:
                if (order > detailsTimeList.size()) {
                    Toast.makeText(AddSwitchTimerActivity.this, "超出定时器最大数量，不能再添加", Toast.LENGTH_SHORT).show();
                } else {
                    save();
                }
                break;
            case R.id.timerCloseRel:
                timer = "2";
                initOpenOrClose();
                break;
            case R.id.timerOpenRel:
                timer = "1";
                initOpenOrClose();
                break;
        }
    }

    private void save() {
        String deciceTime = lp1 + lp2;
//        if (oneCb.isChecked()) {
//            one = "1";
//        } else {
//            one = "";
//        }
//
//        if (twoCb.isChecked()) {
//            two = "2";
//        } else {
//            two = "";
//        }
//
//        if (threeCb.isChecked()) {
//            three = "3";
//        } else {
//            three = "";
//        }
//
//        if (fourCb.isChecked()) {
//            four = "4";
//        } else {
//            four = "";
//        }
//
//        if (fiveCb.isChecked()) {
//            five = "5";
//        } else {
//            five = "";
//        }
//
//        if (sixCb.isChecked()) {
//            six = "6";
//        } else {
//            six = "";
//        }
//
//        if (sevenCb.isChecked()) {
//            seven = "7";
//        } else {
//            seven = "";
//        }
//        String deviceWeek = one + two + three + four + five + six + seven;
//
//        Log.e("===最新周期", deviceWeek);

        String tt="";
        if ("1".equals(timer)) {
            tt = "1";
        } else if ("2".equals(timer)){
            tt = "2";
        }
        SuccinctProgress.showSuccinctProgress(this, "正在保存...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.SET_TIME);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("deviceId", controlId);
        params.addQueryStringParameter("resourceFlag", detailsTimeList.get(order - 1).getResourceFlag());
        params.addQueryStringParameter("deviceStation", detailsTimeList.get(order - 1).getDeviceStation());
        params.addQueryStringParameter("deviceCode", detailsTimeList.get(order - 1).getDeviceCode());
        int adress = (detailsTimeList.get(order-1).getSwitchNumber() - 1) * 80 +
                Integer.valueOf(detailsTimeList.get(order-1).getRegisterAddress());
        String str = String.format("%04d",adress);
        Log.e("===转为int的adress",str);
        params.addQueryStringParameter("registerAddress", str);
//        params.addQueryStringParameter("registerAddress", detailsTimeList.get(order-1).getRegisterAddress());
        params.addQueryStringParameter("registerNumber", detailsTimeList.get(order - 1).getRegisterNumber());
        params.addQueryStringParameter("permission", detailsTimeList.get(order - 1).getPermission());
        params.addQueryStringParameter("deviceKind", detailsTimeList.get(order - 1).getEkName());
        params.addQueryStringParameter("deviceFirm", detailsTimeList.get(order - 1).getDeviceFirm());
        params.addQueryStringParameter("deviceTime", deciceTime);
//        if ("".equals(deviceWeek) || deviceWeek == null) {
//            params.addQueryStringParameter("deviceWeek", "0");
//        } else {
//            params.addQueryStringParameter("deviceWeek", deviceWeek);
//        }
        params.addQueryStringParameter("deviceWeek", "no");

        params.addQueryStringParameter("deviceType", tt);
        params.addQueryStringParameter("temperature", detailsTimeList.get(order - 1).getTemperature().equals("null")?"0":detailsTimeList.get(order - 1).getTemperature());
        params.addQueryStringParameter("order", order + "");
        if ("lv".equals(from)) {
            params.addQueryStringParameter("addFlag", "old");
            params.addQueryStringParameter("timeId", list.get(position).getId());
            params.addQueryStringParameter("deviceStatus", list.get(position).getDeviceStatus());
        } else {
            params.addQueryStringParameter("addFlag", "new");
            params.addQueryStringParameter("timeId", "0");
            params.addQueryStringParameter("deviceStatus", "1");
        }

        Log.e("==access_token", AppUtils.getAccessToken(this));
        Log.e("==temperature", detailsTimeList.get(order - 1).getTemperature().equals("null")?"0":detailsTimeList.get(order - 1).getTemperature());
        Log.e("==controlId", controlId);
        Log.e("==resourceFlag", detailsTimeList.get(order - 1).getResourceFlag());
        Log.e("==deviceStation", detailsTimeList.get(order - 1).getDeviceStation());
        Log.e("==deviceCode", detailsTimeList.get(order - 1).getDeviceCode());
        Log.e("==registerAddress", detailsTimeList.get(order - 1).getRegisterAddress());
        Log.e("==registerNumber", detailsTimeList.get(order - 1).getRegisterNumber());
        Log.e("==permission", detailsTimeList.get(order - 1).getPermission());
        Log.e("==deviceKind", detailsTimeList.get(order - 1).getEkName());
        ;
        Log.e("==deviceFirm", detailsTimeList.get(order - 1).getDeviceFirm());
        Log.e("==deviceTime", deciceTime);
        Log.e("==deviceType", tt);
//        Log.e("==temperature", detailsTimeList.get(order - 1).getTemperature());

        Log.e("==order", order + "");
        Log.e("==deviceStatus", "0");

        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code) || "操作成功".equals(code)) {
                        Intent intent0 = new Intent();
                        setResult(0, intent0);
                        finish();
                    } else {
                        Toast.makeText(AddSwitchTimerActivity.this, code, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SuccinctProgress.dismiss();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(AddSwitchTimerActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                SuccinctProgress.dismiss();
            }

            @Override
            public void onFinished() {
                SuccinctProgress.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent0 = new Intent();
        setResult(1, intent0);
        finish();
        super.onBackPressed();
    }
}
