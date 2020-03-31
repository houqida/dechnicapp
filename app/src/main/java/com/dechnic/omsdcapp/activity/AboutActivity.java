package com.dechnic.omsdcapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.url.HttpURL;

import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Utils;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * “关于”界面
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.backRel)
    RelativeLayout backRel;
    @Bind(R.id.versionTv)
    TextView versionTv;
    @Bind(R.id.isNewTv)
    TextView isNewTv;
    @Bind(R.id.phoneNumberTv)
    TextView phoneNumberTv;
    @Bind(R.id.iphoneIv)
    ImageView iphoneIv;
    @Bind(R.id.androidIv)
    ImageView androidIv;
    @Bind(R.id.wechatIv)
    ImageView wechatIv;
    @Bind(R.id.messageTv)
    TextView messageTv;

    private String id;
    private String androidQR;
    private String phone;
    private String weChatQR;
    private String iphoneQR;
    private long updatedOn;
    private long createdOn;
    private String versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        versionTv.setText(Utils.getVersion(this));
        getDetail();
    }

    private void getDetail(){
        SuccinctProgress.showSuccinctProgress(this,"获取中...",SuccinctProgress.THEME_ARC,true,true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        }else {
            RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.ABOUTMESSAGE);
            params.addHeader("access_token", AppUtils.getAccessToken(this));
            params.setReadTimeout(5000);
            params.setConnectTimeout(5000);
            x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    SuccinctProgress.dismiss();
                    josn(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SuccinctProgress.dismiss();
                    Toast.makeText(AboutActivity.this,R.string.http_error,Toast.LENGTH_SHORT).show();
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

    private void josn(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject object = jsonArray.getJSONObject(0);
            id = object.optString("id");
            androidQR = object.optString("androidQR");
            phone = object.optString("phone");
            weChatQR = object.optString("weChatQR");
            iphoneQR = object.optString("iphoneQR");
            updatedOn = object.optLong("updatedOn");
            createdOn = object.optLong("createdOn");
            versionCode = object.optString("versionCode");
            Message msg = handler.obtainMessage();
            msg.what=1;
            handler.sendMessage(msg);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                phoneNumberTv.setText(phone);
                x.image().bind(iphoneIv,iphoneQR);
                x.image().bind(androidIv,androidQR);
                x.image().bind(wechatIv,weChatQR);
            }
        }
    };
    @OnClick(R.id.backRel)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backRel:
                finish();
                break;
        }
    }
}
