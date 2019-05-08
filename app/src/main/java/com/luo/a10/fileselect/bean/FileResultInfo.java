package com.luo.a10.fileselect.bean;

import java.util.List;


public class FileResultInfo implements Comparable<FileResultInfo> {
    private String name;
    private List<FileItem> fileItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<FileItem> fileItems) {
        this.fileItems = fileItems;
    }


    @Override
    public int compareTo(FileResultInfo o) {
        return o.getName().compareTo(name);
    }
}
