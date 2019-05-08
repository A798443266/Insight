package com.luo.a10.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.RecordInfo;
import com.luo.a10.yuyinrecord.RecordingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户搜索记录表操作
 */

public class DbDao {
    private final DBHelper mHelper;

    public DbDao(Context context) {
        mHelper = new DBHelper(context);//创建数据库
    }

    //添加搜索记录到数据库
    public void addRecord(String id, String content) {//手机号、类型（文档记录还是专家记录）、内容
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.execSQL("insert into search_record(id,content) values (?,?)", new Object[]{id, content});
        db.close();
    }


    public List<RecordInfo> queryById(String id) {
        List<RecordInfo> records = new ArrayList<>();
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from search_record where id =? order by createtime DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        while (cursor.moveToNext()) {
            RecordInfo record = new RecordInfo();
            record.setContent(cursor.getString(cursor.getColumnIndex("content")));
            record.setTime(cursor.getString(cursor.getColumnIndex("createtime")));
            records.add(record);
        }

        // 关闭资源
        cursor.close();
        db.close();
        return records;
    }

    public void deleteOne(String id, String content) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "delete from search_record where id =? and content =?";
        db.execSQL(sql, new String[]{id, content});
        db.close();
    }


    public void deleteAll(String id) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "delete from search_record where id =?";
        db.execSQL(sql, new String[]{id});
        db.close();
    }


    //以下是录音操作的dao
    public void insertLuyin(String name,String path,String tag,long time,long riqi) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.execSQL("insert into yuyin(name,path,tag,time,riqi) values (?,?,?,?,?)", new Object[]{name, path,tag,time,riqi});
        Log.e("TAG", "插入成功");
        db.close();
    }

    public List<RecordingItem> queryLuyins(){
        List<RecordingItem> records = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from yuyin order by riqi desc";
        Cursor cursor = db.rawQuery(sql,null);

        while (cursor.moveToNext()) {
            RecordingItem record = new RecordingItem();
            record.setFilePath(cursor.getString(cursor.getColumnIndex("path")));
            record.setName(cursor.getString(cursor.getColumnIndex("name")));
            record.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            record.setRiqi(cursor.getLong(cursor.getColumnIndex("riqi")));
            records.add(record);
        }

        Log.e("TAG", records.size()+"");
        // 关闭资源
        cursor.close();
        db.close();
        return records;
    }

    //删除所有的语音记录
    public void delAllYuyin(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "delete from yuyin";
        db.execSQL(sql);
        Log.e("TAG", "删除所有语音速记成功");
        db.close();
    }


    public void updateYuyin(String name,int id){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "update yuyin set name=? where id=?";
        db.execSQL(sql,new String[]{name,id+""});
        Log.e("TAG", "修改语音速记成功");
        db.close();
    }


}
