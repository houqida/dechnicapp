package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/22.
 */

public class ControlEntity implements Serializable{
    private String controlId;//设备id
    private String controlIv;//图标
    private String controlName;//名称
    private boolean isOnline;//是否在线
    private String temp;//温度
    private boolean isOpen;//是否打开
    private String roomName;//房间号
    private String deviceCode;//编号

    private int type;

    private String modle;//模式 即制冷制热  对于温控器有
    private String gear;//档位 对于温控器有

    private String ekName;//设备种类
    private String deviceFirm;//厂商信息

    private String isOn;

    private int switchNumber;//若设备为开关，该参数是开关的号码
    private String indoorTemperature;//室温
    private int panelDisable;// 面板禁用的显示 0 显示，1 不显示

    public int getPanelDisable() {
        return panelDisable;
    }

    public void setPanelDisable(int panelDisable) {
        this.panelDisable = panelDisable;
    }

    public String getIndoorTemperature() {
        return indoorTemperature;
    }

    public void setIndoorTemperature(String indoorTemperature) {
        this.indoorTemperature = indoorTemperature;
    }

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public ControlEntity(String controlId, String controlIv,
                         String controlName, boolean isOnline, String temp, boolean isOpen, String roomName) {

        this.controlId = controlId;
        this.controlIv = controlIv;
        this.controlName = controlName;
        this.isOnline = isOnline;
        this.temp = temp;
        this.isOpen = isOpen;
        this.roomName = roomName;
    }

    public ControlEntity() {
        super();
    }

    public String getEkName() {
        return ekName;
    }

    public void setEkName(String ekName) {
        this.ekName = ekName;
    }

    public String getDeviceFirm() {
        return deviceFirm;
    }

    public void setDeviceFirm(String deviceFirm) {
        this.deviceFirm = deviceFirm;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getModle() {
        return modle;
    }


    public void setModle(String modle) {
        this.modle = modle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlIv() {
        return controlIv;
    }

    public void setControlIv(String controlIv) {
        this.controlIv = controlIv;
    }

    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
