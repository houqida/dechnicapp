package com.dechnic.omsdcapp.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.entity.FunctionEntity;
import com.dechnic.omsdcapp.entity.SceneDeviceEntity;
import com.dechnic.omsdcapp.scene.adapter.SceneDetailsDeviceListAdapter;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.ListViewforScrollView;
import com.dechnic.omsdcapp.widget.SuccinctProgress;
import com.google.gson.Gson;

import org.json.JSONArray;
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

public class AddSceneActivity extends BaseActivity {
    @Bind(R.id.back_Rel)
    RelativeLayout backRel;
    @Bind(R.id.saveRel)
    RelativeLayout saveRel;
    @Bind(R.id.scene_name_et)
    EditText sceneNameEt;
    @Bind(R.id.goIv)
    ImageView goIv;
    @Bind(R.id.job_iv)
    ImageView jobIv;
    @Bind(R.id.job_rel)
    RelativeLayout jobRel;
    @Bind(R.id.rest_iv)
    ImageView restIv;
    @Bind(R.id.rest_rel)
    RelativeLayout restRel;
    @Bind(R.id.out_iv)
    ImageView outIv;
    @Bind(R.id.out_rel)
    RelativeLayout outRel;
    @Bind(R.id.long_iv)
    ImageView longIv;
    @Bind(R.id.long_rel)
    RelativeLayout longRel;
    @Bind(R.id.week_iv)
    ImageView weekIv;
    @Bind(R.id.week_rel)
    RelativeLayout weekRel;
    @Bind(R.id.green_iv)
    ImageView greenIv;
    @Bind(R.id.green_rel)
    RelativeLayout greenRel;
    @Bind(R.id.scene_lin)
    LinearLayout sceneLin;
    @Bind(R.id.add_device_rel1)
    RelativeLayout addDeviceRel1;
    @Bind(R.id.add_device_rel2)
    RelativeLayout addDeviceRel2;

    int tag = 0;


    private ListViewforScrollView device_lv;
    private List<SceneDeviceEntity> entityList;
    private SceneDeviceEntity entity;
    private SceneDetailsDeviceListAdapter adapter;
    private List<FunctionEntity> functionEntityList;


    private String userid, sceneid, sceneName, sceneIv;
    private Bundle bundle;
    private List<String> idList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        userid = bundle.getString("userid");
        sceneid = bundle.getString("sceneId");
        sceneName = bundle.getString("sceneName");
        sceneIv = bundle.getString("sceneIv");
        if (!sceneName.equals("添加场景")) {
            sceneNameEt.setText(sceneName);
            tag = Integer.valueOf(sceneIv);
            myHandler.sendEmptyMessage(tag);
        }
        initListView();
    }

    private void initListView() {
        device_lv = (ListViewforScrollView) findViewById(R.id.device_lv);
        entityList = new ArrayList<>();
        functionEntityList = new ArrayList<>();
        idList = new ArrayList<>();
        getSceneDetails();
        adapter = new SceneDetailsDeviceListAdapter(this, entityList);
        device_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setListener(new SceneDetailsDeviceListAdapter.onSwitchListener() {
            @Override
            public void onSwitchChanged(String id, String on) {
                String ps = "";
                for (int i = 0; i < entityList.size(); i++) {
                    if (entityList.get(i).getDeviceId().equals(id)) {
                        entityList.get(i).setIsOn(on);
                        String p = entityList.get(i).getPermission();
                        if (on.equals("0")) {
                            for (int j = 0; j < functionEntityList.size(); j++) {
                                if (functionEntityList.get(j).getResourceFlag().equals(Constants.STATE_CLOSE)) {
                                    ps = functionEntityList.get(j).getPermission();
                                }

                            }
                        }else {
                            for (int j = 0; j < functionEntityList.size(); j++) {
                                if (functionEntityList.get(j).getResourceFlag().equals(Constants.STATE_OPEN)) {
                                    ps = functionEntityList.get(j).getPermission();
                                }

                            }
                        }

                        p = p.substring(0,p.length()-1) + ps;
                        Log.e("===保存的ppp",p);

                        entityList.get(i).setPermission(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        device_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("===设备点击","设备点击");
                if (entityList.get(position).getDeviceKind().equals("温控器")) {
                    Intent intent = new Intent(AddSceneActivity.this,SceneSetTempActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("pattern",entityList.get(position).getPattern());
                    bundle.putString("speed",entityList.get(position).getSpeed());
                    bundle.putString("temperature",entityList.get(position).getTemperature());
                    bundle.putString("deviceId",entityList.get(position).getDeviceId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,1);

                } else {
                    return;
                }
            }
        });


    }

    private void getSceneDetails() {
        SuccinctProgress.showSuccinctProgress(this, "加载中...", SuccinctProgress.THEME_ARC, true, true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        } else {
            RequestParams params = new RequestParams(HttpURL.Url(AddSceneActivity.this) + HttpURL.SCENEDEVICELIST);
            params.addHeader("access_token", AppUtils.getAccessToken(this));
            params.addQueryStringParameter("sceneId", sceneid);
            Log.e("=====sceneId", sceneid);
            Log.e("====access_token",AppUtils.getAccessToken(this));
            Log.e("====url",HttpURL.Url(AddSceneActivity.this) + HttpURL.SCENEDEVICELIST);
            params.setConnectTimeout(5000);
            params.setReadTimeout(5000);
            x.http().request(HttpMethod.GET,params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("=====result",result.toString());
                    pullJson(result);
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Log.e("===错误码",ex.getMessage());
                    Toast.makeText(AddSceneActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.optString("result");
            if ("1".equals(code)) {
                JSONArray jsonArray = jsonObject.optJSONArray("deviceList");
                Log.d("===scenejsonarray", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    entity = new SceneDeviceEntity();
                    entity.setDeviceKind(object.optString("deviceKind"));
                    String kind = object.optString("deviceKind");
                    entity.setDeviceCode(object.optString("deviceCode"));
                    entity.setDeviceFirm(object.optString("deviceFirm"));
                    entity.setDeviceId(object.optString("deviceId"));
                    entity.setAceneId(object.optString("sceneId"));
                    entity.setDeviceStation(object.optString("deviceStation"));
                    entity.setId(object.optString("id"));
                    entity.setIsOn(object.optString("isOn"));
                    entity.setPattern(object.optString("pattern"));
                    entity.setTemperature(object.optString("temperature"));
                    entity.setRegisterAddress(object.optString("registerAddress"));
                    entity.setPermission(object.optString("permission"));
                    entity.setRegisterNumber(object.optString("registerNumber"));
                    entity.setSpeed(object.optString("speed"));
                    entity.setDeviceName(object.optString("deviceName"));

                    idList.add(object.optString("deviceId"));
                    Log.e("===获取pppp",object.optString("permission"));
                    entityList.add(entity);
                }

                if (entityList.size()==0) {
                    addDeviceRel1.setVisibility(View.VISIBLE);
                    addDeviceRel2.setVisibility(View.GONE);
                }else {
                    addDeviceRel1.setVisibility(View.GONE);
                    addDeviceRel2.setVisibility(View.VISIBLE);
                }

                Log.e("===idListLog",idList.toString());
                adapter.notifyDataSetChanged();

                JSONArray array = jsonObject.optJSONArray("resourceList");
                Log.e("===功能码",array.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject1 = array.getJSONObject(i);
                    FunctionEntity function  = new FunctionEntity();
                    function.setPermission(jsonObject1.optString("permission"));
                    function.setRegisterAddress(jsonObject1.optString("registerAddress"));
                    function.setRegisterNumber(jsonObject1.optString("registerNumber"));
                    function.setRemark(jsonObject1.optString("remark"));
                    function.setResourceFlag(jsonObject1.optString("resourceFlag"));
                    function.setResourceName(jsonObject1.optString("resourceName"));

                    functionEntityList.add(function);

                }
            } else {
                Toast.makeText(AddSceneActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_Rel, R.id.saveRel, R.id.job_rel, R.id.rest_rel, R.id.out_rel,
            R.id.long_rel, R.id.week_rel, R.id.green_rel, R.id.add_device_rel1, R.id.add_device_rel2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Rel:
                if ("添加场景".equals(sceneName)) {
                    delteScene();
                }else {
                    finishs();
                }
                break;
            case R.id.saveRel:
                save();
                break;
            case R.id.job_rel:
                tag = 1;
                Message msg = myHandler.obtainMessage();
                msg.what = tag;
                myHandler.sendMessage(msg);
                break;
            case R.id.rest_rel:
                tag = 2;
                Message msg2 = myHandler.obtainMessage();
                msg2.what = tag;
                myHandler.sendMessage(msg2);
                break;
            case R.id.out_rel:
                tag = 3;
                Message msg3 = myHandler.obtainMessage();
                msg3.what = tag;
                myHandler.sendMessage(msg3);
                break;
            case R.id.long_rel:
                tag = 4;
                Message msg4 = myHandler.obtainMessage();
                msg4.what = tag;
                myHandler.sendMessage(msg4);
                break;
            case R.id.week_rel:
                tag = 5;
                Message msg5 = myHandler.obtainMessage();
                msg5.what = tag;
                myHandler.sendMessage(msg5);
                break;
            case R.id.green_rel:
                tag = 6;
                Message msg6 = myHandler.obtainMessage();
                msg6.what = tag;
                myHandler.sendMessage(msg6);
                break;
            case R.id.add_device_rel1:
                Intent intent = new Intent(this,SceneDeviceListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag","no");
                bundle.putString("sceneid",sceneid);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);

                break;
            case R.id.add_device_rel2:
                Intent intent2 = new Intent(this,SceneDeviceListActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("tag","yes");
                bundle2.putString("sceneid",sceneid);
                bundle2.putStringArrayList("idList", (ArrayList<String>) idList);
                intent2.putExtras(bundle2);
                startActivityForResult(intent2,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&&resultCode==0) {
            entityList.clear();
            functionEntityList.clear();
            idList.clear();
            getSceneDetails();
            adapter.notifyDataSetChanged();
            if (entityList.size()==0) {
                addDeviceRel1.setVisibility(View.VISIBLE);
                addDeviceRel2.setVisibility(View.GONE);
            }else {
                addDeviceRel1.setVisibility(View.GONE);
                addDeviceRel2.setVisibility(View.VISIBLE);
            }

        } else if (requestCode==1&&resultCode==0) {
            String deviceId = data.getStringExtra("deviceId");
            String pattern = data.getStringExtra("pattern");
            String speed = data.getStringExtra("speed");
            String temperature = data.getStringExtra("temperature");
            String resourceFlag_pattern = data.getStringExtra("resourceFlag_pattern");
            String resourceFlag_speed = data.getStringExtra("resourceFlag_speed");
            String resourceFlag_temp = data.getStringExtra("resourceFlag_temp");
            String resourceFlag_state = "";
            String p1 = "",p2 = "",p3 = "",p4 = "";
            for (int i = 0; i < functionEntityList.size(); i++) {
                if (functionEntityList.get(i).getResourceFlag().equals(resourceFlag_pattern)) {
                    p1 = functionEntityList.get(i).getPermission();
                }
            }

            for (int i = 0; i < functionEntityList.size(); i++) {
                if (functionEntityList.get(i).getResourceFlag().equals(resourceFlag_speed)) {
                    p3 = functionEntityList.get(i).getPermission();
                }

            }
            for (int i = 0; i < functionEntityList.size(); i++) {
                if (functionEntityList.get(i).getResourceFlag().equals(resourceFlag_temp)) {
                    p2 = temperature;
                }

            }

            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i).getDeviceId().equals(deviceId)) {
                    if (entityList.get(i).getIsOn().equals("0")) {
                        resourceFlag_state = "state_close";
                    }else {
                        resourceFlag_state =  "state_open";
                    }
                }

            }

            for (int i = 0; i < functionEntityList.size(); i++) {
                if ("春泉温控器".equals(functionEntityList.get(i).getRemark())) {
                    if (functionEntityList.get(i).getResourceFlag().equals(resourceFlag_state)) {
                        p4 = functionEntityList.get(i).getPermission();
                    }
                }
            }
            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i).getDeviceId().equals(deviceId)) {
                    entityList.get(i).setPattern(pattern);
                    entityList.get(i).setSpeed(speed);
                    entityList.get(i).setTemperature(temperature);
                    entityList.get(i).setPermission(p1+","+p2+","+p3+","+p4);
                }
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void delteScene() {
        SuccinctProgress.showSuccinctProgress(this, "请稍等...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.DELTESCENE);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("sceneId",sceneid);
        params.setConnectTimeout(5 * 1000);
        params.setReadTimeout(5 * 1000);
        x.http().request(HttpMethod.GET,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SuccinctProgress.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)) {
                        finishs();
                    }else{
                        Toast.makeText(AddSceneActivity.this, code, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(AddSceneActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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

    private void finishs() {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    private void save() {
        String name = sceneNameEt.getText().toString();
        Gson gson = new Gson();
        if (name.isEmpty()) {
            Toast.makeText(this, "请填写场景名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tag == 0) {
            Toast.makeText(this, "请选择图标", Toast.LENGTH_SHORT).show();
        }

        SuccinctProgress.showSuccinctProgress(this, "正在保存...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.SAVESCENE);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("sceneId",sceneid);
        params.addQueryStringParameter("userId",userid);
        params.addQueryStringParameter("sceneName",name);
        params.addQueryStringParameter("sceneImg",tag+"");
        Log.e("===图标",tag+"");
        params.addQueryStringParameter("deviceList",gson.toJson(entityList));
        params.setReadTimeout(5000);
        params.setConnectTimeout(5000);
        Log.e("===保存list",gson.toJson(entityList));

        Log.e("",sceneid);

        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                json(result);
                SuccinctProgress.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(AddSceneActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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
            JSONObject object = new JSONObject(result);
            String code = object.optString("result");
            Log.e("===code",code);
            if (code.equals("1")) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            } else {
                Toast.makeText(AddSceneActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private MyHandler myHandler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    jobIv.setVisibility(View.VISIBLE);
                    restIv.setVisibility(View.GONE);
                    outIv.setVisibility(View.GONE);
                    longIv.setVisibility(View.GONE);
                    weekIv.setVisibility(View.GONE);
                    greenIv.setVisibility(View.GONE);
                    goIv.setImageResource(R.mipmap.samll_job_modle_icon);
                    break;
                case 2:
                    jobIv.setVisibility(View.GONE);
                    restIv.setVisibility(View.VISIBLE);
                    outIv.setVisibility(View.GONE);
                    longIv.setVisibility(View.GONE);
                    weekIv.setVisibility(View.GONE);
                    greenIv.setVisibility(View.GONE);
                    goIv.setImageResource(R.mipmap.samll_rest_moldle_icon);
                    break;
                case 3:
                    jobIv.setVisibility(View.GONE);
                    restIv.setVisibility(View.GONE);
                    outIv.setVisibility(View.VISIBLE);
                    longIv.setVisibility(View.GONE);
                    weekIv.setVisibility(View.GONE);
                    greenIv.setVisibility(View.GONE);
                    goIv.setImageResource(R.mipmap.samll_out_modle_icon);
                    break;
                case 4:
                    jobIv.setVisibility(View.GONE);
                    restIv.setVisibility(View.GONE);
                    outIv.setVisibility(View.GONE);
                    longIv.setVisibility(View.VISIBLE);
                    weekIv.setVisibility(View.GONE);
                    greenIv.setVisibility(View.GONE);
                    goIv.setImageResource(R.mipmap.small_longrest_modle_icon);
                    break;
                case 5:
                    jobIv.setVisibility(View.GONE);
                    restIv.setVisibility(View.GONE);
                    outIv.setVisibility(View.GONE);
                    longIv.setVisibility(View.GONE);
                    weekIv.setVisibility(View.VISIBLE);
                    greenIv.setVisibility(View.GONE);
                    goIv.setImageResource(R.mipmap.samll_week_modle_icon);
                    break;
                case 6:
                    jobIv.setVisibility(View.GONE);
                    restIv.setVisibility(View.GONE);
                    outIv.setVisibility(View.GONE);
                    longIv.setVisibility(View.GONE);
                    weekIv.setVisibility(View.GONE);
                    greenIv.setVisibility(View.VISIBLE);
                    goIv.setImageResource(R.mipmap.samll_green_modle_icon);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ("添加场景".equals(sceneName)) {
            delteScene();
        }else {
            finishs();
        }
        super.onBackPressed();
    }
}
