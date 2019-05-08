package com.luo.a10.eventBusObject;

import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;

import java.util.List;

/**
 * 通知打开底部菜单的通知
 */

public class OpenMenuEvent {

    private FolderAndDoc doc;
    public OpenMenuEvent() {
    }

    public OpenMenuEvent(FolderAndDoc doc) {
        this.doc = doc;
    }

    public FolderAndDoc getDoc() {
        return doc;
    }

    public void setDocs(FolderAndDoc doc) {
        this.doc = doc;
    }
}
