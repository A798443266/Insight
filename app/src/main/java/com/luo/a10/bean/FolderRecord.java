package com.luo.a10.bean;

import com.luo.a10.bean.change.FolderAndDoc;

import java.io.Serializable;
import java.util.List;

/**
 * 当前打开的文件夹及其里面的包含的文件信息
 */
public class FolderRecord implements Serializable {

    private FolderAndDoc folderAndDoc;//此时正在打开的文件夹
    private List<FolderAndDoc> docs; //文件夹里面的文件列表

    public FolderRecord() {
    }

    public FolderRecord(FolderAndDoc folderAndDoc,List<FolderAndDoc> docs) {
        this.folderAndDoc = folderAndDoc;
        this.docs = docs;
    }

    public FolderAndDoc getFolderAndDoc() {
        return folderAndDoc;
    }

    public void setFolderAndDoc(FolderAndDoc folderAndDoc) {
        this.folderAndDoc = folderAndDoc;
    }

    public List<FolderAndDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<FolderAndDoc> docs) {
        this.docs = docs;
    }
}
