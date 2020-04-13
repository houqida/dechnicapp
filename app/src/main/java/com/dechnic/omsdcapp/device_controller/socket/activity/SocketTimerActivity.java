package com.dechnic.omsdcapp.device_controller.socket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.device_controller.socket.adapter.SocketTimerShowAdapter;
import com.dechnic.omsdcapp.entity.DevicesDetailsTime;
import com.dechnic.omsdcapp.entity.TimerEntity;
import com.dechnic.omsdcapp.url.HttpURL;
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

/**
 * Created by Administrator on 2017/5/4.
 */

public class SocketTimerActivity extends BaseActivity {
    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.addTimerRel)
    RelativeLayout addTimerRel;
    @Bind(R.id.timer_lv)
    ListView timerLv;

    private List<TimerEntity> entityList;
    private SocketTimerShowAdapter adapter;

    private TimerEntity entity;

    String controlId = "";
    Bundle bundle;
    private String hour;
    private String minute;
    private StringBuffer sb;

    private List<DevicesDetailsTime> detailsTimeList;

    private static final int TO_ADDTIME = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_timer);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        controlId = bundle.getString("controlId");
        detailsTimeList = (List<DevicesDetailsTime>) bundle.getSerializable("list");
        Log.e("==传来的list",detailsTimeList.size()+"");
        initViews();
    }

    private void initViews() {
        entityList = new ArrayList<>();
//        getData();
        getTimeList();
        adapter = new SocketTimerShowAdapter(this,entityList);
        timerLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setListener(new SocketTimerShowAdapter.onSwitchListener() {
            @Override
            public void onSwitchChanged(boolean status, String id,int devideOrder) {
                for (int i = 0; i < entityList.size(); i++) {
                    if (id.equals(entityList.get(i).getId())) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("status",status);
                        bundle.putInt("i",i);
                        bundle.putInt("devideOrder",devideOrder);
                        msg.obj = bundle;
                        handler.sendMessage(msg);
                    }
                }
            }
        });

        timerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SocketTimerActivity.this, AddSocketTimerActivity.class);
                bundle.putInt("position",position);
                bundle.putSerializable("timeList", (Serializable) entityList);
                bundle.putInt("size",entityList.size());
                bundle.putString("from","lv");
                intent.putExtras(bundle);
                startActivityForResult(intent,TO_ADDTIME);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_ADDTIME && resultCode == 0) {
            initViews();
        }
    }

    private void getTimeList(){
        SuccinctProgress.showSuccinctProgress(this,"正在加载...",SuccinctProgress.THEME_ARC,true,true);
        if (NetWorkUtils.getNetState(this)== Constants.NO_NETWORK) {
            Toast.makeText(SocketTimerActivity.this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
            return;
        }
        RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.TIME_LIST);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("deviceId",controlId);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                json(result);
                SuccinctProgress.dismiss();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(SocketTimerActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void json(String result) {
        try {
            JSONArray array = new JSONArray(result);
            Log.e("===array",array.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                entity = new TimerEntity();
                String deviceType = object.optString("deviceType");
                String id = object.optString("id");
                String deviceWeek = object.optString("deviceWeek");
                String deviceTime = object.optString("deviceTime");
                String deviceId = object.optString("deviceId");
                int order = object.optInt("deviceOrder");
                Log.e("==顺序",order+"");
                String deviceStatus = object.optString("deviceStatus");

                Log.e("==devicestatus",deviceStatus);
                Log.e("==deviceType",deviceType);
                Log.d("===deviceweek",deviceWeek);

                if ("1".equals(deviceStatus)) {//  开启
                    entity.setOpen(true);
                }else {// 不开启
                    entity.setOpen(false);
                }

//                if ("1".equals(deviceType)) {//1 开机
//                    entity.setTimer(true);
//                }else {// 关机
//                    entity.setTimer(false);
//                }

                entity.setTimer(deviceType);
                entity.setDeviceType(deviceType);
                entity.setCreatedOn(object.optLong("createdOn")+"");
                entity.setTemperature(object.optString("temperature"));

                Log.e("===time==temperature",object.optString("temperature"));
                entity.setOrder(order);
                entity.setUpdatedOn(object.optLong("updatedOn")+"");
                entity.setId(id);
                entity.setDeviceId(deviceId);
                entity.setDeviceStatus(deviceStatus);
                entity.setOrg_deviceWeek(deviceWeek);
                entity.setOrg_deviceTime(deviceTime);


                hour = deviceTime.substring(0,2);
                minute = deviceTime.substring(2,4);

                entity.setDeviceTime(hour +":"+ minute);

                entity.setHour(hour);
                entity.setMinute(minute);

                String week="";
                String wes ="";
                List<String> weekList = new ArrayList<>();
                entity.setDeviceWeek("no");
//                if ("0".equals(deviceWeek)) {
//                    entity.setDeviceWeek("仅一次");
//                }else {
//                    if (deviceWeek.length()==7) {
//                        entity.setDeviceWeek("每天");
//                    }else {
//                        for (int j = 0; j < deviceWeek.length(); j++) {
//                            wes = deviceWeek.substring(j,j+1);
//                            if ("1".equals(wes)) {
//                                week = "周一";
//                            } else if ("2".equals(wes)) {
//                                week = "周二";
//                            }else if ("3".equals(wes)) {
//                                week = "周三";
//                            } else if ("4".equals(wes)) {
//                                week = "周四";
//                            }else if ("5".equals(wes)) {
//                                week = "周五";
//                            }else if ("6".equals(wes)) {
//                                week = "周六";
//                            }else if ("7".equals(wes)) {
//                                week = "周天";
//                            }
//                            weekList.add(week);
//                        }
//
//                        sb = new StringBuffer();
//                        for(String s:weekList){
//                            sb.append(s+"  ");
//                        }
//                        entity.setDeviceWeek(sb.toString());
//
//                    }
//
//                }

                entityList.add(entity);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.backRel, R.id.addTimerRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRel:
                finish();
                break;
            case R.id.addTimerRel:
                Intent intent = new Intent(this, AddSocketTimerActivity.class);
                bundle.putString("from","add");
                bundle.putInt("size",entityList.size());
                intent.putExtras(bundle);
                startActivityForResult(intent,TO_ADDTIME);
                break;
        }
    }
    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle bundle = (Bundle) msg.obj;
                    Boolean deviceStatus = bundle.getBoolean("status");
                    int i = bundle.getInt("i");
                    int devideOrder = bundle.getInt("devideOrder");
                    Operate(deviceStatus,i,devideOrder);
                    break;
                case 2:
                    Bundle bundle2 = (Bundle) msg.obj;
                    Boolean deviceStatus2 = bundle2.getBoolean("status");
                    int i2 = bundle2.getInt("i");
                    entityList.get(i2).setOpen(deviceStatus2);
                    if (deviceStatus2) {
                        entityList.get(i2).setDeviceStatus("1");
                    }else {
                        entityList.get(i2).setDeviceStatus("0");
                    }
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    private void Operate(final Boolean deviceStatus, final int i, int devideOrder){
        SuccinctProgress.showSuccinctProgress(this,"请稍等...",SuccinctProgress.THEME_ARC,true,true);
        RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.SET_TIME);

        params.addHeader("access_token",AppUtils.getAccessToken(this));
        params.addQueryStringParameter("deviceId",controlId);
        params.addQueryStringParameter("resourceFlag",detailsTimeList.get(devideOrder-1).getResourceFlag());
        params.addQueryStringParameter("deviceStation",detailsTimeList.get(devideOrder-1).getDeviceStation());
        params.addQueryStringParameter("deviceCode",detailsTimeList.get(devideOrder-1).getDeviceCode());
        params.addQueryStringParameter("registerAddress",detailsTimeList.get(devideOrder-1).getRegisterAddress());
        params.addQueryStringParameter("registerNumber",detailsTimeList.get(devideOrder-1).getRegisterNumber());
        params.addQueryStringParameter("permission",detailsTimeList.get(devideOrder-1).getPermission());
        params.addQueryStringParameter("deviceKind",detailsTimeList.get(devideOrder-1).getEkName());
        params.addQueryStringParameter("deviceFirm",detailsTimeList.get(devideOrder-1).getDeviceFirm());
        params.addQueryStringParameter("deviceTime",entityList.get(i).getOrg_deviceTime());
        params.addQueryStringParameter("deviceWeek",entityList.get(i).getDeviceWeek());
        params.addQueryStringParameter("deviceType",entityList.get(i).getDeviceType());
        params.addQueryStringParameter("temperature",detailsTimeList.get(devideOrder-1).getTemperature().equals("null")?"0":detailsTimeList.get(devideOrder-1).getTemperature());


        Log.e("==access_token",AppUtils.getAccessToken(this));
        Log.e("==temperature",detailsTimeList.get(devideOrder-1).getTemperature().equals("null")?"0":detailsTimeList.get(devideOrder-1).getTemperature());
        Log.e("==controlId",controlId);
        Log.e("==resourceFlag",detailsTimeList.get(devideOrder-1).getResourceFlag());
        Log.e("==deviceStation",detailsTimeList.get(devideOrder-1).getDeviceStation());
        Log.e("==deviceCode",detailsTimeList.get(devideOrder-1).getDeviceCode());
        Log.e("==registerAddress",detailsTimeList.get(devideOrder-1).getRegisterAddress());
        Log.e("==registerNumber",detailsTimeList.get(devideOrder-1).getRegisterNumber());
        Log.e("==permission",detailsTimeList.get(devideOrder-1).getPermission());
        Log.e("==deviceKind",detailsTimeList.get(devideOrder-1).getEkName()); ;
        Log.e("==deviceFirm",detailsTimeList.get(devideOrder-1).getDeviceFirm());
        Log.e("==deviceTime",entityList.get(i).getOrg_deviceTime());
        Log.e("==deviceType",entityList.get(i).getDeviceType());
//        Log.e("==temperature",detailsTimeList.get(devideOrder-1).getTemperature());

        params.addQueryStringParameter("order",devideOrder+"");
        params.addQueryStringParameter("addFlag","old");
        params.addQueryStringParameter("timeId",entityList.get(i).getId());


        if (deviceStatus) {
            params.addQueryStringParameter("deviceStatus","1");
        }else {
            params.addQueryStringParameter("deviceStatus","0");
        }

        Log.e("==order",devideOrder+"");
        Log.e("==addFlag","old");
        Log.e("==timeId",entityList.get(i).getId());
        Log.e("==deviceStatus","0");

        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)||"操作成功".equals(code)) {
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("status",deviceStatus);
                        bundle.putInt("i",i);
                        msg.obj = bundle;
                        handler.sendMessage(msg);
                    }else {
                        Toast.makeText(SocketTimerActivity.this,code,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SuccinctProgress.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(SocketTimerActivity.this,R.string.http_error,Toast.LENGTH_SHORT).show();
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
