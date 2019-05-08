package com.luo.a10.bean;

import java.io.Serializable;

/**
 * 归档
 */
public class Guidang implements Serializable {
    private int id;
    private int userId;
    private String name;
    private boolean insight;

    public Guidang() {
    }

    public Guidang(int id, int userId, String name, boolean insight) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.insight = insight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInsight() {
        return insight;
    }

    public void setInsight(boolean insight) {
        this.insight = insight;
    }
}
