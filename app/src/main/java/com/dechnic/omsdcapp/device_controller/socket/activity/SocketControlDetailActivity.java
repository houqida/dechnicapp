package com.dechnic.omsdcapp.device_controller.socket.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.Base2Activity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.entity.DevicesDetails;
import com.dechnic.omsdcapp.entity.DevicesDetailsTime;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.AlertDialog;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
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

public class SocketControlDetailActivity extends Base2Activity {
    @Bind(R.id.back_Rel)
    RelativeLayout backRel;
    @Bind(R.id.socket_tv)
    TextView socketTv;
    @Bind(R.id.status_tv)
    TextView statusTv;
    @Bind(R.id.control_icon_toclose)
    ImageView controlIcontoclose;

    @Bind(R.id.control_icon_toopen)
    ImageView controlIcontoopen;

    @Bind(R.id.control_Rel)
    RelativeLayout controlRel;
    @Bind(R.id.activity_socket_control_detail)
    LinearLayout activitySocketControlDetail;
    @Bind(R.id.headIconRel)
    RelativeLayout headIconRel;
    @Bind(R.id.socket_dingshi_newIv)
    ImageView socketDingshiNewIv;
    @Bind(R.id.socket_dingshi_newTv)
    TextView socketDingshiNewTv;
    @Bind(R.id.socket_dingshi_newRel)
    RelativeLayout socketDingshiNewRel;
    @Bind(R.id.socket_jinyong_newIv)
    ImageView socketJinyongNewIv;
    @Bind(R.id.socket_jinyong_newTv)
    TextView socketJinyongNewTv;
    @Bind(R.id.socket_jinyong_newRel)
    RelativeLayout socketJinyongNewRel;
    @Bind(R.id.socket_qiyong_newIv)
    ImageView socketQiyongNewIv;
    @Bind(R.id.socket_qiyong_newTv)
    TextView socketQiyongNewTv;
    @Bind(R.id.socket_qiyong_newRel)
    RelativeLayout socketQiyongNewRel;
    @Bind(R.id.headIconIv)
    ImageView headIconIv;


    boolean isOpen = true;
    boolean isOnLine = true;
    Bundle bundle;
    private String controlId;
    private String ekName;
    private String deviceFirm;

    private List<DevicesDetails> list;
    private List<DevicesDetailsTime>  detailsTimeList;
    private int time;
    private int time_position;
    private int open = 1;
    private int open_position;
    private int close = 1;
    private int close_position;
    private int dis_open;
    private int dis_open_position;
    private int dis_close;
    private int dis_close_position;

    private String position;
    private int panelDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_control_detail);
        ButterKnife.bind(this);
        setTranslucent(this);
        bundle = getIntent().getExtras();
        isOnLine = bundle.getBoolean("isOnline");
        isOpen = bundle.getBoolean("isOpen");
        controlId = bundle.getString("controlId");
        String devicename = bundle.getString("devicename");
        socketTv.setText(devicename);
        ekName = bundle.getString("ekName");
        deviceFirm = bundle.getString("deviceFirm");
        position = bundle.getString("position");
        panelDisable = bundle.getInt("panelDisable");
        getDetails();
        status();
    }

    private void getDetails(){
        SuccinctProgress.showSuccinctProgress(this,"正在加载...",SuccinctProgress.THEME_ARC,true,true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        }else {
            RequestParams details = new RequestParams(HttpURL.Url(this)+HttpURL.DEVICEDETAILS);
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
            detailsTimeList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                DevicesDetails devicesDetails = new DevicesDetails();
                devicesDetails.setRegisterAddress(object.optString("registerAddress"));
                devicesDetails.setIsOn(object.optString("isOn"));
                devicesDetails.setResourceName(object.optString("resourceName"));
                devicesDetails.setResourceFlag(object.optString("resourceFlag"));
                String resourceFlagtime = object.optString("resourceFlag");
                Log.e("==flag", object.optString("resourceFlag"));
                devicesDetails.setPermission(object.optString("permission"));
                Log.e("====permission",object.optString("permission"));
                devicesDetails.setRegisterNumber(object.optString("registerNumber"));
                devicesDetails.setDeviceCode(object.optString("deviceCode"));
                devicesDetails.setEkName(object.optString("deviceKind"));
                devicesDetails.setDeviceFirm(object.optString("deviceFirm"));
                devicesDetails.setDeviceStation(object.optString("deviceStation"));
                panelDisable = object.optInt("panelDisable");
                if ("春泉".equals(object.optString("deviceFirm"))) {
                    if ("time1".equals(resourceFlagtime)||"time2".equals(resourceFlagtime)||"time3".equals(resourceFlagtime)
                            ||"time4".equals(resourceFlagtime)||"time5".equals(resourceFlagtime)||"time6".equals(resourceFlagtime)
                            ||"time7".equals(resourceFlagtime)||"time8".equals(resourceFlagtime)||"time9".equals(resourceFlagtime)
                            ||"time10".equals(resourceFlagtime)||"time11".equals(resourceFlagtime)||"time12".equals(resourceFlagtime)
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
                }else {
                    if ("time1".equals(resourceFlagtime)||"time2".equals(resourceFlagtime)||"time3".equals(resourceFlagtime)
                            ||"time4".equals(resourceFlagtime)||"time5".equals(resourceFlagtime)||"time6".equals(resourceFlagtime)) {
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
                list.add(devicesDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails() {
        for (int i = 0; i < list.size(); i++) {
            if (Constants.TIME.equals(list.get(i).getResourceFlag())) {
                time = 1;
            } else if (Constants.STATE_OPEN.equals(list.get(i).getResourceFlag())) {
                open = 1;
                open_position = i;
                Log.e("==open_position",open_position+"");
            } else if (Constants.STATE_CLOSE.equals(list.get(i).getResourceFlag())) {
                close = 1;
                close_position = i;
                Log.e("==close_position",close_position+"");
            } else if (Constants.DISABLE_OPEN.equals(list.get(i).getResourceFlag())) {
                dis_open = 1;
                dis_open_position = i;
                Log.e("==dis_open_position",dis_open_position+"");
            } else if (Constants.DISABLE_CLOSE.equals(list.get(i).getResourceFlag())) {
                dis_close = 1;
                dis_close_position = i;
                Log.e("==dis_close_position",dis_close_position+"");
            }

        }

    }

    private void status() {
        if (isOnLine) {
            if (isOpen) {
                activitySocketControlDetail.setBackgroundResource(R.color.socket_online_open);
                headIconIv.setImageResource(R.mipmap.socket_detail_open);
                statusTv.setText("插座已开启");
                socketDingshiNewIv.setImageResource(R.mipmap.socket_dingshi_open_new);
                socketDingshiNewTv.setTextColor(Color.parseColor("#ffffff"));
                socketJinyongNewIv.setImageResource(R.mipmap.socket_jinyong_open_new);
                socketJinyongNewTv.setTextColor(Color.parseColor("#ffffff"));
                socketQiyongNewIv.setImageResource(R.mipmap.socket_qiyong_open_new);
                socketQiyongNewTv.setTextColor(Color.parseColor("#ffffff"));
                statusTv.setTextColor(Color.parseColor("#ffffff"));
                controlIcontoclose.setImageResource(R.mipmap.socket_control_online_icon);
                controlIcontoopen.setVisibility(View.GONE);
                controlIcontoclose.setVisibility(View.VISIBLE);

            } else {
                activitySocketControlDetail.setBackgroundResource(R.color.socket_online_close);
                headIconIv.setImageResource(R.mipmap.socket_detail_closeoroffline);
                statusTv.setText("插座已关闭");
                socketDingshiNewIv.setImageResource(R.mipmap.socket_dingshi_close_new);
                socketJinyongNewIv.setImageResource(R.mipmap.socket_jinyong_close_new);
                socketQiyongNewIv.setImageResource(R.mipmap.socket_qiyong_close_new);
                socketDingshiNewTv.setTextColor(Color.parseColor("#999d9f"));
                socketJinyongNewTv.setTextColor(Color.parseColor("#999d9f"));
                socketQiyongNewTv.setTextColor(Color.parseColor("#999d9f"));
                statusTv.setTextColor(Color.parseColor("#999d9f"));
                controlIcontoopen.setImageResource(R.mipmap.socket_control_online_icon);
                controlIcontoopen.setVisibility(View.VISIBLE);
                controlIcontoclose.setVisibility(View.GONE);
            }

        } else {
            activitySocketControlDetail.setBackgroundResource(R.color.socket_offline);
            headIconIv.setImageResource(R.mipmap.socket_detail_closeoroffline);
            controlIcontoopen.setImageResource(R.mipmap.switch_control_icon_offline);
            controlIcontoclose.setImageResource(R.mipmap.switch_control_icon_offline);
            socketDingshiNewIv.setImageResource(R.mipmap.socket_dingshi_offline_new);
            socketJinyongNewIv.setImageResource(R.mipmap.socket_jinyong_offline_new);
            socketQiyongNewIv.setImageResource(R.mipmap.socket_qiyong_offline_new);
            socketDingshiNewTv.setTextColor(Color.parseColor("#c2c2c2"));
            socketJinyongNewTv.setTextColor(Color.parseColor("#c2c2c2"));
            socketQiyongNewTv.setTextColor(Color.parseColor("#c2c2c2"));
            if (isOpen) {
                statusTv.setText("插座已开启");
                controlIcontoopen.setVisibility(View.GONE);
                controlIcontoclose.setVisibility(View.VISIBLE);

            }else {
                statusTv.setText("开关已关闭");
                controlIcontoopen.setVisibility(View.VISIBLE);
                controlIcontoclose.setVisibility(View.GONE);
            }
            statusTv.setTextColor(Color.parseColor("#c2c2c2"));
            controlIcontoclose.setClickable(false);
            controlIcontoopen.setClickable(false);
            socketDingshiNewRel.setClickable(false);
            socketJinyongNewRel.setClickable(false);
            socketQiyongNewRel.setClickable(false);
        }
    }

    @OnClick({R.id.back_Rel, R.id.control_icon_toopen,R.id.control_icon_toclose,
            R.id.socket_dingshi_newRel, R.id.socket_jinyong_newRel, R.id.socket_qiyong_newRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Rel:
                Intent intent0 = new Intent();
                intent0.putExtra("position",position);
                intent0.putExtra("isOpen",isOpen+"");
                intent0.putExtra("panelDisable",panelDisable+"");
                setResult(0,intent0);
                finish();
                break;
            case R.id.control_icon_toclose:
                if (close==1) {
                    new AlertDialog(SocketControlDetailActivity.this).builder().setTitle("")
                            .setMsg("确认关闭吗？")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  Operate(close_position,0);
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();

                }else {
                    Toast.makeText(SocketControlDetailActivity.this,"暂无此项功能",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.control_icon_toopen:
                if (open==1) {
                    Operate(open_position,1);
                }else {
                    Toast.makeText(SocketControlDetailActivity.this,"暂无此项功能",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.socket_dingshi_newRel:
                if (isOnLine) {
                    if (time==1) {
                        Intent intent = new Intent(this, SocketTimerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("controlId",controlId);
                        bundle.putSerializable("list", (Serializable) detailsTimeList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SocketControlDetailActivity.this,"暂无此项功能",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    return;
                }
                break;
            case R.id.socket_jinyong_newRel://打开禁用面板，即不能使用面板控制
                if (isOnLine) {
                    if (dis_open==1) {
                        Operate(dis_open_position,3);
                    }else {
                        Toast.makeText(SocketControlDetailActivity.this,"暂无此项功能",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.socket_qiyong_newRel://关闭禁用面板，即可以使用面板控制
                if (isOnLine) {
                    if (dis_close==1) {
                        Operate(dis_close_position,4);
                    }else {
                        Toast.makeText(SocketControlDetailActivity.this,"暂无此项功能",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    private void Operate(int position, final int type) {
        SuccinctProgress.showSuccinctProgress(this,"请稍等...",SuccinctProgress.THEME_ARC,true,true);
        RequestParams requset = new RequestParams(HttpURL.Url(this)+HttpURL.OPERATIONDEVICE);
        requset.addHeader("access_token",AppUtils.getAccessToken(this));
        requset.addQueryStringParameter("deviceId",controlId);
        requset.addQueryStringParameter("resourceFlag",list.get(position).getResourceFlag());
        requset.addQueryStringParameter("deviceStation",list.get(position).getDeviceStation());
        requset.addQueryStringParameter("deviceCode",list.get(position).getDeviceCode());
        requset.addQueryStringParameter("registerAddress",list.get(position).getRegisterAddress());
        requset.addQueryStringParameter("registerNumber",list.get(position).getRegisterNumber());
        requset.addQueryStringParameter("permission",list.get(position).getPermission());
        requset.addQueryStringParameter("deviceKind",list.get(position).getEkName());
        requset.addQueryStringParameter("deviceFirm",list.get(position).getDeviceFirm());

        Log.e("==deviceId", controlId);
        Log.e("===resourceFlag",list.get(position).getResourceFlag());
        Log.e("===deviceStation",list.get(position).getDeviceStation());
        Log.e("===deviceCode",list.get(position).getDeviceCode());
        Log.e("===registerAddress",list.get(position).getRegisterAddress());
        Log.e("===registerNumber",list.get(position).getRegisterNumber());
        Log.e("===permission",list.get(position).getPermission());
        Log.e("===deviceKind",list.get(position).getEkName());
        Log.e("===deviceFirm",list.get(position).getDeviceFirm());


        x.http().request(HttpMethod.GET, requset, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)||"操作成功".equals(code)) {
                        if (type==0) {
                            isOpen = false;
                            status();
                        }else if (type==1){
                            isOpen = true;
                            status();
                        } else if (type==3) {
                            panelDisable = 0;
                            Toast.makeText(SocketControlDetailActivity.this,"面板已禁用",Toast.LENGTH_SHORT).show();
                        }else if (type==4){
                            panelDisable = 1;
                            Toast.makeText(SocketControlDetailActivity.this,"面板已启用",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SuccinctProgress.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();

                    Log.e("===responseCode",responseCode+"");
                    Log.e("===responseMsg",responseMsg+"");
                    Log.e("===errorResult",errorResult+"");
                    // ...
                } else { // 其他错误
                    // ...
                    Toast.makeText(SocketControlDetailActivity.this,R.string.http_error,Toast.LENGTH_SHORT).show();

                }
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


    @Override
    public void onBackPressed() {
        Intent intent0 = new Intent();
        intent0.putExtra("position",position);
        intent0.putExtra("isOpen",isOpen+"");
        intent0.putExtra("panelDisable",panelDisable+"");
        setResult(0,intent0);
        finish();
        super.onBackPressed();
    }
}
