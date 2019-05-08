package com.luo.a10.eventBusObject;

/**
 * 时光轴的pop的通知
 */

public class PopEvent {
    private int type;
    public PopEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
