package com.dechnic.omsdcapp.application;

import android.app.Application;
import android.content.Context;

import com.dechnic.omsdcapp.activity.HomeActivity;
import com.dechnic.omsdcapp.commons.Constants;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CCApplication extends Application{
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        closeAndroidPDialog();
        x.Ext.init(this);//初始化xtils
        Config.DEBUG = false;
        UMShareAPI.get(this);
        //检查内存泄露
//        refWatcher=LeakCanary.install(this);

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 2 * 1000;
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        Beta.canShowUpgradeActs.add(HomeActivity.class);
        Bugly.init(getApplicationContext(), Constants.BUGLY_APP_ID, false);


        //分享key   换成自己的
        PlatformConfig.setWeixin("wx522c49b1ca4a7084", "fc06a95ae031dd9806a8846cda46363a");//已换
        PlatformConfig.setQQZone("1106149363", "ajbBmvY3tHV2Iq5E");//
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");


    }
//    public static RefWatcher getRefWatcher(Context context) {
//        CCApplication application = (CCApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//    private RefWatcher refWatcher;

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



