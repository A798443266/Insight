package com.luo.a10.bean.change;

//文件夹
public class Folder1 {
    private int id;
    private int userId;
    private String name;
    private int pid;

    public Folder1() {
    }

    public Folder1(int id, int userId, String name, int pid) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.pid = pid;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
