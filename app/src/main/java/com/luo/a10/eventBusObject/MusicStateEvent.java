package com.luo.a10.eventBusObject;

//musicActivity发送给MainActivity音乐状态的通知
public class MusicStateEvent {
    private int type;  //0:正在播放  1：已暂停

    public MusicStateEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
