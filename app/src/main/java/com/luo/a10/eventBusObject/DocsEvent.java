package com.luo.a10.eventBusObject;

import com.luo.a10.bean.change.FolderAndDoc;

import java.util.List;

//从fragment发送文件到mainActivity中的消息，这样MainActivity弹出的菜单就可以操作这些文件了了
public class DocsEvent {
    private List<FolderAndDoc> docs;

    public DocsEvent(List<FolderAndDoc> docs) {
        this.docs = docs;
    }

    public List<FolderAndDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<FolderAndDoc> docs) {
        this.docs = docs;
    }
}
