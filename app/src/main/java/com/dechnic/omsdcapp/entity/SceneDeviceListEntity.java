package com.dechnic.omsdcapp.entity;

/**
 * Created by Administrator on 2017/4/25.
 */

public class SceneDeviceListEntity {

    /**
     * deviceKind : null
     * pattern : null
     * permission : null
     * deviceCode : null
     * updatedOn : null
     * deviceId : 3
     * createdOn : null
     * speed : null
     * registerAddress : null
     * isOn : null
     * temperature : null
     * aceneId : 1
     * id : 7
     * registerNumber : null
     * deviceFirm : 春泉
     * deviceStation : null
     * deviceName; 设备名称
     * flag  状态  1，显示移除，2 显示添加
     */

    private String deviceKind;
    private String pattern;
    private String permission;
    private String deviceCode;
    private String updatedOn;
    private String deviceId;
    private String createdOn;
    private String speed;
    private String registerAddress;
    private String isOn;
    private String temperature;
    private String sceneId;
    private String id;
    private String registerNumber;
    private String deviceFirm;
    private String deviceStation;
    private String deviceName;
    private String flag;
    private int switchNumber;

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public SceneDeviceListEntity(String deviceKind, String pattern, String permission, String deviceCode,
                                 String updatedOn, String deviceId, String createdOn, String speed,
                                 String registerAddress, String isOn, String temperature, String aceneId,
                                 String id, String registerNumber, String deviceFirm, String deviceStation, String deviceName) {
        this.deviceKind = deviceKind;
        this.pattern = pattern;
        this.permission = permission;
        this.deviceCode = deviceCode;
        this.updatedOn = updatedOn;
        this.deviceId = deviceId;
        this.createdOn = createdOn;
        this.speed = speed;
        this.registerAddress = registerAddress;
        this.isOn = isOn;
        this.temperature = temperature;
        this.sceneId = aceneId;
        this.id = id;
        this.registerNumber = registerNumber;
        this.deviceFirm = deviceFirm;
        this.deviceStation = deviceStation;
        this.deviceName = deviceName;
    }


    public SceneDeviceListEntity() {
    }

    public String getDeviceKind() {
        return deviceKind;
    }

    public void setDeviceKind(String deviceKind) {
        this.deviceKind = deviceKind;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getAceneId() {
        return sceneId;
    }

    public void setAceneId(String aceneId) {
        this.sceneId = aceneId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDeviceFirm() {
        return deviceFirm;
    }

    public void setDeviceFirm(String deviceFirm) {
        this.deviceFirm = deviceFirm;
    }

    public String getDeviceStation() {
        return deviceStation;
    }

    public void setDeviceStation(String deviceStation) {
        this.deviceStation = deviceStation;
    }
}
