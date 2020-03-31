package com.dechnic.omsdcapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.Base2Activity;
import com.dechnic.omsdcapp.commons.AppUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页
 */

public class SplashActivity extends Base2Activity {
    private Timer timer;
    private RelativeLayout activity_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTranslucent(this);
        activity_splash = (RelativeLayout) findViewById(R.id.activity_splash);
//        timer = new Timer();
//        timer.schedule(new MyTimerTask(), 2000);

        StartAniFour();

    }

    private void StartAniFour() {
        AlphaAnimation start = new AlphaAnimation(0.0f, 1.0f);
        start.setDuration(3000);
        // findViewById(R.id.splash).startAnimation(start);
        start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        activity_splash.startAnimation(start);
    }

    private void startMainActivity() {
        if (AppUtils.isAutoLoginCb(SplashActivity.this)) {//自动登陆
            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();

        }else {
            Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            if (AppUtils.isAutoLoginCb(SplashActivity.this)) {//自动登陆
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

            }else {
                Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
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
}
