package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/15.
 */

public class DevicesDetailsTime implements Serializable{
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

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    public DevicesDetailsTime(String registerAddress, String isOn,
                              String temperature, String pattern, String resourceName,
                              String resourceFlag, String permission, String registerNumber,
                              String deviceCode, String speed, String deviceStation, String ekName, String deviceFirm, String tMin, String tMax) {
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
        this.ekName = ekName;
        this.deviceFirm = deviceFirm;
        this.tMin = tMin;
        this.tMax = tMax;
    }

    public DevicesDetailsTime() {
        super();
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
}
