package com.luo.a10.bean;

public class Fileinfo {

    private int id;
    private String name;
    private String url;
    private int catalogId;
    private int category;
    private long size = 0;
    private long uploadTime;
    private String tag;
    private String info;

    public Fileinfo() {
    }

    public Fileinfo(int id, String name, String url, int catalogId, int category, long size, long uploadTime, String tag, String info) {
        this.id = id;
        this.name = name;
        this.url = url;
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

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
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
