package com.luo.a10.bean;

import java.io.Serializable;

public class DocAndFloder implements Serializable {
    private int id;
    private int parentId = 0;//父文件id
    private String docName = "名称.doc";//文件名
    private String userId;
    private int content = 0;//如果是文件夹表示包含的子文件个数
    private int isFolder = 0;//0 表示文件夹，1 表示文件
    private String size = "33.3K";//文件大小
    private String link = "";//预览地址
    private String time = "2019-01-02 13:34";//时间

    public DocAndFloder() {
    }

    public DocAndFloder(int id, int parentId, String docName, String userId, int content, int isFolder, String size, String link, String time) {
        this.id = id;
        this.parentId = parentId;
        this.docName = docName;
        this.userId = userId;
        this.content = content;
        this.isFolder = isFolder;
        this.size = size;
        this.link = link;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public int getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(int isFolder) {
        this.isFolder = isFolder;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
