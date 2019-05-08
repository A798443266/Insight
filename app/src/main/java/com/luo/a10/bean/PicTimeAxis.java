package com.luo.a10.bean;

import java.util.List;

/**
 * 图片时光轴
 */
public class PicTimeAxis {

    private String month;
    private List<PicInfo> pics;
    public PicTimeAxis() {
    }

    public PicTimeAxis(String month, List<PicInfo> pics) {
        this.month = month;
        this.pics = pics;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PicInfo> getPics() {
        return pics;
    }

    public void setPics(List<PicInfo> pics) {
        this.pics = pics;
    }
}




