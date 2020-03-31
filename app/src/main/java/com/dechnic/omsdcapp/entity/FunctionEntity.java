package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/25.
 * 功能码
 */

public class FunctionEntity implements Serializable{

    /**
     * registerAddress : 0081
     * remark : 春泉温控器
     * resourceFlag : pattern_cold
     * resourceName : 运行模式-制冷
     * permission : 1
     * registerNumber : 1
     */

    private String registerAddress;
    private String remark;
    private String resourceFlag;
    private String resourceName;
    private String permission;
    private String registerNumber;

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResourceFlag() {
        return resourceFlag;
    }

    public void setResourceFlag(String resourceFlag) {
        this.resourceFlag = resourceFlag;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
}
