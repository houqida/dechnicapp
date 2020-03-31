package com.dechnic.omsdcapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.Base2Activity;
import com.dechnic.omsdcapp.commons.AppUtils;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;
import com.dechnic.omsdcapp.entity.UserInfo;
import com.dechnic.omsdcapp.url.HttpURL;
import com.dechnic.omsdcapp.widget.SuccinctProgress;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户登录类
 */
public class UserLoginActivity extends Base2Activity {

    private EditText nameTv, passwordTv;//用户名和密码
    private CheckBox isRemberCb, isAutoLoginCb;//记住密码，自动登录，默认选中状态
    private Button loginBtn;//登陆按钮
    private RelativeLayout companyNameRelLay;//显示公司名称，可点击，进入服务器绑定界面
    private String serverAdress = "";
    private String userPw;
    private String userName;
    private String access_token;
    private String controller;
    private String socket;
    private String switchs;

    UserInfo userInfo = new UserInfo();

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    private String deviceid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setTranslucent(this);
        initViews();//初始化控件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//动态获取权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
////                deviceid = tm.getDeviceId();
                deviceid = Settings.System.getString(UserLoginActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        } else {
//            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            deviceid = tm.getDeviceId();
            deviceid = Settings.System.getString(UserLoginActivity.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
//                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                    deviceid = tm.getDeviceId();
                     deviceid = Settings.System.getString(UserLoginActivity.this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                } else {
                    // Permission Denied
//                    WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                    if (manager != null) {
//                        deviceid = manager.getConnectionInfo().getMacAddress();
//                    }
                     deviceid = Settings.System.getString(UserLoginActivity.this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initViews() {
        nameTv = (EditText) findViewById(R.id.nameTv);
        passwordTv = (EditText) findViewById(R.id.passwordTv);
        final UserInfo userInfo = AppUtils.getUserInfo(UserLoginActivity.this);
        if (userInfo != null) {
            nameTv.setText(userInfo.getUserName());
        }

        passwordTv.setText(AppUtils.getPassWord(this) + "");

        nameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordTv.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                return false;
            }
        });

        isRemberCb = (CheckBox) findViewById(R.id.isRemberCb);
        isAutoLoginCb = (CheckBox) findViewById(R.id.isAutoLoginCb);

        isAutoLoginCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRemberCb.setChecked(true);
                }
            }
        });
        loginBtn = (Button) findViewById(R.id.loginBtn);
        companyNameRelLay = (RelativeLayout) findViewById(R.id.companyNameRelLay);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("===>",deviceid);
                submit();
            }
        });

        companyNameRelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intoActivty();
            }
        });

    }

    private void submit() {
//        String url = HttpURL.HTTP + AppUtils.getServerAdress(this)+HttpURL.Logins;
        userName = nameTv.getText().toString();
        userPw = passwordTv.getText().toString();
        if (NetWorkUtils.getNetState(this) == Constants.NO_NETWORK) {
            Toast.makeText(UserLoginActivity.this, "您的网络有问题，无法连接网络！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
            return;
        }
        if (userName.isEmpty() || userPw.isEmpty()) {
            Toast.makeText(UserLoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
            SuccinctProgress.dismiss();
            return;
        }
        SuccinctProgress.showSuccinctProgress(UserLoginActivity.this, "正在登录...",
                SuccinctProgress.THEME_ARC, true, true);
        RequestParams login = new RequestParams(HttpURL.Url(this) + HttpURL.Logins);
        Log.e("===login", HttpURL.Url(this) + HttpURL.Logins);
        login.addQueryStringParameter("deviceToken", deviceid);
        Log.e("---",deviceid);

        login.addQueryStringParameter("userName", userName);
        login.addQueryStringParameter("password", userPw);
        login.setConnectTimeout(5000);
        login.setReadTimeout(5000);
        Log.e("---","开始");
        x.http().post(login, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {//请求成功，返回json串
                SuccinctProgress.dismiss();
                Message message = handler.obtainMessage();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.obj = bundle;
                handler.sendMessage(message);
                Log.e("==result", result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//请求失败，弹出错误信息
                Log.e("====test", ex.getMessage());
                Log.e("---","结束");
                Toast.makeText(UserLoginActivity.this, "服务器连接失败，请检查IP地址并重新登录", Toast.LENGTH_SHORT).show();
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

    private void intoActivty() {
        Intent intent = new Intent(this, ServerAdressActivity.class);
        startActivity(intent);
        finish();
    }


    private void pullJson(String result) {
        //执行数据解析操作
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.optString("code");
            if ("0".equals(code)) {
                String error = jsonObject.optString("error");
                SuccinctProgress.showError(this, error, true, true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SuccinctProgress.dismiss();
                    }
                },1000);
            } else {
                access_token = jsonObject.optString("access_token");
                controller = jsonObject.optString("device_controller");
                socket = jsonObject.optString("device_socket");
                switchs = jsonObject.optString("device_switch");
                //保存数据
                saveMessage();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveMessage() {
        //保存accesstoken
        AppUtils.setAccessToken(getApplicationContext(), access_token);
        //执行数据保存
        //保存是否记住自动登陆
        if (isAutoLoginCb.isChecked()) {
            AppUtils.setIsAutoLoginCb(this, true);
            userInfo.setAutoLogin(true);
        } else {
            AppUtils.setIsAutoLoginCb(this, false);
            userInfo.setAutoLogin(false);
        }

        //保存密码
        if (isRemberCb.isChecked()) {
            AppUtils.setPassWord(this, userPw);
            userInfo.setRemberPw(true);
        } else {
            AppUtils.setPassWord(this, "");
            userInfo.setRemberPw(false);
        }
        userInfo.setAccessToken(access_token);
        userInfo.setIsLogin("Y");
        userInfo.setUserName(userName);
        userInfo.setUserPassWord(userPw);
        if ((null != controller && !"".equals(controller)&&Integer.valueOf(controller)!=0)
                && (null != socket && !"".equals(socket)&&Integer.valueOf(socket)!=0)
                && (null != switchs && !"".equals(switchs)&&Integer.valueOf(switchs)!=0)) {
            userInfo.setDevice_control(controller);
            userInfo.setDevice_socket(socket);
            userInfo.setDevice_switch(switchs);
        } else {
            userInfo.setDevice_control("1");
            userInfo.setDevice_switch("2");
            userInfo.setDevice_socket("3");
        }

        AppUtils.setUserInfo(this, userInfo);

        handler.sendEmptyMessage(1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Bundle bundle0 = (Bundle) msg.obj;
                    String result = bundle0.getString("result");
                    pullJson(result);
                    break;
                case 1:
                    Intent intent = new Intent(UserLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("===requestCode", requestCode + "");
//        Log.e("==resultCode+", resultCode + "");
//        if (requestCode == 0 && resultCode == 0) {
//            serverAdress = data.getStringExtra("adress");
//            AppUtils.setServerAdress(this,serverAdress);
//        }
//
//    }

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
    protected void onDestroy() {
        super.onDestroy();

    }
}
