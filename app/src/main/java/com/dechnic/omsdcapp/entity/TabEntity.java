package com.dechnic.omsdcapp.entity;

/**
 * Created by Administrator on 2017/3/30.
 */

public class TabEntity {
    private String tab;
    private String id;
    private int sort;

    public TabEntity(String tab, String id,int sort) {
        this.tab = tab;
        this.id = id;
        this.sort = sort;
    }

    public TabEntity() {
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
