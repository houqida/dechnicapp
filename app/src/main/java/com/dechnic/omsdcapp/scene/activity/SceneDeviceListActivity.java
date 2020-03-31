package com.dechnic.omsdcapp.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.entity.FunctionEntity;
import com.dechnic.omsdcapp.entity.SceneDeviceListEntity;
import com.dechnic.omsdcapp.scene.adapter.SceneAddDeviceListAdapter;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

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

/**
 * Created by Administrator on 2017/5/4.
 */

public class SceneDeviceListActivity extends BaseActivity {
    @Bind(R.id.back_Rel)
    RelativeLayout backRel;
    @Bind(R.id.devideList)
    ListView devideLv;
    private SceneAddDeviceListAdapter adapter;
    private List<SceneDeviceListEntity> entityList;
    private List<FunctionEntity> functionEntityList;
    private SceneDeviceListEntity entity;

    private Bundle bundle;
    private List<String> idList = new ArrayList<>();
    private String sceneid = "0";
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_device_list);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        sceneid = bundle.getString("sceneid");
        Log.e("===传来的sceneid",sceneid);
        if ("yes".equals(tag)) {//场景中有设备的情况
            idList = bundle.getStringArrayList("idList");
        }
        initViews();
    }

    private void initViews() {
        backRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });
        entityList = new ArrayList<>();
        functionEntityList = new ArrayList<>();
        getDeviceList();
        adapter = new SceneAddDeviceListAdapter(this, entityList);
        devideLv.setAdapter(adapter);
        adapter.setListener(new SceneAddDeviceListAdapter.onAddOrDelteListener() {
            @Override
            public void onChanged(String id, String flag) {
                if ("1".equals(flag)) {//执行移除操作
                    remove(id);
                } else {//执行添加操作
                    insert(id);
                }
            }
        });

    }

    private void insert(final String id) {
        String deviceKind = "";
        String deviceFirm = "";
        String deviceStation = "";
        String deviceCode = "";
        String deviceName = "";
        String permission1 = "";
        String permission2 = "";
        String permission3 = "";
        String permission4 = "";
        String registerAddress = "";
        int switchNumber = 0;
        for (int i = 0; i < entityList.size(); i++) {
            if (id.equals(entityList.get(i).getId())) {
                deviceKind = entityList.get(i).getDeviceKind();
                deviceFirm = entityList.get(i).getDeviceFirm();
                deviceCode = entityList.get(i).getDeviceCode();
                deviceStation = entityList.get(i).getDeviceStation();
                deviceName = entityList.get(i).getDeviceName();
                switchNumber =  entityList.get(i).getSwitchNumber();
            }

        }
        SuccinctProgress.showSuccinctProgress(this,"正在添加...",SuccinctProgress.THEME_ARC,true,true);
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.SCENEINSERTDEVICE);
        params.setReadTimeout(5000);
        params.setConnectTimeout(5000);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("sceneId",sceneid);
        params.addQueryStringParameter("deviceId",id);
        params.addQueryStringParameter("deviceName",deviceName);
        params.addQueryStringParameter("deviceKind",deviceKind);
        params.addQueryStringParameter("deviceFirm",deviceFirm);
        params.addQueryStringParameter("deviceStation",deviceStation);
        params.addQueryStringParameter("deviceCode",deviceCode);

        if("温控器".equals(deviceKind)){
            for (int i = 0; i < functionEntityList.size(); i++) {
                if (functionEntityList.get(i).getResourceFlag().equals(Constants.PATTERN_COLD)) {
                    permission1 = functionEntityList.get(i).getPermission();
                } else if (functionEntityList.get(i).getResourceFlag().equals(Constants.SPEED_AUTO)) {
                    permission3 = functionEntityList.get(i).getPermission();
                } else if (functionEntityList.get(i).getResourceFlag().equals(Constants.temperature)) {
                    permission2 = 20+"";
                } else if (functionEntityList.get(i).getResourceFlag().equals(Constants.STATE_CLOSE)) {
                    permission4 = functionEntityList.get(i).getPermission();
                }
                registerAddress = functionEntityList.get(0).getRegisterAddress();

            }
            params.addQueryStringParameter("registerAddress",registerAddress);
            params.addQueryStringParameter("registerNumber","4");
            params.addQueryStringParameter("permission",permission1+","+permission2+","+permission3+","+permission4);
            Log.e("====permission",permission1+","+permission2+","+permission3+","+permission4);

        } else if ("开关".equals(deviceKind)) {
            for (int i = 0; i < functionEntityList.size(); i++) {
                if ("春泉开关".equals(functionEntityList.get(i).getRemark())) {
                    if (Constants.STATE_CLOSE.equals(functionEntityList.get(i).getResourceFlag())) {
                        int adress = (switchNumber - 1) * 2 + Integer.valueOf(functionEntityList.get(i).getRegisterAddress());
                        String str = String.format("%04d",adress);
                        registerAddress = str;
                        permission1 = functionEntityList.get(i).getPermission();
                    }
                }
            }
            params.addQueryStringParameter("registerAddress",registerAddress);
            Log.e(deviceKind,registerAddress);
            Log.e("===开关permision",permission1);
            params.addQueryStringParameter("permission",permission1);
            params.addQueryStringParameter("registerNumber","1");
        } else if ("插座".equals(deviceKind)) {
            for (int i = 0; i < functionEntityList.size(); i++) {
                if ("春泉插座".equals(functionEntityList.get(i).getRemark())) {
                    if (Constants.STATE_CLOSE.equals(functionEntityList.get(i).getResourceFlag())) {
                        registerAddress = functionEntityList.get(i).getRegisterAddress();
                        permission1 = functionEntityList.get(i).getPermission();
                    }
                }
            }
            params.addQueryStringParameter("registerAddress",registerAddress);
            Log.e(deviceKind,registerAddress);
            Log.e("===插座的permision",permission1);
            params.addQueryStringParameter("permission",permission1);
            params.addQueryStringParameter("registerNumber","1");
        }
        params.addQueryStringParameter("isOn","0");
        params.addQueryStringParameter("temperature","20");
        params.addQueryStringParameter("pattern","制冷");
        params.addQueryStringParameter("speed","自动");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)) {
                        for (int i = 0; i < entityList.size(); i++) {
                            if (id.equals(entityList.get(i).getId())) {
                                entityList.get(i).setFlag("1");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(SceneDeviceListActivity.this,code,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SceneDeviceListActivity.this,R.string.http_error,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                SuccinctProgress.dismiss();

            }
        });

    }

    private void remove(final String id) {
        SuccinctProgress.showSuccinctProgress(this, "正在移除...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.SCENEREMOVEDEVICE);
        params.setReadTimeout(5000);
        params.setConnectTimeout(5000);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.addQueryStringParameter("sceneId", sceneid);
        params.addQueryStringParameter("deviceId", id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SuccinctProgress.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)) {
                        for (int i = 0; i < entityList.size(); i++) {
                            if (id.equals(entityList.get(i).getId())) {
                                entityList.get(i).setFlag("2");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SceneDeviceListActivity.this, code, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(SceneDeviceListActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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

    private void getDeviceList() {
        SuccinctProgress.showSuccinctProgress(this, "加载中...", SuccinctProgress.THEME_ARC, true, true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        } else {
            RequestParams params = new RequestParams(HttpURL.Url(this) + HttpURL.SCENEALLDEVICE);
            params.addHeader("access_token", AppUtils.getAccessToken(this));
            params.setConnectTimeout(5000);
            params.setReadTimeout(5000);
            x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("====result.toString()",result.toString());
                    json(result);
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Log.e("===抛出异常",ex.getMessage());
                    Toast.makeText(SceneDeviceListActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();

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

    private void json(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.optString("result");
            if ("1".equals(code)) {
                JSONArray array = jsonObject.optJSONArray("deviceList");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    entity = new SceneDeviceListEntity();
                    entity.setDeviceStation(object.optString("deviceStation"));
                    entity.setDeviceFirm(object.optString("deviceFirm"));
                    entity.setDeviceName(object.optString("deviceName"));
                    entity.setDeviceCode(object.optString("deviceCode"));
                    entity.setId(object.optString("id"));
                    entity.setDeviceKind(object.optString("deviceKind"));
                    entity.setSwitchNumber(object.optInt("switchNumber"));

                    String id = object.optString("id");
                    Log.e("=====ididid",id);
                    entity.setFlag("2");
                    Log.e("===idList",idList.toString());
                    if ("yes".equals(tag)) {
                        for (int j = 0; j < idList.size(); j++) {
                            if (idList.get(j).toString().equals(id)) {
                                entity.setFlag("1");
                                break;
                            }else {
                                entity.setFlag("2");
                            }
                        }
                    }

                    entityList.add(entity);
                }

                adapter.notifyDataSetChanged();

                JSONArray jsonArray = jsonObject.optJSONArray("resourceList");
                Log.e("===设备列表中的功能码",jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    FunctionEntity function  = new FunctionEntity();
                    function.setPermission(jsonObject1.optString("permission"));
                    function.setRegisterAddress(jsonObject1.optString("registerAddress"));
                    function.setRegisterNumber(jsonObject1.optString("registerNumber"));
                    function.setRemark(jsonObject1.optString("remark"));
                    function.setResourceFlag(jsonObject1.optString("resourceFlag"));
                    function.setResourceName(jsonObject1.optString("resourceName"));

                    functionEntityList.add(function);

                }
                Log.e("=====resourceList",jsonArray.toString());
            } else {
                Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
        super.onBackPressed();
    }
}
