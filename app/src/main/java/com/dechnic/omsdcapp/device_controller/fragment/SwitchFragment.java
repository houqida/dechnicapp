package com.dechnic.omsdcapp.device_controller.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.LazyFragment;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.device_controller.switchs.activity.SwitchControlDetailActivity;
import com.dechnic.omsdcapp.device_controller.switchs.adapter.SwitchControlAdapter;
import com.dechnic.omsdcapp.entity.ControlEntity;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.SuccinctProgress;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class SwitchFragment extends LazyFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private static List<ControlEntity> list;
    private PullToRefreshListView switchLv;
    private static SwitchControlAdapter adapter;
    private RelativeLayout empty_Rel;
    int page = 1;
    int limit = 10;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private long startTime;//上一次加载时间
    int tag = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_switch);
        startTime = System.currentTimeMillis();
        initViews();
        return getContentView();
    }


    private void initViews() {
        list = new ArrayList<>();
        empty_Rel = (RelativeLayout) findViewById(R.id.empty_Rel);
        switchLv = (PullToRefreshListView) findViewById(R.id.switchLv);
        isPrepared = true;
        if (isVisible) {
            getSwitchList(page);
        }
        adapter = new SwitchControlAdapter(getActivity(), list);
        switchLv.setAdapter(adapter);
        switchLv.setMode(PullToRefreshBase.Mode.BOTH);
        switchLv.setOnRefreshListener(this);
        switchLv.setEmptyView(empty_Rel);

        adapter.setListener(new SwitchControlAdapter.onSwitchListener() {
            @Override
            public void onSwitchChanged(boolean status, String id) {
                for (int i = 0; i < list.size(); i++) {
                    if (id.equals(list.get(i).getControlId())) {
                        operationListDevice(i, status);//执行操作
                    }
                }

            }
        });
        switchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SwitchControlDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("position",position+"");
                bundle.putBoolean("isOnline", list.get(position - 1).isOnline());
                bundle.putBoolean("isOpen", list.get(position - 1).isOpen());
                bundle.putString("controlId", list.get(position - 1).getControlId());
                bundle.putString("devicename", list.get(position - 1).getRoomName() + list.get(position - 1).getControlName());
                bundle.putString("ekName", list.get(position - 1).getEkName());
                bundle.putString("deviceFirm", list.get(position - 1).getDeviceFirm());
                bundle.putInt("panelDisable",list.get(position-1).getPanelDisable());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            String  isOpen = data.getStringExtra("isOpen");
            int pos = Integer.valueOf(data.getStringExtra("position"));
            int dis = Integer.valueOf(data.getStringExtra("panelDisable"));
            list.get(pos-1).setPanelDisable(dis);
            if ("true".equals(isOpen)) {
                list.get(pos-1).setOpen(true);
            }else {
                list.get(pos-1).setOpen(false);
            }

            adapter.notifyDataSetChanged();

        }
    }

    private void getSwitchList(int page) {
//        SuccinctProgress.showSuccinctProgress(getActivity(), "正在加载...", SuccinctProgress.THEME_ARC, true, true);
        if (NetWorkUtils.getNetState(getActivity()) == Constants.NO_NETWORK) {
            Toast.makeText(getActivity(), "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        } else {
            showHttpLoading();
            RequestParams getList = new RequestParams(HttpURL.Url(getActivity()) + HttpURL.DEVICELIST);
            getList.addHeader("access_token", AppUtils.getAccessToken(getActivity()));
            Log.e("===access_token", AppUtils.getAccessToken(getActivity()));
            getList.addQueryStringParameter("name", "开关");
            getList.addQueryStringParameter("page", page + "");
            getList.addQueryStringParameter("limit", limit + "");
            getList.setConnectTimeout(5000);
            getList.setReadTimeout(5000);
            x.http().request(HttpMethod.GET, getList, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
//                    SuccinctProgress.dismiss();

                    pullJson(result);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("===ex", ex.getMessage());
//                    SuccinctProgress.dismiss();
                    Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_SHORT).show();
                    Log.e("===ex", "==");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onFinished() {
                    Log.e("===onFinished", "onFinished");
//                    SuccinctProgress.dismiss();
                    if (dialog!=null&&dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

        }
//        SuccinctProgress.dismiss();
    }

    private void pullJson(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            Log.e("==array-switch", jsonArray.toString());
            if (page == 1) {
                list.clear();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                ControlEntity controlEntity = new ControlEntity();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String isOn = jsonObject.optString("isOn");
                String id = jsonObject.optString("id");
                String deviceName = jsonObject.optString("deviceName");
                String roomName = jsonObject.optString("roomName");
                String status = jsonObject.optString("status");
                String deviceCode = jsonObject.optString("deviceCode");
                controlEntity.setControlId(id);
                controlEntity.setControlName(deviceName);
                controlEntity.setRoomName(roomName);
                controlEntity.setDeviceCode(deviceCode);
                controlEntity.setEkName(jsonObject.optString("deviceKind"));
                controlEntity.setDeviceFirm(jsonObject.optString("deviceFirm"));
                controlEntity.setSwitchNumber(jsonObject.optInt("switchNumber"));
                controlEntity.setPanelDisable(jsonObject.optInt("panelDisable"));
                if (isOn.equals("0")) {//开关处于关闭状态
                    controlEntity.setOpen(false);
                } else {//开关处于打开状态
                    controlEntity.setOpen(true);
                }

                if (status.equals("0")) {//状态处于离线状态
                    controlEntity.setOnline(false);
                } else {
                    controlEntity.setOnline(true);
                }


                list.add(controlEntity);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void operationListDevice(final int i, final boolean status) {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请稍等...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(getActivity()) + HttpURL.OPERATIONLISTDEVICE);
        params.addHeader("access_token", AppUtils.getAccessToken(getActivity()));
        params.addQueryStringParameter("deviceId", list.get(i).getControlId());

        if (status) {
            params.addQueryStringParameter("onType", "1");
        } else {
            params.addQueryStringParameter("onType", "0");
        }

        params.addQueryStringParameter("deviceKind", list.get(i).getEkName());
        params.addQueryStringParameter("deviceFirm", list.get(i).getDeviceFirm());
        params.addQueryStringParameter("switchNumber",list.get(i).getSwitchNumber()+"");

        Log.e("===这是几",list.get(i).getSwitchNumber()+"");

        params.setConnectTimeout(5000);
        params.setReadTimeout(5000);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.optString("result");
                    if ("1".equals(res) || "操作成功".equals(res)) {
                        Message msg = myHandler.obtainMessage();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("status", status);
                        bundle.putInt("i", i);
                        msg.obj = bundle;
                        myHandler.sendMessage(msg);
                    } else {
                        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_SHORT).show();

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

    private MyHandler myHandler = new MyHandler();

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        list.clear();
        getSwitchList(page);
        new FinishRefresh().execute();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = page + 1;
        getSwitchList(page);
        new FinishRefresh().execute();
    }

    @Override
    protected void lazyLoad() {
        long endTime = System.currentTimeMillis();
        if (isVisible && isPrepared) {
            page = 1;
            getSwitchList(page);
        }
    }

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            switchLv.onRefreshComplete();

        }
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle bundle = (Bundle) msg.obj;
                boolean status = bundle.getBoolean("status");
                int i = bundle.getInt("i");
                list.get(i).setOpen(status);
                adapter.notifyDataSetChanged();
                SuccinctProgress.dismiss();
            }
        }
    }
    private ProgressDialog dialog;

    public void showHttpLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            // 设置进度条风格，风格为圆形，旋转的
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("请稍等...");
        }
        dialog.show();
    }
    ;
}
