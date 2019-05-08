package com.luo.a10.bean;

import com.luo.a10.bean.change.FolderAndDoc;

public class ShareInfo {
    private int look;
    private int download;
    private FolderAndDoc folderAndDoc;

    public ShareInfo() {
    }

    public ShareInfo(int look, int download, FolderAndDoc folderAndDoc) {
        this.look = look;
        this.download = download;
        this.folderAndDoc = folderAndDoc;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public FolderAndDoc getFolderAndDoc() {
        return folderAndDoc;
    }

    public void setFolderAndDoc(FolderAndDoc folderAndDoc) {
        this.folderAndDoc = folderAndDoc;
    }
}
