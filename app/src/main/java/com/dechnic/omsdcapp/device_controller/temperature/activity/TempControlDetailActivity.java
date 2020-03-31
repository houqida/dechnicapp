package com.dechnic.omsdcapp.device_controller.temperature.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.Base2Activity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.entity.ControlEntity;
import com.dechnic.omsdcapp.entity.DevicesDetails;
import com.dechnic.omsdcapp.entity.DevicesDetailsTime;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.ShowPercentSmallView;
import com.dechnic.omsdcapp.widget.ShowPercentView;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("ResourceAsColor")
public class TempControlDetailActivity extends Base2Activity {

    //在线，打开时 的控件
    @Bind(R.id.backRelLay)
    RelativeLayout backRelLay;
    @Bind(R.id.popupRelLay)
    RelativeLayout popupRelLay;
    @Bind(R.id.modelTv)
    TextView modelTv;
    @Bind(R.id.modelRel)
    RelativeLayout modelRel;
    @Bind(R.id.gearTv)
    TextView gearTv;
    @Bind(R.id.gearRel)
    RelativeLayout gearRel;
    @Bind(R.id.temTv)
    TextView temTv;
    @Bind(R.id.tempRel)
    RelativeLayout tempRel;
    @Bind(R.id.coldIv)
    ImageView coldIv;
    @Bind(R.id.coldTv)
    TextView coldTv;
    @Bind(R.id.coldRel)
    RelativeLayout coldRel;
    @Bind(R.id.hotIv)
    ImageView hotIv;
    @Bind(R.id.hotTv)
    TextView hotTv;
    @Bind(R.id.hotRel)
    RelativeLayout hotRel;
    @Bind(R.id.windyIv)
    ImageView windyIv;
    @Bind(R.id.windyTv)
    TextView windyTv;
    @Bind(R.id.windyRel)
    RelativeLayout windyRel;
    @Bind(R.id.highIv)
    ImageView highIv;
    @Bind(R.id.highTv)
    TextView highTv;
    @Bind(R.id.highRel)
    RelativeLayout highRel;
    @Bind(R.id.lowIv)
    ImageView lowIv;
    @Bind(R.id.lowTv)
    TextView lowTv;
    @Bind(R.id.lowRel)
    RelativeLayout lowRel;
    @Bind(R.id.middleIv)
    ImageView middleIv;
    @Bind(R.id.middleTv)
    TextView middleTv;
    @Bind(R.id.middleRel)
    RelativeLayout middleRel;
    @Bind(R.id.autoIv)
    ImageView autoIv;
    @Bind(R.id.autoTv)
    TextView autoTv;
    @Bind(R.id.aotuRel)
    RelativeLayout aotuRel;
    @Bind(R.id.energyIv)
    ImageView energyIv;
    @Bind(R.id.energyTv)
    TextView energyTv;
    @Bind(R.id.energyRel)
    RelativeLayout energyRel;
    @Bind(R.id.sleepIv)
    ImageView sleepIv;
    @Bind(R.id.sleepTv)
    TextView sleepTv;
    @Bind(R.id.sleepRel)
    RelativeLayout sleepRel;
    @Bind(R.id.switvhIv)
    ImageView switvhIv;
    @Bind(R.id.switchTv)
    TextView switchTv;
    @Bind(R.id.switchRel)
    RelativeLayout switchRel;
    @Bind(R.id.activity_temp_control_detail)
    LinearLayout activityTempControlDetail;
    @Bind(R.id.titleBar)
    RelativeLayout titleBar;
    @Bind(R.id.deviceTv)
    TextView deviceTv;
    @Bind(R.id.onLine_Open_Lay)
    LinearLayout onLineOpenLay;
    @Bind(R.id.myShowPercentView)
    ShowPercentView myShowPercentView;
    @Bind(R.id.sub_temp_iv)
    ImageView subTempIv;
    @Bind(R.id.add_temp_iv)
    ImageView addTempIv;
    @Bind(R.id.percentTempTv)
    TextView percentTempTv;
    @Bind(R.id.sub_temp_Rel)
    RelativeLayout subTempRel;
    @Bind(R.id.add_temp_Rel)
    RelativeLayout addTempRel;
    @Bind(R.id.percentTempIv)
    ImageView percentTempIv;

    //在线，关闭状态的控件
    @Bind(R.id.close_backRelLay)
    RelativeLayout closeBackRelLay;
    @Bind(R.id.close_deviceTv)
    TextView closeDeviceTv;
    @Bind(R.id.close_popupRelLay)
    RelativeLayout closePopupRelLay;
    @Bind(R.id.close_switchRel)
    RelativeLayout closeSwitchRel;
    @Bind(R.id.activity_temp_control_detail_close)
    LinearLayout activityTempControlDetailClose;
    @Bind(R.id.close_titleBar)
    RelativeLayout closeTitleBar;
    @Bind(R.id.close_switvhIv)
    ImageView close_switvhIv;
    @Bind(R.id.close_switchTv)
    TextView close_switchTv;

    //离线状态
    @Bind(R.id.off_backRelLay)
    RelativeLayout offBackRelLay;
    @Bind(R.id.off_deviceTv)
    TextView offDeviceTv;
    @Bind(R.id.activity_temp_control_detail_offline)
    LinearLayout activityTempControlDetailOffline;
    @Bind(R.id.off_modelTv)
    TextView offModelTv;
    @Bind(R.id.off_gearTv)
    TextView offGearTv;
    @Bind(R.id.off_temTv)
    TextView offTemTv;
    @Bind(R.id.off_switchTv)
    TextView offSwitchTv;
    @Bind(R.id.myShowPercentView_off)
    ShowPercentView myShowPercentView_off;
    @Bind(R.id.percentTempTv_off)
    TextView percentTempTv_off;
    @Bind(R.id.percentTempIv_off)
    ImageView percentTempIv_off;
    @Bind(R.id.close_sleepTv)
    TextView closeSleepTv;
    @Bind(R.id.off_sleepTv)
    TextView offSleepTv;
    @Bind(R.id.showPercentSmallView)
    ShowPercentSmallView showPercentSmallView;
    @Bind(R.id.showPercentSmallView_close)
    ShowPercentSmallView showPercentSmallViewClose;
    @Bind(R.id.showPercentSmallView_off)
    ShowPercentSmallView showPercentSmallViewOff;


    private RelativeLayout timeRel, limitTempRel, banRel;
    private PopupWindow popupWindow;
    private int width;

    boolean online;
    boolean openOrClose;

    private ControlEntity entity = new ControlEntity();
    Bundle bundle;

    int percent = 0;
    String controlId = "";
    String devicename = "";
    String ekName = "";
    String deviceFirm = "";
    String isOn = "";
    private List<DevicesDetails> list;
    int cold, hot, wind, auto, low, middle, high, sleep, thirft,
            disable_open, disable_close, t_limit, time, open, close, temp;
    private String speed;
    private String pattern;
    private String temperature;
    private String cold_registerAddress;
    private String cold_deviceStation;
    private String cold_deviceCode;
    private String cold_registerNumber;
    private String cold_permission;

    private String hot_registerAddress;
    private String hot_deviceStation;
    private String hot_deviceCode;
    private String hot_registerNumber;
    private String hot_permission;

    private String wind_registerAddress;
    private String wind_deviceStation;
    private String wind_deviceCode;
    private String wind_registerNumber;
    private String wind_permission;

    private String auto_registerAddress;
    private String auto_deviceStation;
    private String auto_deviceCode;
    private String auto_registerNumber;
    private String auto_permission;

    private String low_registerAddress;
    private String low_deviceStation;
    private String low_deviceCode;
    private String low_registerNumber;
    private String low_permission;

    private String middle_registerAddress;
    private String middle_deviceStation;
    private String middle_deviceCode;
    private String middle_registerNumber;
    private String midlle_permission;

    private String high_registerAddress;
    private String high_deviceStation;
    private String high_deviceCode;
    private String high_registerNumber;
    private String high_permission;

    private String sleep_registerAddress;
    private String sleep_deviceStation;
    private String sleep_deviceCode;
    private String sleep_registerNumber;
    private String sleep_permission;

    private String thirft_registerAddress;
    private String thirft_deviceStation;
    private String thirft_deviceCode;
    private String thirft_registerNumber;
    private String thirft_permission;
    private String thirft_resourceFlag;
    private String sleep_resourceFlag;
    private String middle_resourceFlag;
    private String high_resourceFlag;
    private String auto_resourceFlag;
    private String low_resourceFlag;
    private String wind_resourceFlag;
    private String hot_resourceFlag;
    private String cold_resourceFlag;
    private String open_registerAddress;
    private String open_deviceStation;
    private String open_deviceCode;
    private String open_registerNumber;
    private String open_permission;
    private String open_resourceFlag;
    private String close_registerAddress;
    private String close_deviceStation;
    private String close_deviceCode;
    private String close_registerNumber;
    private String close_permission;
    private String close_resourceFlag;
    private String temperature_registerAddress;
    private String temperature_deviceStation;
    private String temperature_deviceCode;
    private String temperature_registerNumber;
    private String temperature_permission;
    private String temperature_resourceFlag;
    private int tMax;
    private int tMin;
    private RelativeLayout openBanRel;
    private String dis_open_registerAddress;
    private String dis_open_deviceStation;
    private String dis_open_deviceCode;
    private String dis_open_registerNumber;
    private String dis_open_permission;
    private String dis_open_resourceFlag;
    private String dis_close_registerAddress;
    private String dis_close_deviceStation;
    private String dis_close_deviceCode;
    private String dis_close_registerNumber;
    private String dis_close_permission;
    private String dis_close_resourceFlag;
    private ImageView dingshiIv;
    private TextView dingshiTv;
    private ImageView tempIv;
    private TextView tempTvs;
    private ImageView banIv;
    private TextView banTv;
    private ImageView openBanIv;
    private TextView openBanTv;
    private String t_limit_registerAddress;
    private String t_limit_deviceStation;
    private String t_limit_deviceCode;
    private String t_limit_registerNumber;
    private String t_limit_permission;
    private String t_limit_resourceFlag;

    private List<DevicesDetailsTime> detailsTimeList;

    private boolean isClickable = false;

    private static final int PATTARN = 0;
    private static final int SPEED = 1;
    private static final int ENERGY = 2;
    private static final int SLEEP = 3;
    private static final int CLOSE = 4;
    private static final int OPEN = 5;
    private static final int SUBTEMP = 6;
    private static final int ADDTEMP = 7;
    private static final int DISABLE_OPEN = 11;
    private static final int DISABLE_CLOSE = 12;
    private static final int WAKE = 10;

    private long endTime;
    private int twice = 0;
    private int position;
    private String indoorTemperature;
    private int panelDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_control_detail);
        setTranslucent(this);
        ButterKnife.bind(this);
//        initPercentView();
        bundle = getIntent().getExtras();
        online = bundle.getBoolean("isOnline");
        openOrClose = bundle.getBoolean("isOpen");
        controlId = bundle.getString("controlId");
        devicename = bundle.getString("devicename");
        ekName = bundle.getString("ekName");
        deviceFirm = bundle.getString("deviceFirm");
        isOn = bundle.getString("isOn");
        position = bundle.getInt("position");
        panelDisable = bundle.getInt("panelDisable");

        getDetails();
        status();//判断三种情况   在线：打开/关闭    离线
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();//计算屏幕宽度

    }

    private void initPercentView() {
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();

        //在线、打开
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myShowPercentView.getLayoutParams();
        params.width = d.getWidth() / 4 * 3;
        params.height = d.getWidth() / 4 * 3;
        myShowPercentView.setLayoutParams(params);

        RelativeLayout.LayoutParams params_samll = (RelativeLayout.LayoutParams) showPercentSmallView.getLayoutParams();
        params_samll.width = d.getWidth() / 3 * 2;
        params_samll.height = d.getWidth() / 3 * 2;
        showPercentSmallView.setLayoutParams(params_samll);


        //离线
        RelativeLayout.LayoutParams params_off = (RelativeLayout.LayoutParams) myShowPercentView_off.getLayoutParams();
        params_off.width = d.getWidth() / 4 * 3;
        params_off.height = d.getWidth() / 4 * 3;
        myShowPercentView_off.setLayoutParams(params_off);

        RelativeLayout.LayoutParams params_small_off = (RelativeLayout.LayoutParams) showPercentSmallViewOff.getLayoutParams();
        params_small_off.width = d.getWidth() / 2;
        params_small_off.height = d.getWidth() / 2;
        showPercentSmallViewOff.setLayoutParams(params_small_off);


    }

    private void getDetails() {
        SuccinctProgress.showSuccinctProgress(this, "正在加载......", SuccinctProgress.THEME_LINE, true, true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        } else {
            RequestParams details = new RequestParams(HttpURL.Url(this) + HttpURL.DEVICEDETAILS);
            details.addHeader("access_token", AppUtils.getAccessToken(this));
            details.addQueryStringParameter("deviceId", controlId);
            x.http().request(HttpMethod.GET, details, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("==jsonresult", result);
                    pullJson(result);
                    showDetails();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Toast.makeText(TempControlDetailActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();

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
    }

    private void pullJson(String result) {
        list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            Log.e("==array", jsonArray.toString());
            detailsTimeList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                DevicesDetails devicesDetails = new DevicesDetails();
                devicesDetails.setRegisterAddress(object.optString("registerAddress"));
                devicesDetails.setIsOn(object.optString("isOn"));
                devicesDetails.setTemperature(object.optString("temperature"));
                temperature = object.optString("temperature");
                devicesDetails.setPattern(object.optString("pattern"));
                pattern = object.optString("pattern");
                devicesDetails.setResourceName(object.optString("resourceName"));
                devicesDetails.setResourceFlag(object.optString("resourceFlag"));
                String resourceFlagtime = object.optString("resourceFlag");
                Log.e("==flag", object.optString("resourceFlag"));
                devicesDetails.setPermission(object.optString("permission"));
                devicesDetails.setRegisterNumber(object.optString("registerNumber"));
                devicesDetails.setDeviceCode(object.optString("deviceCode"));
                devicesDetails.setSpeed(object.optString("speed"));
                devicesDetails.setEkName(object.optString("deviceKind"));
                devicesDetails.setDeviceFirm(object.optString("deviceFirm"));
                devicesDetails.setDeviceStation(object.optString("deviceStation"));
                devicesDetails.setIndoorTemperature(object.optString("indoorTemperature"));
                indoorTemperature = object.optString("indoorTemperature");
                panelDisable = object.optInt("panelDisable");

                if ("春泉".equals(object.optString("deviceFirm"))) {
                    if ("time1".equals(resourceFlagtime) || "time2".equals(resourceFlagtime) || "time3".equals(resourceFlagtime)
                            || "time4".equals(resourceFlagtime) || "time5".equals(resourceFlagtime) || "time6".equals(resourceFlagtime)
                            || "time7".equals(resourceFlagtime) || "time8".equals(resourceFlagtime) || "time9".equals(resourceFlagtime)
                            || "time10".equals(resourceFlagtime) || "time11".equals(resourceFlagtime) || "time12".equals(resourceFlagtime)
                            ) {
                        DevicesDetailsTime time = new DevicesDetailsTime();
                        time.setRegisterAddress(object.optString("registerAddress"));
                        time.setTemperature(object.optString("temperature"));
                        time.setResourceFlag(object.optString("resourceFlag"));
                        time.setPermission(object.optString("permission"));
                        time.setRegisterNumber(object.optString("registerNumber"));
                        time.setDeviceCode(object.optString("deviceCode"));
                        time.setEkName(object.optString("deviceKind"));
                        time.setDeviceFirm(object.optString("deviceFirm"));
                        time.setDeviceStation(object.optString("deviceStation"));

                        detailsTimeList.add(time);

                    }
                } else {
                    if ("time1".equals(resourceFlagtime) || "time2".equals(resourceFlagtime) || "time3".equals(resourceFlagtime)
                            || "time4".equals(resourceFlagtime) || "time5".equals(resourceFlagtime) || "time6".equals(resourceFlagtime)) {
                        DevicesDetailsTime time = new DevicesDetailsTime();
                        time.setRegisterAddress(object.optString("registerAddress"));
                        time.setTemperature(object.optString("temperature"));
                        time.setResourceFlag(object.optString("resourceFlag"));
                        time.setPermission(object.optString("permission"));
                        time.setRegisterNumber(object.optString("registerNumber"));
                        time.setDeviceCode(object.optString("deviceCode"));
                        time.setEkName(object.optString("deviceKind"));
                        time.setDeviceFirm(object.optString("deviceFirm"));
                        time.setDeviceStation(object.optString("deviceStation"));
                        detailsTimeList.add(time);
                    }
                }

                speed = object.optString("speed");
                tMax = object.optInt("tMax");
                Log.e("==最高限温", tMax + "");
                tMin = object.optInt("tMin");
                Log.e("==最低限温", tMin + "");
                devicesDetails.settMax(tMax + "");
                devicesDetails.settMin(tMin + "");
                devicesDetails.setSpeed(speed);
                list.add(devicesDetails);
            }

            modelTv.setText("模式  " + pattern);
            if ("制冷".equals(pattern)) {
                percentTempIv.setImageResource(R.mipmap.tocold);
            } else if ("制热".equals(pattern)) {
                percentTempIv.setImageResource(R.mipmap.tohot);
            } else if ("通风".equals(pattern)) {
                percentTempIv.setImageResource(R.mipmap.towindy);
            }
            gearTv.setText("档位  " + speed);
            temTv.setText("室温  " + indoorTemperature + "℃");
            percent = Integer.valueOf(temperature);
            percentTempTv.setText(temperature);
            for (int i = 1; i < percent + 1; i++) {
                myShowPercentView.setPercent(percent * 2);
            }
            myShowPercentView_off.setPercent(percent * 2);
            offModelTv.setText("模式  " + pattern);
            if ("制冷".equals(pattern)) {
                percentTempIv_off.setImageResource(R.mipmap.off_tocold);
            } else if ("制热".equals(pattern)) {
                percentTempIv_off.setImageResource(R.mipmap.off_tohot);
            } else if ("通风".equals(pattern)) {
                percentTempIv_off.setImageResource(R.mipmap.off_towindy);
            }
            offGearTv.setText("档位  " + speed);
            offTemTv.setText("室温  " + indoorTemperature + "℃");
            percentTempTv_off.setText(temperature);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails() {
        for (int i = 0; i < list.size(); i++) {
            Log.e("====ResourceFlag", list.get(i).getResourceFlag() + "");
            //判断权限
            if (Constants.PATTERN_COLD.equals(list.get(i).getResourceFlag())) {//制冷
                cold = 1;
                cold_registerAddress = list.get(i).getRegisterAddress();
                cold_deviceStation = list.get(i).getDeviceStation();
                cold_deviceCode = list.get(i).getDeviceCode();
                cold_registerNumber = list.get(i).getRegisterNumber();
                cold_permission = list.get(i).getPermission();
                cold_resourceFlag = list.get(i).getResourceFlag();
                Log.e("====cold", cold + "");
            } else if (Constants.PATTERN_HOT.equals(list.get(i).getResourceFlag())) {//制热
                hot = 1;
                hot_registerAddress = list.get(i).getRegisterAddress();
                hot_deviceStation = list.get(i).getDeviceStation();
                hot_deviceCode = list.get(i).getDeviceCode();
                hot_registerNumber = list.get(i).getRegisterNumber();
                hot_permission = list.get(i).getPermission();
                hot_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.PATTERN_WIND.equals(list.get(i).getResourceFlag())) {//通风
                wind = 1;
                wind_registerAddress = list.get(i).getRegisterAddress();
                wind_deviceStation = list.get(i).getDeviceStation();
                wind_deviceCode = list.get(i).getDeviceCode();
                wind_registerNumber = list.get(i).getRegisterNumber();
                wind_permission = list.get(i).getPermission();
                wind_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.SPEED_AUTO.equals(list.get(i).getResourceFlag())) {//自动
                auto = 1;
                auto_registerAddress = list.get(i).getRegisterAddress();
                auto_deviceStation = list.get(i).getDeviceStation();
                auto_deviceCode = list.get(i).getDeviceCode();
                auto_registerNumber = list.get(i).getRegisterNumber();
                auto_permission = list.get(i).getPermission();
                auto_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.SPEED_LOW.equals(list.get(i).getResourceFlag())) {//低档
                low = 1;
                low_registerAddress = list.get(i).getRegisterAddress();
                low_deviceStation = list.get(i).getDeviceStation();
                low_deviceCode = list.get(i).getDeviceCode();
                low_registerNumber = list.get(i).getRegisterNumber();
                low_permission = list.get(i).getPermission();
                low_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.SPEED_MIDDLE.equals(list.get(i).getResourceFlag())) {//中档
                middle = 1;
                middle_registerAddress = list.get(i).getRegisterAddress();
                middle_deviceStation = list.get(i).getDeviceStation();
                middle_deviceCode = list.get(i).getDeviceCode();
                middle_registerNumber = list.get(i).getRegisterNumber();
                midlle_permission = list.get(i).getPermission();
                middle_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.SPEED_HIGH.equals(list.get(i).getResourceFlag())) {//高档
                high = 1;
                high_registerAddress = list.get(i).getRegisterAddress();
                high_deviceStation = list.get(i).getDeviceStation();
                high_deviceCode = list.get(i).getDeviceCode();
                high_registerNumber = list.get(i).getRegisterNumber();
                high_permission = list.get(i).getPermission();
                high_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.STATE_SLEEP.equals(list.get(i).getResourceFlag())) {//睡眠
                sleep = 1;
                sleep_registerAddress = list.get(i).getRegisterAddress();
                sleep_deviceStation = list.get(i).getDeviceStation();
                sleep_deviceCode = list.get(i).getDeviceCode();
                sleep_registerNumber = list.get(i).getRegisterNumber();
                sleep_permission = list.get(i).getPermission();
                Log.e("===sleep_permission", sleep_permission);
                sleep_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.STATE_THRITF.equals(list.get(i).getResourceFlag())) {//节能
                thirft = 1;
                thirft_registerAddress = list.get(i).getRegisterAddress();
                thirft_deviceStation = list.get(i).getDeviceStation();
                thirft_deviceCode = list.get(i).getDeviceCode();
                thirft_registerNumber = list.get(i).getRegisterNumber();
                thirft_permission = list.get(i).getPermission();
                thirft_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.STATE_OPEN.equals(list.get(i).getResourceFlag())) {//打开
                open_registerAddress = list.get(i).getRegisterAddress();
                open_deviceStation = list.get(i).getDeviceStation();
                open_deviceCode = list.get(i).getDeviceCode();
                open_registerNumber = list.get(i).getRegisterNumber();
                open_permission = list.get(i).getPermission();
                open_resourceFlag = list.get(i).getResourceFlag();

            } else if (Constants.STATE_CLOSE.equals(list.get(i).getResourceFlag())) {//关闭
                close_registerAddress = list.get(i).getRegisterAddress();
                close_deviceStation = list.get(i).getDeviceStation();
                close_deviceCode = list.get(i).getDeviceCode();
                close_registerNumber = list.get(i).getRegisterNumber();
                close_permission = list.get(i).getPermission();
                close_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.temperature.equals(list.get(i).getResourceFlag())) {//温度
                temp = 1;
                temperature_registerAddress = list.get(i).getRegisterAddress();
                temperature_deviceStation = list.get(i).getDeviceStation();
                temperature_deviceCode = list.get(i).getDeviceCode();
                temperature_registerNumber = list.get(i).getRegisterNumber();
                temperature_permission = list.get(i).getPermission();
                temperature_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.DISABLE_OPEN.equals(list.get(i).getResourceFlag())) {//禁用面板-打开,即面板不可使用
                disable_open = 1;
                dis_open_registerAddress = list.get(i).getRegisterAddress();
                dis_open_deviceStation = list.get(i).getDeviceStation();
                dis_open_deviceCode = list.get(i).getDeviceCode();
                dis_open_registerNumber = list.get(i).getRegisterNumber();
                dis_open_permission = list.get(i).getPermission();
                dis_open_resourceFlag = list.get(i).getResourceFlag();

            } else if (Constants.DISABLE_CLOSE.equals(list.get(i).getResourceFlag())) {//禁用面板-关闭,即面板可以使用
                disable_close = 1;
                dis_close_registerAddress = list.get(i).getRegisterAddress();
                dis_close_deviceStation = list.get(i).getDeviceStation();
                dis_close_deviceCode = list.get(i).getDeviceCode();
                dis_close_registerNumber = list.get(i).getRegisterNumber();
                dis_close_permission = list.get(i).getPermission();
                dis_close_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.T_LIMIT.equals(list.get(i).getResourceFlag())) {
                t_limit = 1;
                t_limit_registerAddress = list.get(i).getRegisterAddress();
                t_limit_deviceStation = list.get(i).getDeviceStation();
                t_limit_deviceCode = list.get(i).getDeviceCode();
                t_limit_registerNumber = list.get(i).getRegisterNumber();
                t_limit_permission = list.get(i).getPermission();
                t_limit_resourceFlag = list.get(i).getResourceFlag();
            } else if (Constants.TIME.equals(list.get(i).getResourceFlag())) {
                time = 1;
            }
        }

        showLayout();
    }

    private void status() {
        if (online) {//在线
            if (openOrClose) {//打开
                deviceTv.setText(devicename);
                activityTempControlDetail.setBackgroundResource(R.mipmap.temp_detail_online_open);
                onLineOpenLay.setVisibility(View.VISIBLE);
                activityTempControlDetailOffline.setVisibility(View.GONE);
                activityTempControlDetailClose.setVisibility(View.GONE);
                if (!"1".equals(isOn)) {
                    sleepTv.setText("唤醒");
                } else {
                    sleepTv.setText("睡眠");
                }
                SuccinctProgress.dismiss();
                //执行请求数据操作
            } else {//关闭
                //换背景,换布局
                closeDeviceTv.setText(devicename);
                activityTempControlDetail.setBackgroundResource(R.mipmap.temp_detail_online_close);
                onLineOpenLay.setVisibility(View.GONE);
                activityTempControlDetailOffline.setVisibility(View.GONE);
                activityTempControlDetailClose.setVisibility(View.VISIBLE);
                SuccinctProgress.dismiss();
            }
            initPopupWindow();

        } else {//离线
            offDeviceTv.setText(devicename);
            activityTempControlDetail.setBackgroundResource(R.mipmap.temp_detail_offline);
            activityTempControlDetailOffline.setVisibility(View.VISIBLE);
            onLineOpenLay.setVisibility(View.GONE);
            activityTempControlDetailClose.setVisibility(View.GONE);
            if (openOrClose) {
                offSwitchTv.setText("关闭");
            } else {
                offSwitchTv.setText("打开");
            }
        }
    }

    private void showLayout() {
        if (cold == 0) {
            coldIv.setImageResource(R.mipmap.tocold_no);
            coldTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (hot == 0) {
            hotIv.setImageResource(R.mipmap.tohot_no);
            hotTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (wind == 0) {
            windyIv.setImageResource(R.mipmap.towindy_no);
            windyTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (auto == 0) {
            autoIv.setImageResource(R.mipmap.toauto_no);
            autoTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (low == 0) {
            lowIv.setImageResource(R.mipmap.tolow_no);
            lowTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (middle == 0) {
            middleIv.setImageResource(R.mipmap.tomiddle_no);
            middleTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (high == 0) {
            highIv.setImageResource(R.mipmap.tohigh_no);
            highTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (thirft == 0) {
            energyIv.setImageResource(R.mipmap.toenergy_no);
            energyTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (sleep == 0) {
            sleepIv.setImageResource(R.mipmap.tosleep_no);
            sleepTv.setTextColor(Color.parseColor("#7ed1ff"));
        }
        if (temp == 0) {
            addTempIv.setImageResource(R.mipmap.add_temp_icon_no);
            subTempIv.setImageResource(R.mipmap.sub_temp_icon_no);
        }
        if (time == 0) {
            dingshiIv.setImageResource(R.mipmap.timimg_no);
            dingshiTv.setTextColor(Color.parseColor("#cccccc"));
        }

        if (t_limit == 0) {
            tempIv.setImageResource(R.mipmap.limit_no);
            tempTvs.setTextColor(Color.parseColor("#cccccc"));
        }
        if (disable_open == 0) {
            banIv.setImageResource(R.mipmap.ban_no);
            banTv.setTextColor(Color.parseColor("#cccccc"));
        }
        if (disable_close == 0) {
            openBanIv.setImageResource(R.mipmap.openban_no);
            openBanTv.setTextColor(Color.parseColor("#cccccc"));
        }
        isClickable = true;
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.detailpopup, null);
        timeRel = (RelativeLayout) view.findViewById(R.id.timeRel);
        limitTempRel = (RelativeLayout) view.findViewById(R.id.limitTempRel);
        banRel = (RelativeLayout) view.findViewById(R.id.banRel);
        openBanRel = (RelativeLayout) view.findViewById(R.id.openBanRel);

        dingshiIv = (ImageView) view.findViewById(R.id.dingshiIv);
        dingshiTv = (TextView) view.findViewById(R.id.dingshiTv);
        tempIv = (ImageView) view.findViewById(R.id.tempIv);
        tempTvs = (TextView) view.findViewById(R.id.tempTvs);
        banIv = (ImageView) view.findViewById(R.id.banIv);
        banTv = (TextView) view.findViewById(R.id.banTv);
        openBanIv = (ImageView) view.findViewById(R.id.openBanIv);
        openBanTv = (TextView) view.findViewById(R.id.openBanTv);

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);

        timeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time != 1) {
                    Toast.makeText(TempControlDetailActivity.this, "暂无此项功能", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TempControlDetailActivity.this, TimerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("controlId", controlId);
                    bundle.putSerializable("list", (Serializable) detailsTimeList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                popupWindow.dismiss();

            }
        });

        limitTempRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t_limit != 1) {
                    Toast.makeText(TempControlDetailActivity.this, "暂无此项功能", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TempControlDetailActivity.this, InstallTempActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tMax", tMax);
                    bundle.putInt("tMin", tMin);
                    bundle.putString("controlId", controlId);
                    bundle.putString("deviceKind", ekName);
                    bundle.putString("deviceFirm", deviceFirm);
                    bundle.putString("deviceStation", t_limit_deviceStation);
                    bundle.putString("deviceCode", t_limit_deviceCode);
                    bundle.putString("registerAddress", t_limit_registerAddress);
                    bundle.putString("registerNumber", t_limit_registerNumber);
                    Log.e("===ekName", ekName);
                    Log.e("===传递的tms", t_limit_registerAddress);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
                popupWindow.dismiss();

            }
        });
        banRel.setOnClickListener(new View.OnClickListener() {//打开 “禁用” 功能
            @Override
            public void onClick(View v) {

                if (disable_close != 1) {
                    Toast.makeText(TempControlDetailActivity.this, "暂无此项功能", Toast.LENGTH_SHORT).show();
                } else {
                    Operate(dis_open_deviceCode, dis_open_deviceStation,
                            dis_open_permission, dis_open_registerAddress,
                            dis_open_registerNumber, dis_open_resourceFlag, pattern, speed, DISABLE_OPEN);
                    popupWindow.dismiss();
                }

            }
        });

        openBanRel.setOnClickListener(new View.OnClickListener() {//关闭 “禁用” 功能
            @Override
            public void onClick(View v) {
                if (disable_open != 1) {
                    Toast.makeText(TempControlDetailActivity.this, "暂无此项功能", Toast.LENGTH_SHORT).show();
                } else {
                    Operate(dis_close_deviceCode, dis_close_deviceStation,
                            dis_close_permission, dis_close_registerAddress,
                            dis_close_registerNumber, dis_close_resourceFlag, pattern, speed, DISABLE_CLOSE);
                    popupWindow.dismiss();
                }

            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            int limit_high = Integer.valueOf(data.getStringExtra("limit_high"));
            int limit_low = Integer.valueOf(data.getStringExtra("limit_low"));
            tMin = limit_low;
            tMax = limit_high;
            if ("制冷".equals(pattern)) {//制冷模式  只需要判断最低温
                if (Integer.valueOf(temperature) < tMin) {
                    temperature = tMin + "";
                    percent = tMin;
                    percentTempTv.setText(percent + "");
                    myShowPercentView.setPercent(percent * 2);
                    Operate(temperature_deviceCode, temperature_deviceStation,
                            percent + "", temperature_registerAddress,
                            temperature_registerNumber, temperature_resourceFlag, pattern, speed, 8);
                }
            } else {//制热模式 判断最高温即可
                if (Integer.valueOf(temperature) > tMax) {
                    percent = tMax;
                    temperature = tMax + "";
                    percentTempTv.setText(percent + "");
                    myShowPercentView.setPercent(percent * 2);
                    Operate(temperature_deviceCode, temperature_deviceStation,
                            percent + "", temperature_registerAddress,
                            temperature_registerNumber, temperature_resourceFlag, pattern, speed, 8);
                }
            }
//            if (Integer.valueOf(temperature) < limit_low) {
//                percent = limit_low;
//                percentTempTv.setText(percent + "");
//
//                myShowPercentView.setPercent(percent * 2);
//                Operate(temperature_deviceCode, temperature_deviceStation,
//                        percent + "", temperature_registerAddress,
//                        temperature_registerNumber, temperature_resourceFlag, pattern, speed, 8);
//            } else if (Integer.valueOf(temperature) > limit_high) {
//                percent = limit_high;
//                percentTempTv.setText(percent + "");
//                myShowPercentView.setPercent(percent * 2);
//
//                Operate(temperature_deviceCode, temperature_deviceStation,
//                        percent + "", temperature_registerAddress,
//                        temperature_registerNumber, temperature_resourceFlag, pattern, speed, 8);
//            }
        }
    }

    @OnClick({R.id.backRelLay, R.id.popupRelLay, R.id.coldRel, R.id.hotRel,
            R.id.windyRel, R.id.highRel, R.id.lowRel, R.id.middleRel, R.id.aotuRel,
            R.id.energyRel, R.id.sleepRel, R.id.switchRel, R.id.close_backRelLay,
            R.id.close_popupRelLay, R.id.close_switchRel, R.id.off_backRelLay, R.id.sub_temp_Rel, R.id.add_temp_Rel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRelLay://点击返回
                Intent intent = new Intent();
                intent.putExtra("position", position + "");
                intent.putExtra("openOrClose", openOrClose + "");
                intent.putExtra("panelDisable",panelDisable+"");
                setResult(0, intent);
                finish();
                break;
            case R.id.popupRelLay://点击弹框
                backgroundAlpha(0.8f);
                popupWindow.showAsDropDown(titleBar, width - 8, -20);
                break;
            case R.id.coldRel://制冷
                if (cold == 1) {
                    if (!"制冷".equals(pattern)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(cold_deviceCode, cold_deviceStation,
                                    cold_permission, cold_registerAddress,
                                    cold_registerNumber, cold_resourceFlag, "制冷", speed, PATTARN);

                        }
                    } else {
                        toast("当前已处于制冷模式");
                    }


                } else {
                    toast("暂无此项功能");
                }
                break;
            case R.id.hotRel://制热
                if (hot == 1) {
                    if (!"制热".equals(pattern)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(hot_deviceCode, hot_deviceStation,
                                    hot_permission, hot_registerAddress,
                                    hot_registerNumber, hot_resourceFlag, "制热", speed, PATTARN);
                        }
                    } else {
                        toast("当前已处于制热模式");
                    }


                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.windyRel://通风
                if (wind == 1) {
                    if (!"通风".equals(pattern)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(wind_deviceCode, wind_deviceStation,
                                    wind_permission, wind_registerAddress,
                                    wind_registerNumber, wind_resourceFlag, "通风", speed, PATTARN);

                        }
                    } else {
                        toast("当前已处于通风模式");
                    }


                } else {
                    toast("暂无此项功能");
                }
                break;
            case R.id.highRel://高档
                if (high == 1) {
                    if (!"高档".equals(speed)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(high_deviceCode, high_deviceStation,
                                    high_permission, high_registerAddress,
                                    high_registerNumber, high_resourceFlag, pattern, "高档", SPEED);

                        }
                    } else {
                        toast("当前已处于高档");
                    }


                } else {
                    toast("暂无此项功能");
                }
                break;
            case R.id.lowRel://低档
                if (low == 1) {
                    if (!"低档".equals(speed)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(low_deviceCode, low_deviceStation,
                                    low_permission, low_registerAddress,
                                    low_registerNumber, low_resourceFlag, pattern, "低档", SPEED);

                        }
                    } else {
                        toast("当前已处于低档");
                    }
                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.middleRel://中档
                if (middle == 1) {
                    if (!"中档".equals(speed)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(middle_deviceCode, middle_deviceStation,
                                    midlle_permission, middle_registerAddress,
                                    middle_registerNumber, middle_resourceFlag, pattern, "中档", SPEED);

                        }
                    } else {
                        toast("当前已处于中档");
                    }

                } else {
                    toast("暂无此项功能");
                }
                break;
            case R.id.aotuRel://自动
                if (auto == 1) {
                    if (!"自动".equals(speed)) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(auto_deviceCode, auto_deviceStation,
                                    auto_permission, auto_registerAddress,
                                    auto_registerNumber, auto_resourceFlag, pattern, "自动", SPEED);

                        }
                    } else {
                        toast("当前已处于自动档");
                    }
                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.energyRel://节能
                if (thirft == 1) {
//                    toast("节能");
                    if (isClickable) {
                        isClickable = false;
                        Operate(thirft_deviceCode, thirft_deviceStation,
                                thirft_permission, thirft_registerAddress,
                                thirft_registerNumber, thirft_resourceFlag, pattern, speed, ENERGY);

                    }

                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.sleepRel://睡眠
                if (sleep == 1) {
                    if (isOn.equals("1")) {
                        if (isClickable) {
                            isClickable = false;
                            SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                            Operate(sleep_deviceCode, sleep_deviceStation,
                                    sleep_permission, sleep_registerAddress,
                                    sleep_registerNumber, sleep_resourceFlag, pattern, speed, SLEEP);
                        }
                    } else {
                        if (isClickable) {
                            isClickable = false;
                            Operate(open_deviceCode, open_deviceStation,
                                    open_permission, open_registerAddress,
                                    open_registerNumber, open_resourceFlag, pattern, speed, WAKE);
                        }
                    }


                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.switchRel://打开状态  点击执行关闭操作
                if (isClickable) {
                    isClickable = false;
                    SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                    Operate(close_deviceCode, close_deviceStation,
                            close_permission, close_registerAddress,
                            close_registerNumber, close_resourceFlag, pattern, speed, CLOSE);
                    Log.e("----关闭", openOrClose + "");

                }

                break;
            case R.id.close_backRelLay:
                Intent intent1 = new Intent();
                intent1.putExtra("position", position + "");
                intent1.putExtra("openOrClose", openOrClose + "");
                intent1.putExtra("panelDisable",panelDisable+"");
                setResult(0, intent1);
                finish();
                break;
            case R.id.close_popupRelLay:
                backgroundAlpha(0.8f);
                popupWindow.showAsDropDown(closeTitleBar, width - 8, -20);
                break;
            case R.id.close_switchRel:
                if (isClickable) {
                    isClickable = false;
                    SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
                    Operate(open_deviceCode, open_deviceStation,
                            open_permission, open_registerAddress,
                            open_registerNumber, open_resourceFlag, pattern, speed, OPEN);
                    Log.e("----打开", openOrClose + "");

                }

                break;
            case R.id.off_backRelLay:
                Intent intent2 = new Intent();
                setResult(1, intent2);
                finish();
                break;
            case R.id.sub_temp_Rel:
                if (temp == 1) {
                    if (isClickable) {
                        percent = percent - 1;
                        if ("制冷".equals(pattern)) {
                            if (percent < tMin) {
                                percent = percent + 1;
                                toast("已达到设定的最低温度");
                                return;
                            }
                        } else {
                            if (percent < 5) {
                                percent = percent + 1;
                                toast("低于国家限温，不可操作");
                                return;
                            }
                        }
                        percentTempTv.setText(percent + "");
                        myShowPercentView.setPercent((percent) * 2);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isClickable = false;
                                Operate(temperature_deviceCode, temperature_deviceStation,
                                        percent + "", temperature_registerAddress,
                                        temperature_registerNumber, temperature_resourceFlag, pattern, speed, SUBTEMP);
                            }
                        },500);


                    }

                } else {
                    toast("暂无此项功能");
                }

                break;
            case R.id.add_temp_Rel:
                if (temp == 1) {
                    if (isClickable) {
                        percent = percent + 1;
                        if ("制冷".equals(pattern)) {
                            if (percent > 40) {
                                percent = percent - 1;
                                toast("高于国家限温，不可操作");
                                return;
                            }
                        } else {
                            if (percent > tMax) {
                                percent = percent - 1;
                                toast("已达到设定的最高温度");
                                return;
                            }
                        }
                        percentTempTv.setText(percent + "");
                        myShowPercentView.setPercent((percent) * 2);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isClickable = false;
                                Operate(temperature_deviceCode, temperature_deviceStation,
                                        percent + "", temperature_registerAddress,
                                        temperature_registerNumber, temperature_resourceFlag, pattern, speed, ADDTEMP);
                            }
                        },500);

                    }

                } else {
                    toast("暂无此项功能");
                }

                break;

        }
    }

    private void Operate(String deviceCode, String deviceStation,
                         final String permission, final String registerAddress,
                         String registerNumber, final String resourceFlag,
                         final String patterns, final String speeds, final int type) {
        RequestParams requset = new RequestParams(HttpURL.Url(this) + HttpURL.OPERATIONDEVICE);
        requset.addHeader("access_token", AppUtils.getAccessToken(TempControlDetailActivity.this));
        requset.addQueryStringParameter("deviceId", controlId);
        if ("制冷".equals(patterns)) {
            if (Integer.valueOf(temperature) < tMin) {
                requset.addQueryStringParameter("resourceFlag", resourceFlag + "," + temperature_resourceFlag);
                requset.addQueryStringParameter("registerNumber", "2");
                requset.addQueryStringParameter("permission", permission + "," + tMin);
            } else {
                requset.addQueryStringParameter("resourceFlag", resourceFlag);
                requset.addQueryStringParameter("registerNumber", registerNumber);
                requset.addQueryStringParameter("permission", permission);
            }
        } else if ("制热".equals(patterns)) {
            if (Integer.valueOf(temperature) > tMax) {
                requset.addQueryStringParameter("resourceFlag", resourceFlag + "," + temperature_resourceFlag);
                requset.addQueryStringParameter("registerNumber", "2");
                requset.addQueryStringParameter("permission", permission + "," + tMax);
            } else {
                requset.addQueryStringParameter("resourceFlag", resourceFlag);
                requset.addQueryStringParameter("registerNumber", registerNumber);
                requset.addQueryStringParameter("permission", permission);
            }
        } else {
            requset.addQueryStringParameter("resourceFlag", resourceFlag);
            requset.addQueryStringParameter("registerNumber", registerNumber);
            requset.addQueryStringParameter("permission", permission);
        }

        requset.addQueryStringParameter("deviceStation", deviceStation);
        requset.addQueryStringParameter("deviceCode", deviceCode);
        requset.addQueryStringParameter("registerAddress", registerAddress);
//        requset.addQueryStringParameter("registerNumber", registerNumber);
//        requset.addQueryStringParameter("permission", permission);
        requset.addQueryStringParameter("deviceKind", ekName);
        requset.addQueryStringParameter("deviceFirm", deviceFirm);
        requset.setConnectTimeout(5000);
        requset.setReadTimeout(5000);
        x.http().request(HttpMethod.GET, requset, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("----反馈1", "==");
                try {
                    JSONObject object = new JSONObject(result);
                    String resultCode = object.optString("result");
                    Log.e("===操作结果",resultCode);
                    if ("1".equals(resultCode) || "操作成功".equals(resultCode)) {
                        if (type == PATTARN) {
                            modelTv.setText("模式  " + patterns);
                            pattern = patterns;
                            if ("制冷".equals(patterns)) {
                                percentTempIv.setImageResource(R.mipmap.tocold);
                                if (Integer.valueOf(temperature) < tMin) {
                                    percent = tMin;
                                    temperature = tMin + "";
                                    percentTempTv.setText(percent + "");
                                    myShowPercentView.setPercent((percent) * 2);
                                }
                            } else if ("制热".equals(patterns)) {
                                percentTempIv.setImageResource(R.mipmap.tohot);
                                if (Integer.valueOf(temperature) > tMax) {
                                    percent = tMax;
                                    temperature = tMax + "";
                                    percentTempTv.setText(percent + "");
                                    myShowPercentView.setPercent((percent) * 2);
                                }
                            }else {
                                percentTempIv.setImageResource(R.mipmap.towindy);
                            }
                        } else if (type == SPEED) {
                            gearTv.setText("档位  " + speeds);
                            speed = speeds;
                        } else if (type == CLOSE) {
                            openOrClose = false;
                            entity.setOpen(openOrClose);
                            status();
                        } else if (type == OPEN) {
                            openOrClose = true;
                            entity.setOpen(openOrClose);
                            isOn = "1";
                            status();
                        } else if (type == SLEEP) {
                            sleepTv.setText("唤醒");
                            isOn = "2";
                        } else if (type == WAKE) {
                            sleepTv.setText("睡眠");
                            isOn = "1";
                        } else if (type == DISABLE_OPEN) {
                            panelDisable = 0;
                            toast("面板已禁用");
                        } else if (type == DISABLE_CLOSE) {
                            panelDisable = 1;
                            toast("面板已启用");
                        } else if (type==SUBTEMP||type==ADDTEMP) {
                            temperature = percent+"";
                        }

                    } else {
                        if ("NOMAC".equals(resultCode)) {
                            toast("设备尚未连接");
                        } else {
                            toast(resultCode);
                        }
                        if (type == SUBTEMP&&type==ADDTEMP) {
                            percent = Integer.valueOf(temperature);
                            percentTempTv.setText(percent + "");
                            myShowPercentView.setPercent((percent) * 2);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TempControlDetailActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                SuccinctProgress.dismiss();
                isClickable = true;
            }
        });


    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    public void toast(String str) {
        Toast toast = Toast.makeText(TempControlDetailActivity.this, str, Toast.LENGTH_LONG);
        Utils.showMyToast(toast, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position + "");
        intent.putExtra("openOrClose", openOrClose + "");
        intent.putExtra("panelDisable",panelDisable+"");
        ;
        setResult(0, intent);
        finish();
        super.onBackPressed();
    }
}
