package com.dechnic.omsdcapp.device_controller.temperature.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class AddTimerActivity extends BaseActivity {
    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.saveTimerRel)
    RelativeLayout saveTimerRel;
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
    @Bind(R.id.timerOpenIv)
    ImageView timerOpenIv;
    @Bind(R.id.timerCloseIv)
    ImageView timerCloseIv;
    @Bind(R.id.wenduTv)
    TextView wenduTv;
    @Bind(R.id.activity_add_timer)
    RelativeLayout activityAddTimer;
    @Bind(R.id.timerCloseRel)
    RelativeLayout timerCloseRel;
    @Bind(R.id.timerOpenRel)
    RelativeLayout timerOpenRel;
    @Bind(R.id.wenduRel)
    RelativeLayout wenduRel;

    @Bind(R.id.loopView)
    LoopView loopView;
    @Bind(R.id.loopView2)
    LoopView loopView2;

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
    String temp = "";
    int size;//定时器列表的长度
    String weeks = "";//周期

    String wes = "";//截断的周期


    private View view;
    private RelativeLayout cacleRel;
    private RelativeLayout sureRel;
    private PopupWindow popupWindow;
    private LoopView tempLoopView;
    List<String> tempLoopViewList = new ArrayList<>();
    private int temperature;

    int[] location = new int[2];
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
        setContentView(R.layout.activity_add_timer);
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
        initPopupWindow();
    }

    private void divider() {
        if ("lv".equals(from)) {
            list = (List<TimerEntity>) bundle.getSerializable("timeList");
            position = bundle.getInt("position");
            timer = list.get(position).getTimer();
            hour = list.get(position).getHour();
            minute = list.get(position).getMinute();
            order = list.get(position).getOrder();
            temp = detailsTimeList.get(order - 1).getTemperature();
            Log.e("===传来的温度", temp);
            temperature = Integer.valueOf(temp);
            wenduTv.setText(temp + "℃");
            weeks = list.get(position).getOrg_deviceWeek();
            if (weeks.length() > 0) {
                for (int i = 0; i < weeks.length(); i++) {
                    wes = weeks.substring(i, i + 1);
                    if ("1".equals(wes)) {
                        oneCb.setChecked(true);
                    } else if ("2".equals(wes)) {
                        twoCb.setChecked(true);
                    } else if ("3".equals(wes)) {
                        threeCb.setChecked(true);
                    } else if ("4".equals(wes)) {
                        fourCb.setChecked(true);
                    } else if ("5".equals(wes)) {
                        fiveCb.setChecked(true);
                    } else if ("6".equals(wes)) {
                        sixCb.setChecked(true);
                    } else if ("7".equals(wes)) {
                        sevenCb.setChecked(true);
                    }
                }
            }
        } else {
            order = size + 1;
            temperature = Integer.valueOf(bundle.getString("temperature"));
            wenduTv.setText(temperature + "℃");
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

    @OnClick({R.id.backRel, R.id.saveTimerRel, R.id.timerCloseRel, R.id.timerOpenRel, R.id.wenduRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                Intent intent0 = new Intent();
                setResult(1, intent0);
                finish();
                break;
            case R.id.saveTimerRel:

                if (order > detailsTimeList.size()) {
                    Toast.makeText(AddTimerActivity.this, "超出定时器最大数量，不能再添加", Toast.LENGTH_SHORT).show();
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
            case R.id.wenduRel:
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
        }
    }

    private void save() {
        String deciceTime = lp1 + lp2;
        if (oneCb.isChecked()) {
            one = "1";
        } else {
            one = "";
        }

        if (twoCb.isChecked()) {
            two = "2";
        } else {
            two = "";
        }

        if (threeCb.isChecked()) {
            three = "3";
        } else {
            three = "";
        }

        if (fourCb.isChecked()) {
            four = "4";
        } else {
            four = "";
        }

        if (fiveCb.isChecked()) {
            five = "5";
        } else {
            five = "";
        }

        if (sixCb.isChecked()) {
            six = "6";
        } else {
            six = "";
        }

        if (sevenCb.isChecked()) {
            seven = "7";
        } else {
            seven = "";
        }
        String deviceWeek = one + two + three + four + five + six + seven;

        Log.e("===最新周期", deviceWeek);
        String temp = temperature + "";
        String tt="0";
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
        params.addQueryStringParameter("registerAddress", detailsTimeList.get(order - 1).getRegisterAddress());
        params.addQueryStringParameter("registerNumber", detailsTimeList.get(order - 1).getRegisterNumber());
        params.addQueryStringParameter("permission", detailsTimeList.get(order - 1).getPermission());
        params.addQueryStringParameter("deviceKind", detailsTimeList.get(order - 1).getEkName());
        params.addQueryStringParameter("deviceFirm", detailsTimeList.get(order - 1).getDeviceFirm());
        params.addQueryStringParameter("deviceTime", deciceTime);
        if ("".equals(deviceWeek) || deviceWeek == null) {
            params.addQueryStringParameter("deviceWeek", "0");
        } else {
            params.addQueryStringParameter("deviceWeek", deviceWeek);
        }

        params.addQueryStringParameter("deviceType", tt);
        params.addQueryStringParameter("temperature", temperature + "");
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

        params.setConnectTimeout(5000);
        params.setReadTimeout(5000);

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
                        Toast.makeText(AddTimerActivity.this, code, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SuccinctProgress.dismiss();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(AddTimerActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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

    private void initPopupWindow() {
        view = LayoutInflater.from(this).inflate(R.layout.install_temp_popup, null);
        cacleRel = (RelativeLayout) view.findViewById(R.id.cacleRel);
        sureRel = (RelativeLayout) view.findViewById(R.id.sureRel);
        tempLoopView = (LoopView) view.findViewById(R.id.loopView);
        for (int i = 5; i < 41; i++) {
            tempLoopViewList.add(i + "");
        }
        tempLoopView.setItems(tempLoopViewList);

        if (!"".equals(temp) && temp != null) {
            temperature = Integer.valueOf(temp);
            tempLoopView.setInitPosition(temperature - 5);
        } else {
            temperature = 5;
            tempLoopView.setInitPosition(0);
        }

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.ins_popup_anim);

        tempLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                temperature = index + 5;
            }
        });

        cacleRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                wenduTv.setText(temperature + "℃");
            }
        });
        sureRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                wenduTv.setText(temperature + "℃");
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
