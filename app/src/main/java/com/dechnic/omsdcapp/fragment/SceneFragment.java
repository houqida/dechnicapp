package com.dechnic.omsdcapp.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.entity.SceneEntity;
import com.dechnic.omsdcapp.scene.activity.AddSceneActivity;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.LineGridView;
import com.dechnic.omsdcapp.base.BaseFragment;
import com.dechnic.omsdcapp.scene.adapter.SceneShowGridAdapter;
import com.dechnic.omsdcapp.widget.AlertDialog;
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
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/22.
 * 场景
 */

public class SceneFragment extends BaseFragment {
    @Bind(R.id.scene_Gv)
    LineGridView sceneGv;
    @Bind(R.id.addScene_btn)
    Button addSceneBtn;
    @Bind(R.id.empty_Rel)
    RelativeLayout emptyRel;

    private SceneShowGridAdapter adapter;
    private List<SceneEntity> list;
    private SceneEntity entity;
    PopupWindow popupWindow;
    private String userid;
    private String newSceneId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_scene);
        ButterKnife.bind(this, getContentView());
        initGrigViews();
        return getContentView();
    }

    private void initGrigViews() {
        sceneGv.setEmptyView(emptyRel);
        list = new ArrayList<>();
        getSceneList();
        adapter = new SceneShowGridAdapter(getActivity(), list);
        sceneGv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sceneGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list.get(position).getSceneName();
                if ("添加场景".equals(name)) {
                    handler.sendEmptyMessage(0);
                } else {
                    //执行群控操作
                    showOperateDialog(position);

                }

            }
        });


        sceneGv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!"添加场景".equals(list.get(position).getSceneName())) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    initPopup(view, position);
                }

                return true;
            }
        });

    }

    private void showOperateDialog(final int position) {
        new AlertDialog(getActivity()).builder().setTitle("群控")
                .setMsg("确认群控操作吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operate(position);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();

            }
        }).show();
    }

    //执行群控操作
    private void operate(int position) {
        Toast.makeText(getActivity(),"正在执行群控操作...",Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams(HttpURL.Url(getActivity())+HttpURL.CONTROLSCENE);
        params.addHeader("access_token",AppUtils.getAccessToken(getActivity()));
        params.addQueryStringParameter("sceneId",list.get(position).getSceceId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

    private void initPopup(final View vs, final int pos) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.scene_longclick_popup, null);
        RelativeLayout edit_gv_rel = (RelativeLayout) view.findViewById(R.id.edit_gv_rel);
        RelativeLayout delet_gv_rel = (RelativeLayout) view.findViewById(R.id.delet_gv_rel);

        int weight = vs.getWidth();
        int height = vs.getHeight();

        popupWindow = new PopupWindow(view, weight, height, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(vs, 0, -height);

        edit_gv_rel.setOnClickListener(new View.OnClickListener() {//编辑场景
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), AddSceneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sceneId",list.get(pos).getSceceId());
                bundle.putString("userid",list.get(pos).getUserId());
                bundle.putString("sceneName",list.get(pos).getSceneName());
                bundle.putString("sceneIv",list.get(pos).getSceneIv());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        delet_gv_rel.setOnClickListener(new View.OnClickListener() {//删除场景
            @Override
            public void onClick(View v) {
                showDialog(vs, pos);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            initGrigViews();
        }
    }

    private void showDialog(final View vs, final int pos) {
        new AlertDialog(getActivity()).builder().setTitle("删除场景")
                .setMsg("确认删除" + list.get(pos).getSceneName() + "吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Toast.makeText(getActivity(), "确认删除", Toast.LENGTH_SHORT).show();
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", pos);
                        message.obj = bundle;
                        handler.sendMessage(message);//执行删除场景的操作

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        }).show();
    }


    private void getSceneList() {
        if (NetWorkUtils.getNetState(getActivity()) == Constants.NO_NETWORK) {
            Toast.makeText(getActivity(), "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        } else {
            SuccinctProgress.showSuccinctProgress(getActivity(), "加载中...", SuccinctProgress.THEME_ARC, true, true);
            RequestParams params = new RequestParams(HttpURL.Url(getActivity()) + HttpURL.SCENELIST);
            params.addHeader("access_token", AppUtils.getAccessToken(getActivity()));
            x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    json(result);
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Toast.makeText(getActivity(), "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
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
            JSONArray array = new JSONArray(result);
            Log.e("====scene_array", array.toString());
            for (int i = 0; i < array.length(); i++) {
                entity = new SceneEntity();
                JSONObject object = array.getJSONObject(i);
                String sceneImg = object.optString("sceneImg");
                String sceneName = object.optString("sceneName");
                String id = object.optString("id");
                String userId = object.optString("userId");

                entity.setSceceId(id);
                entity.setSceneIv(sceneImg);
                entity.setSceneName(sceneName);
                entity.setUserId(userId);

                list.add(entity);

            }
            if (list.size()>0) {
                SceneEntity entity1 = new SceneEntity();
                entity1.setSceneIv("7");
                entity1.setSceneName("添加场景");
                list.add(entity1);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addScene_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addScene_btn:
                handler.sendEmptyMessage(0);
                break;
        }
        ;
    }


    private void executeDelScene(final int position) {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请稍等...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(getActivity()) + HttpURL.DELTESCENE);
        params.addHeader("access_token", AppUtils.getAccessToken(getActivity()));
        params.addQueryStringParameter("sceneId",list.get(position).getSceceId());
        params.setConnectTimeout(5 * 1000);
        params.setReadTimeout(5 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SuccinctProgress.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)) {
                        Message message = handler.obtainMessage();
                        message.what = 3;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        message.obj = bundle;
                        handler.sendMessage(message);
                    }else{
                        Toast.makeText(getActivity(), code, Toast.LENGTH_SHORT).show();
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

    private void executeAddScene() {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请稍等...", SuccinctProgress.THEME_ARC, true, true);
        RequestParams params = new RequestParams(HttpURL.Url(getActivity()) + HttpURL.CLICKTOADDSCENE);
        params.addHeader("access_token", AppUtils.getAccessToken(getActivity()));
        params.setConnectTimeout(5 * 1000);
        params.setReadTimeout(5 * 1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SuccinctProgress.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String code = object.optString("result");
                    if ("1".equals(code)) {
                        userid = object.optString("userId");
                        newSceneId = object.optString("sceneId");
                        handler.sendEmptyMessage(1);
                    } else {
                        Toast.makeText(getActivity(), code, Toast.LENGTH_SHORT).show();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://执行添加场景操作，先请求接口，创建一个id
                    executeAddScene();
                    break;
                case 1://执行跳转操作，跳转到添加场景的界面
                    Intent intent = new Intent(getActivity(), AddSceneActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sceneId", newSceneId);
                    bundle.putString("userid",userid);
                    bundle.putString("sceneName","添加场景");
                    bundle.putString("sceneIv","7");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                    break;

                case 2://执行删除场景操作
                    Bundle bundles = (Bundle) msg.obj;
                    int position = bundles.getInt("position");
                    executeDelScene(position);
                    break;
                case 3://更新列表
                    Bundle b = (Bundle) msg.obj;
                    int pos = b.getInt("position");
                    initGrigViews();
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };

}
