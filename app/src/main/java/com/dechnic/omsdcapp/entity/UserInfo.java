package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/21.
 * 用户信息类
 */

public class UserInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    private String userName;//用户名
    private String userPassWord;//密码
    private String accessToken;//token
    private String isLogin;//登陆情况 “Y”登陆，“N”未登陆
    private Boolean isRemberPw;//是否记住密码
    private Boolean isAutoLogin;//是否自动登陆

    private String device_control;
    private String device_switch;
    private String device_socket;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevice_control() {
        return device_control;
    }

    public void setDevice_control(String device_control) {
        this.device_control = device_control;
    }

    public String getDevice_switch() {
        return device_switch;
    }

    public void setDevice_switch(String device_switch) {
        this.device_switch = device_switch;
    }

    public String getDevice_socket() {
        return device_socket;
    }

    public void setDevice_socket(String device_socket) {
        this.device_socket = device_socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public Boolean getRemberPw() {
        return isRemberPw;
    }

    public void setRemberPw(Boolean remberPw) {
        isRemberPw = remberPw;
    }

    public Boolean getAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(Boolean autoLogin) {
        isAutoLogin = autoLogin;
    }
}
