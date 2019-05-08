package com.luo.a10.bean;

import java.util.List;

/**
 * 智能分类的item的bean类
 */
public class ClassificationItem {
    private String itemName;//大分类的名称
    private String[] typenames;//小的分类的名称
    private String[] pics; //每个小分类对应的图片集合

    public ClassificationItem() {
    }

    public ClassificationItem(String itemName, String[] typenames, String[] pics) {
        this.itemName = itemName;
        this.typenames = typenames;
        this.pics = pics;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String[] getTypenames() {
        return typenames;
    }

    public void setTypenames(String[] typenames) {
        this.typenames = typenames;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }
}


