package com.dechnic.omsdcapp.device_controller.temperature.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
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

public class InstallTempActivity extends BaseActivity {
    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.saveTimerRel)
    RelativeLayout saveTimerRel;
    @Bind(R.id.bar)
    RelativeLayout bar;
    @Bind(R.id.highTempTv)
    TextView highTempTv;
    @Bind(R.id.highRel)
    RelativeLayout highRel;
    @Bind(R.id.lowTempTv)
    TextView lowTempTv;
    @Bind(R.id.lowRel)
    RelativeLayout lowRel;
    @Bind(R.id.activity_install_temp)
    RelativeLayout activityInstallTemp;

    private View view;
    private RelativeLayout cacleRel;
    private RelativeLayout sureRel;
    private PopupWindow popupWindow;
    private LoopView loopView;
    List<String> list = new ArrayList<>();
    private String temp;
    private String tempHigh;
    private String tempLow;
    String tag;
    int[] location = new int[2];

    int tMax, tMin;
    Bundle bundle;

    private int low;
    private int high;

    private String t_limit_registerAddress;
    private String t_limit_deviceStation;
    private String t_limit_deviceCode;
    private String t_limit_registerNumber;
    private String t_limit_permission;
    private String deviceKind;
    private String deviceFirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_temp);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        tMax = bundle.getInt("tMax");
        tMin = bundle.getInt("tMin");
        t_limit_registerAddress = bundle.getString("registerAddress");
        t_limit_deviceStation = bundle.getString("deviceStation");
        t_limit_deviceCode = bundle.getString("deviceCode");
        t_limit_registerNumber = bundle.getString("registerNumber");
        deviceFirm = bundle.getString("deviceFirm");
        deviceKind = bundle.getString("deviceKind");
        highTempTv.setText(tMax + "℃");
        lowTempTv.setText(tMin + "℃");
        low = tMin;
        high = tMax;
        initPopupWindpws();
    }

    private void initPopupWindpws() {
        view = LayoutInflater.from(this).inflate(R.layout.install_temp_popup, null);
        cacleRel = (RelativeLayout) view.findViewById(R.id.cacleRel);
        sureRel = (RelativeLayout) view.findViewById(R.id.sureRel);
        loopView = (LoopView) view.findViewById(R.id.loopView);
        for (int i = 5; i < 41; i++) {
            list.add(i + "");
        }
        loopView.setItems(list);
        tempHigh = loopView.getSelectedItem() + "℃";
        tempLow = loopView.getSelectedItem() + "℃";
//        loopView.setInitPosition(0);//

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.ins_popup_anim);

        loopView.setListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                if ("1".equals(tag)) {
                    tempHigh = index + 5 + "℃";
                    if (tMax == index + 5) {
                        high = tMax;
                    } else {
                        high = index + 5;
                    }
                } else {
                    tempLow = index + 5 + "℃";
                    if (tMin == index + 5) {
                        low = tMin;
                    } else {
                        low = index + 5;
                    }

                }
            }
        });

        cacleRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                high = tMax;
                low = tMin;
            }
        });

        sureRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if ("1".equals(tag)) {
                    highTempTv.setText(tempHigh);
                } else {
                    lowTempTv.setText(tempLow);
                }
            }
        });


    }

    @OnClick({R.id.backRel, R.id.saveTimerRel, R.id.highRel, R.id.lowRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                Intent intent0 = new Intent();
                setResult(1, intent0);
                finish();
                break;
            case R.id.saveTimerRel:
                Log.e("-----high", high + "");
                Log.e("-----low", low + "");
//                if (high < low) {
//                    Toast.makeText(this, "最高温不能低于最低温", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//
//                }
                setLimit();
                break;
            case R.id.highRel:
                tag = "1";
                view.getLocationOnScreen(location);
                loopView.setInitPosition(tMax-5);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
            case R.id.lowRel:
                tag = "0";
                view.getLocationOnScreen(location);
                loopView.setInitPosition(tMin-5);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, -location[1]);
                break;
        }
    }

    private void setLimit() {
        SuccinctProgress.showSuccinctProgress(this, "修改限温...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.TEMP_SETLIMIT);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("deviceId", bundle.getString("controlId"));
        params.addQueryStringParameter("tMin", low + "");
        params.addQueryStringParameter("tMax", high + "");
        ;
        params.addQueryStringParameter("deviceKind", deviceKind);
        params.addQueryStringParameter("deviceFirm", deviceFirm);
        params.addQueryStringParameter("deviceStation", t_limit_deviceStation);
        params.addQueryStringParameter("deviceCode", t_limit_deviceCode);
        params.addQueryStringParameter("registerAddress", t_limit_registerAddress);

        String highh;
        String loww;
        if (high < 10) {
            highh = "0" + high;
        } else {
            highh = "" + high;
        }
        if (low < 10) {
            loww = "0" + low;
        } else {
            loww = "" + low;
        }
        if ("春泉".equals(deviceFirm)) {
            params.addQueryStringParameter("registerNumber", t_limit_registerNumber);
            params.addQueryStringParameter("permission", highh + "," + loww);
            Log.e("===permission", highh + "" + loww);
        }else {
            params.addQueryStringParameter("registerNumber", "4");
            params.addQueryStringParameter("permission", loww + ",40,5," + highh);
        }

        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SuccinctProgress.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String alt = object.optString("result");
                    Log.e("====altt", alt);
                    if ("1".equals(alt) || "操作成功".equals(alt)) {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(InstallTempActivity.this, alt, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(InstallTempActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent();
                    intent.putExtra("limit_high", high + "");
                    intent.putExtra("limit_low", low + "");
                    setResult(0, intent);
                    finish();
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent0 = new Intent();
        setResult(1, intent0);
        finish();
        super.onBackPressed();
    }
}
