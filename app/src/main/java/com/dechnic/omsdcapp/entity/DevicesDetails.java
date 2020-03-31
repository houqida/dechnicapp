package com.dechnic.omsdcapp.entity;

/**
 * Created by Administrator on 2017/4/10.
 */

public class DevicesDetails {

    /**
     * registerAddress : 0081
     * isOn : 0
     * temperature : 26
     * pattern : 制冷
     * resourceName : 运行模式-制热
     * resourceFlag : pattern_hot
     * permission : 4
     * registerNumber : 1
     * deviceCode : 27
     * speed : 高速
     * deviceStation : 192.168.1.234
     * ekName:设备种类
     * deviceFirm厂商名称
     * tMin 最低限温
     * tMax 最高限温
     * switchNumber 开关数  只针对开关
     */

    private String registerAddress;
    private String isOn;
    private String temperature;
    private String pattern;
    private String resourceName;
    private String resourceFlag;
    private String permission;
    private String registerNumber;
    private String deviceCode;
    private String speed;
    private String deviceStation;
    private String ekName;
    private String deviceFirm;
    private String tMin;
    private String tMax;
    private int switchNumber;
    private String indoorTemperature;

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

    public DevicesDetails(String registerAddress, String isOn, String temperature, String pattern,
                          String resourceName, String resourceFlag, String permission,
                          String registerNumber, String deviceCode, String speed, String deviceStation,
                          String ekNamem, String deviceFirm, String tMax, String tMin) {
        this.registerAddress = registerAddress;
        this.isOn = isOn;
        this.temperature = temperature;
        this.pattern = pattern;
        this.resourceName = resourceName;
        this.resourceFlag = resourceFlag;
        this.permission = permission;
        this.registerNumber = registerNumber;
        this.deviceCode = deviceCode;
        this.speed = speed;
        this.deviceStation = deviceStation;
        this.ekName = ekNamem;
        this.deviceFirm = deviceFirm;
        this.tMax = tMax;
        this.tMin = tMin;
    }

    public DevicesDetails() {
        super();
    }

    public String gettMin() {
        return tMin;
    }

    public void settMin(String tMin) {
        this.tMin = tMin;
    }

    public String gettMax() {
        return tMax;
    }

    public void settMax(String tMax) {
        this.tMax = tMax;
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

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceFlag() {
        return resourceFlag;
    }

    public void setResourceFlag(String resourceFlag) {
        this.resourceFlag = resourceFlag;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeviceStation() {
        return deviceStation;
    }

    public void setDeviceStation(String deviceStation) {
        this.deviceStation = deviceStation;
    }
}
