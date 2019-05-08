package com.luo.a10.eventBusObject;

/**
 * 服务中播放音乐与activity传递消息的通知类
 */
public class MusicPlayEvent {
    private int type;  //0开始播放 ， 1播放错误  2播放完成

    public MusicPlayEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
