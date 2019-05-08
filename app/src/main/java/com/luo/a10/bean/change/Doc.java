package com.luo.a10.bean.change;

public class Doc {
    private int id;
    private String name;
    private String url;
    private int userId;
    private int catalogId;
    private int category;
    private long size;
    private String uploadTime;
    private String tag = "";
    private String info = "";

    public Doc() {
    }

    public Doc(int id, String name, String url, int userId, int catalogId, int category, long size, String uploadTime, String tag, String info) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.userId = userId;
        this.catalogId = catalogId;
        this.category = category;
        this.size = size;
        this.uploadTime = uploadTime;
        this.tag = tag;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
