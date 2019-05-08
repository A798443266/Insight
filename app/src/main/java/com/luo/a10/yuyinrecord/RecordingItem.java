package com.luo.a10.yuyinrecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.luo.a10.utils.SpUtils;

import java.io.Serializable;

/**
 * 录音文件的实体类
 *
 */

public class RecordingItem implements Serializable {
    private String mName;
    private String mFilePath;
    private int mId;
    private long mRiqi;
    private long mTime;

    public RecordingItem() {
    }

    public RecordingItem(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mRiqi = in.readLong();
        mTime = in.readLong();
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public long getRiqi() {
        return mRiqi;
    }

    public void setRiqi(long mRiqi) {
        mRiqi = mRiqi;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }


}
