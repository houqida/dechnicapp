package com.dechnic.omsdcapp.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/3/21.
 */

public class NetWorkUtils {
    //获取网络状态
    /**
     * 获取网络状态
     *
     * @param context
     * @return 1=wifi 2=mobile 0=nonetwork
     */
    public static int getNetState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isAvailable() && wifi.isConnected()) {
            // Toast.makeText(context, "当前有可用的wifi网络连接", 0).show();
            return Constants.WIFI;
        } else if (mobile != null && mobile.isAvailable()
                && mobile.isConnected()) {
            // Toast.makeText(context, "当前有可用的mobile网络连接", 0).show();
            return Constants.MOBILE;
        } else {
            // Toast.makeText(context, "当前无可用的网络连接", 0).show();
            return Constants.NO_NETWORK;
        }
    }
}
