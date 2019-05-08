package com.luo.a10.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 新建分类传递数据的bean类
 */
public class AddFenleiInfo implements Serializable {
    private List<String> addDocs;
    private List<Boolean> isVideos; //保存是否是视频的标志，以便在适配器中显示

    public AddFenleiInfo() {
    }

    public AddFenleiInfo(List<String> addDocs) {
        this.addDocs = addDocs;
    }

    public List<String> getAddDocs() {
        return addDocs;
    }

    public void setAddDocs(List<String> addDocs) {
        this.addDocs = addDocs;
    }

    public List<Boolean> getIsVideos() {
        return isVideos;
    }

    public void setIsVideos(List<Boolean> isVideos) {
        this.isVideos = isVideos;
    }
}
