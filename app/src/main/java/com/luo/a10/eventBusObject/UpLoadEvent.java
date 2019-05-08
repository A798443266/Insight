package com.luo.a10.eventBusObject;

public class UpLoadEvent {
    private int progress;
    private String type;

    public UpLoadEvent() {
    }

    public UpLoadEvent(String type,int progress) {
        this.progress = progress;
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
