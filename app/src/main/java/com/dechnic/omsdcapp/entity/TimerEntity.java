package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class TimerEntity implements Serializable{
    private String timerId;
    private String isTimer;//定时 开启或关闭
    private Boolean isOpen;//开关的开启关闭

    private String deviceType;//1为开机，2为关机
    private String deviceStatus;//0为不开启，1为开启
    private String temperature;//温度
    private String id;//定时器的id
    private String deviceWeek;//146表示周一周四周六   0表示没有
    private String updatedOn;
    private String deviceTime;//定时的时间
    private String deviceId;//设备的id
    private String createdOn;
    private int order;//排序


    private String org_deviceWeek;//原始周期数据
    private String org_deviceTime;//原始时间数据

    private String hour;
    private String minute;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getOrg_deviceWeek() {
        return org_deviceWeek;
    }

    public void setOrg_deviceWeek(String org_deviceWeek) {
        this.org_deviceWeek = org_deviceWeek;
    }

    public String getOrg_deviceTime() {
        return org_deviceTime;
    }

    public void setOrg_deviceTime(String org_deviceTime) {
        this.org_deviceTime = org_deviceTime;
    }

    public TimerEntity() {
        super();
    }

    public TimerEntity(String timerId, String isTimer, Boolean isOpen, String deviceType,
                       String deviceStatus, String temperature, String id, String deviceWeek,
                       String updatedOn, String deviceTime, String deviceId, String createdOn, int order) {
        this.timerId = timerId;
        this.isTimer = isTimer;
        this.isOpen = isOpen;
        this.deviceType = deviceType;
        this.deviceStatus = deviceStatus;
        this.temperature = temperature;
        this.id = id;
        this.deviceWeek = deviceWeek;
        this.updatedOn = updatedOn;
        this.deviceTime = deviceTime;
        this.deviceId = deviceId;
        this.createdOn = createdOn;
        this.order = order;
    }

    public String getTimerId() {
        return timerId;
    }

    public void setTimerId(String timerId) {
        this.timerId = timerId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDeviceWeek() {
        return deviceWeek;
    }

    public void setDeviceWeek(String deviceWeek) {
        this.deviceWeek = deviceWeek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTimer() {
        return isTimer;
    }

    public void setTimer(String timer) {
        isTimer = timer;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }
}
