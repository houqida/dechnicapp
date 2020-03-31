package com.dechnic.omsdcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.adapter.HelpMessageListAdapter;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.entity.HelpMessage;
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
 * 使用帮助类
 */
public class UseHelpfulActivity extends BaseActivity {

    private RelativeLayout backRel;
    private ListView lv;
    private List<HelpMessage> list;
    private HelpMessage message;
    private HelpMessageListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_helpful);
        initView();
    }

    private void initView() {
        backRel = (RelativeLayout) findViewById(R.id.backRel);
        backRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv = (ListView) findViewById(R.id.helpLv);
        list = new ArrayList<>();
        getList();
        adapter = new HelpMessageListAdapter(this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("content",list.get(position).getContent());
                bundle.putString("title",list.get(position).getTitle());
                Intent intent = new Intent(UseHelpfulActivity.this,UseHelpfulDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void getList(){
        SuccinctProgress.showSuccinctProgress(this,"加载中...",SuccinctProgress.THEME_ARC,true,true);
        RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.HELPMESSAGE);
        params.addHeader("access_token", AppUtils.getAccessToken(this));
        params.setConnectTimeout(5000);
        params.setReadTimeout(5000);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pullJson(result);
                SuccinctProgress.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                Toast.makeText(UseHelpfulActivity.this,
                        ex.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void pullJson(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                message = new HelpMessage();
                JSONObject object = array.getJSONObject(i);
                String id = object.optString("id");
                long updatedOn = object.optLong("updatedOn");
                long createdOn = object.optLong("createdOn");
                String title = object.optString("title");
                String content = object.optString("content");

                Date date = new Date(updatedOn);
                Date date1 = new Date(createdOn);
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                String updateTime = sdf.format(date);
                String createTime = sdf.format(date1);

                message.setId(id);
                message.setTitle(title);
                message.setContent(content);
                message.setUpdatedOn(updateTime);
                message.setCreatedOn(createTime);

                list.add(message);

            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
