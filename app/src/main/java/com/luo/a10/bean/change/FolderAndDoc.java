package com.luo.a10.bean.change;

import java.io.Serializable;

public class FolderAndDoc implements Serializable {
    private int id;//id
    private int pId;//父id
    private int userId;//用户id
    private String name;//文件夹名称
    private String time = "";//时间；包含有多少项
    private String link = "";//下载地址
    private String cover = "";//如果是视频就有封面地址
    private long size = 0;//大小 如果是文件夹包含有多少项
    private String tag = "";//标签
    private int category = -1;//文件类型  文件夹 ：-1   文档：1    图片：2    音频：3    视频：4   其他：5

    public FolderAndDoc() {
    }

    public FolderAndDoc(int id, int pId, int userId, String name, String time, String link, String cover, long size, String tag, int category) {
        this.id = id;
        this.pId = pId;
        this.userId = userId;
        this.name = name;
        this.time = time;
        this.link = link;
        this.cover = cover;
        this.size = size;
        this.tag = tag;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
