package com.luo.a10.bean;

import java.util.List;

public class InnerItem {
    private String name;
    private List<String> pics;

    public InnerItem() {
    }

    public InnerItem(String name, List<String> pics) {
        this.name = name;
        this.pics = pics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
