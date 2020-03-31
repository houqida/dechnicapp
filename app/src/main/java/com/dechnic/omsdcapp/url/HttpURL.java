package com.dechnic.omsdcapp.url;

import android.content.Context;

import com.dechnic.omsdcapp.commons.AppUtils;

/**
 * Created by Administrator、 on 2017/3/21.
 */

@SuppressWarnings({"SpellCheckingInspection", "DefaultFileTemplate"})
public class HttpURL {
    private static final String HTTP = "http://";

    public static String Url(Context context) {
        String server = "";
        String URL = "";
        if (!"".equals(AppUtils.getServerAdress(context)) && AppUtils.getServerAdress(context) != null) {
            server = AppUtils.getServerAdress(context);
        } else {
            //server = "192.168.1.117:8088";
            server = "60.216.2.109:8081";
            AppUtils.setServerAdress(context, server);
        }
        URL = HTTP + server ;
        return URL;

    }

    /**
     *
     */
    //设备
    public static final String Logins = "/app/login/appLogin";
    public static final String DEVICELIST = "/app/currency/getDeviceList";//获取设备列表
    public static final String DEVICETAB_ORDER = "/app/myInfo/setDeviceOrder";//设置温控器、插座、开关等在导航栏的排序
    public static final String DEVICEDETAILS = "/app/currency/getDeviceDetails";//获取设备详情
    public static final String OPERATIONDEVICE = "/app/currency/operationDevice";//操作设备详情
    public static final String OPERATIONLISTDEVICE = "/app/currency/operationListDevice";//操作设备列表
    public static final String TEMP_SETLIMIT = "/app/currency/setLimit";//修改温限
    public static final String TIME_LIST = "/app/currency/getTimerList";//获取定时器列表
    public static final String SET_TIME = "/app/currency/setTimer";//更新或者添加定时器


    //“我的”
    public static final String UNREADMES = "/app/myInfo/getUnreadMes";//获取未读消息数量
    public static final String UPDATEPASSWORD = "/app/myInfo/updatePassword";//修改密码
    public static final String MESSAGELIST = "/app/myInfo/getMessage";//获取消息列表
    public static final String HELPMESSAGE = "/app/myInfo/getHelpMessage";//获取帮助信息
    public static final String ABOUTMESSAGE = "/myInfo/getAboutMessage";//获取 关于 信息
    public static final String TICKINGMESSAGE = "/app/myInfo/setTicklingMessage";//反馈信息

    //场景
    public static final String SCENELIST = "/app/scene/getByUserId";//获取场景列表
    public static final String CLICKTOADDSCENE = "/app/scene/addScene";//点击“添加场景”
    public static final String DELTESCENE = "/app/scene/delScene";//删除场景
    public static final String SCENEDEVICELIST= "/app/scene/getDeviceByScene";//获取场景详情中设备列表
    public static final String  SCENEALLDEVICE= "/app/scene/getUserAllDevice";//获取场景离所有设备列表
    public static final String  SCENEREMOVEDEVICE= "/app/scene/removeDevice";//在场景中移除已经添加的设备
    public static final String  SCENEINSERTDEVICE= "/app/scene/insertDevice";//在场景中添加的设备
    public static final String SAVESCENE= "/app/scene/saveScene";//保存修改后的场景详情
    public static final String CONTROLSCENE= "/app/scene/controlScene";//场景群控

    //设备电量
    public static final String DAIILELECTRIC = "/app/web/html/batteryLeft.html";//每日用电
    public static final String COUNTELECTRIC = "/app/web/html/batteryLeftMonth.html";//用电统计
    public static final String DETAILELECTRIC = "/app/web/html/batteryLeftDevice.html";//用电明细

    //节能监测
    public static final String ENERGYCONSUME = "/app/web/html/energyConsume.html";//能耗概况
    public static final String  POINTERDATA= "/app/web/html/energyData.html";//实时数据
    public static final String ENERGYFORMS = "/app/web/html/energyItemAnalyze.html";//能耗报表
    public static final String ENERGRANALYSE = "/app/web/html/energyItemComparison.html";//对比分析




    //分享连接
    public static final String SHAREAPP = "/app/web/html/appDechnic.html";
}
