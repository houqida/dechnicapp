package com.dechnic.omsdcapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/12.
 */

public class InfoMessage implements Serializable{

    /**
     * id : 1
     * updatedOn : 1491997695000
     * title : 啊啊啊啊
     * createdOn : 1491911293000
     * content : 内容
     */

    private String id;
    private String updatedOn;
    private String title;
    private String createdOn;
    private String content;

    public InfoMessage(String id, String updatedOn, String title, String createdOn, String content) {
        this.id = id;
        this.updatedOn = updatedOn;
        this.title = title;
        this.createdOn = createdOn;
        this.content = content;
    }

    public InfoMessage() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
