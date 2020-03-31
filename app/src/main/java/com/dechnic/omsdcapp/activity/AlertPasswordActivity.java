package com.dechnic.omsdcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.BaseActivity;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.entity.UserInfo;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

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
 * 修改密码的类
 */
public class AlertPasswordActivity extends BaseActivity {

    @Bind(R.id.cacleRel)
    RelativeLayout cacleRel;
    @Bind(R.id.saveRel)
    RelativeLayout saveRel;
    @Bind(R.id.accountTv)
    TextView accountTv;
    @Bind(R.id.oldPassEd)
    EditText oldPassEd;
    @Bind(R.id.newPassEd)
    EditText newPassEd;
    @Bind(R.id.surenewPassEd)
    EditText surenewPassEd;

    UserInfo userInfo;
    private String oldPass;
    private String newPass;
    private String surenewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_password);
        userInfo = AppUtils.getUserInfo(this);
        ButterKnife.bind(this);
        accountTv.setText(userInfo.getUserName());

    }

    @OnClick({R.id.cacleRel, R.id.saveRel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cacleRel:
                finish();
                break;
            case R.id.saveRel:
                save();
                break;
        }
    }

    private void save(){
        oldPass = oldPassEd.getText().toString();
        newPass = newPassEd.getText().toString();
        surenewPass = surenewPassEd.getText().toString();

        if (oldPass.isEmpty()) {
            Toast.makeText(this,"当前登录密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.isEmpty()) {
            Toast.makeText(this,"新密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (surenewPass.isEmpty()) {
            Toast.makeText(this,"请再次填写新密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(surenewPass)) {
            Toast.makeText(this,"两次密码填写不一致！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.length()<6||newPass.length()>20) {
            Toast.makeText(this,"密码为6-20位数字、字母组合",Toast.LENGTH_SHORT).show();
            return;
        }
        SuccinctProgress.showSuccinctProgress(this,"保存中...",SuccinctProgress.THEME_ARC,true,true);
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
        }else {
            RequestParams params = new RequestParams(HttpURL.Url(this)+HttpURL.UPDATEPASSWORD);
            params.addHeader("access_token",AppUtils.getAccessToken(this));
            params.addQueryStringParameter("oldPassword", oldPass);
            params.addQueryStringParameter("newPassword", newPass);
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
                    Toast.makeText(AlertPasswordActivity.this,R.string.http_error,Toast.LENGTH_SHORT).show();

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
            JSONObject object = new JSONObject(result);
            String results = object.optString("result");
            Log.e("==保存结果",results);
            if ("1".equals(results)) {
                AppUtils.setPassWord(this,newPass);
                userInfo.setRemberPw(true);
                AppUtils.setUserInfo(this,userInfo);
                Intent intent = new Intent(this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this,results,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
