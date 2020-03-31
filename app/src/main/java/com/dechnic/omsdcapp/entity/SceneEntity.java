package com.dechnic.omsdcapp.entity;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SceneEntity {
    private String sceceId;
    private String sceneIv;
    private String sceneName;
    private String userId;

    public SceneEntity(String sceceId, String sceneIv, String sceneName, String userId) {
        this.sceceId = sceceId;
        this.sceneIv = sceneIv;
        this.sceneName = sceneName;
        this.userId = userId;
    }

    public SceneEntity() {
        super();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSceceId() {
        return sceceId;
    }

    public void setSceceId(String sceceId) {
        this.sceceId = sceceId;
    }

    public String getSceneIv() {
        return sceneIv;
    }

    public void setSceneIv(String sceneIv) {
        this.sceneIv = sceneIv;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}
