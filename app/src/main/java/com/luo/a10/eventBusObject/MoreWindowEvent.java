package com.luo.a10.eventBusObject;

public class MoreWindowEvent {
    private int type; //1:智能扫描  2：新建文件夹

    public MoreWindowEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
