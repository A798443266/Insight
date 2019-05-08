package com.luo.a10.bean;

import java.util.List;

/**
 * 按 日或者按月分类的bean类
 */
public class DayPhotoItem {
    private String time; //日期
    private List<String> pics;//照片的集合

    public DayPhotoItem() {
    }

    public DayPhotoItem(String time, List<String> pics) {
        this.time = time;
        this.pics = pics;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
