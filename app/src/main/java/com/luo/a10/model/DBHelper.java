package com.luo.a10.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //创建数据库
    public DBHelper(@Nullable Context context) {
        super(context, "a10.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户搜索记录的表
        //插入的时候默认值为当前时间
        db.execSQL("create table search_record (id varchar," +
                "content varchar,createtime timestamp NOT NULL DEFAULT  (datetime('now','localtime')));");

        //录音表
        db.execSQL("create table yuyin (name varchar,path varchar,tag varchar,time INTEGER,riqi INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
