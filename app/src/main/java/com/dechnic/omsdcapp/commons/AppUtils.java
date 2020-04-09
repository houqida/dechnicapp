package com.dechnic.omsdcapp.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dechnic.omsdcapp.entity.UserInfo;

/**
 * Created by Administrator on 2017/3/21.
 * 本地存储 token，用户登录信息等
 */

public class AppUtils {
    /**
     *AccessToken的存储与获取
     */
    public static String getAccessToken(Context context) {
        String value = context.getSharedPreferences(Constants.SP_ACCESSTOKEN, Context.MODE_PRIVATE)
                .getString(Constants.ACCESSTOKEN, null);
        Log.e("accessToken", "" + value);
        return value;
    }
    public static void setAccessToken(Context context, String accessName) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_ACCESSTOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_token = sp.edit();
        editor_token.putString(Constants.ACCESSTOKEN, accessName);
        editor_token.commit();
        return;
    }


    //保存服务器地址
    public static String getServerAdress(Context context) {
        String value = context.getSharedPreferences(Constants.IS_SERVER_ADRESS, Context.MODE_PRIVATE)
                .getString(Constants.SERVER_ADRESS, Constants.SERVER_ADDR);
        if (value == null || value.equals("")) value = Constants.SERVER_ADDR;
        Log.e("serverAdress", "" + value);
        return value;
    }
    public static void setServerAdress(Context context, String adress) {
        SharedPreferences sp = context.getSharedPreferences(Constants.IS_SERVER_ADRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_token = sp.edit();
        editor_token.putString(Constants.SERVER_ADRESS, adress);
        editor_token.commit();
        return;
    }


    /**
     * 用户信息的存储与获取
     * @param context
     * @param userInfo
     */

    public static void setUserInfo(Context context, UserInfo userInfo) {
        try {
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_USERINFO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_user = sp.edit();
            editor_user.putString(Constants.USERINFO, SerializableUtil.obj2Str(userInfo));
            Log.e("===obj2str",SerializableUtil.obj2Str(userInfo));
            editor_user.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    public static UserInfo getUserInfo(Context context) {
        UserInfo userinfo = null;
        try {
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_USERINFO, Context.MODE_PRIVATE);
            String userStr = sp.getString(Constants.USERINFO, null);
            if (userStr == null) {
            } else {
                userinfo = (UserInfo) SerializableUtil.str2Obj(userStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userinfo;
    }


    public static void setSharedPreferences(Context context, String SharedPreferencesName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_user = sp.edit();
        editor_user.putString(key, value);
        editor_user.commit();
        return;
    }

    public static String getSharedPreferences(Context context, String SharedPreferencesName, String key) {
        String value = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE).getString(key, null);
        Log.d(key, "" + value);
        return value;
    }


    public static void setPassWord(Context context, String password){
        SharedPreferences sp = context.getSharedPreferences(Constants.IS_REMEMBER, 0);
        SharedPreferences.Editor editor_user = sp.edit();
        editor_user.putString(Constants.PASSWORD,password);
        editor_user.commit();
        return;
    }

    public static String getPassWord(Context context){
        SharedPreferences setting = context.getSharedPreferences(Constants.IS_REMEMBER, 0);
        String passWord = setting.getString(Constants.PASSWORD,"");
        return passWord;

    }

    public static Boolean isAutoLoginCb(Context context){
        SharedPreferences setting = context.getSharedPreferences(Constants.IS_AUTO_LOGIN, 0);
        Boolean user_first = setting.getBoolean(Constants.AUTO_LOGIN, false);
        return user_first;

    }

    public static void setIsAutoLoginCb(Context context, boolean ret){
        SharedPreferences sp = context.getSharedPreferences(Constants.IS_AUTO_LOGIN, 0);
        SharedPreferences.Editor editor_user = sp.edit();
        editor_user.putBoolean(Constants.AUTO_LOGIN, ret);
        editor_user.commit();
        return;

    }


}
