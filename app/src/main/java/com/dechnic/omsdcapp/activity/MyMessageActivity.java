package com.dechnic.omsdcapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.dechnic.omsdcapp.adapter.MyMessageListAdapter;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.entity.InfoMessage;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的消息
 */
public class MyMessageActivity extends BaseActivity {

    private RelativeLayout back;
    private PullToRefreshListView messLv;
    private List<InfoMessage> list;
    private InfoMessage message;
    private MyMessageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.backRel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messLv = (PullToRefreshListView) findViewById(R.id.messLv);
        list = new ArrayList<>();
        getMessageList();
        adapter = new MyMessageListAdapter(this,list);
        messLv.setAdapter(adapter);


    }

    private void getMessageList(){
        SuccinctProgress.showSuccinctProgress(this,"正在加载...",SuccinctProgress.THEME_LINE,true,true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        }else {
            RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.MESSAGELIST);
            params.addHeader("access_token", AppUtils.getAccessToken(this));
            params.setReadTimeout(5000);
            params.setConnectTimeout(5000);
            x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    pullJson(result);
                    SuccinctProgress.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Toast.makeText(MyMessageActivity.this,
                            R.string.http_error,Toast.LENGTH_SHORT).show();

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
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                message = new InfoMessage();
                JSONObject object = array.getJSONObject(i);
                String id = object.optString("id");
                long updatedOn = object.optLong("updatedOn");
                String title = object.optString("title");
                long createdOn = object.optLong("createdOn");
                String content = object.optString("content");

                message.setContent(content);
                message.setTitle(title);
                message.setId(id);

                Date date = new Date(updatedOn);
                Date date1 = new Date(createdOn);
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                String updateTime = sdf.format(date);
                String createTime = sdf.format(date1);

                message.setCreatedOn(createTime);
                message.setUpdatedOn(updateTime);

                list.add(message);
            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
