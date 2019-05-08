package com.luo.a10.eventBusObject;

/**
 * 通知全选的事件
 */

public class SelectAllEvent {
    private boolean isSelectAll;
    public SelectAllEvent(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }

    public boolean isSelectAll() {
        return isSelectAll;
    }

    public void setSelectAll(boolean selectAll) {
        isSelectAll = selectAll;
    }
}
