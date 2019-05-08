package com.luo.a10.bean;

public class PicInfo {
    //图片的bean类 完全是为了方便json解析建立的
    private int id;
    private String name;
    private String url;
    private String catalogId;
    private String size;
    private String time;
    private String tag;

    public PicInfo() {
    }

    public PicInfo(int id, String name, String url, String catalogId, String size, String time, String tag) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.catalogId = catalogId;
        this.size = size;
        this.time = time;
        this.tag = tag;
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

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
