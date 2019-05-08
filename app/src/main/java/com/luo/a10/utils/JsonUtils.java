package com.luo.a10.utils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luo.a10.bean.Guidang;
import com.luo.a10.bean.PicInfo;
import com.luo.a10.bean.PicTimeAxis;
import com.luo.a10.bean.change.Doc;
import com.luo.a10.bean.change.Folder1;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.bean.Fileinfo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static List<FolderAndDoc> parseFolderAndDoc(String json) {
        List<FolderAndDoc> folderAndDocs = new ArrayList<>();
        List<Folder1> fs = new ArrayList<>();
        List<Doc> ds = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(json);
        String folders = jsonObject.getString("catalogs");
        String docs = jsonObject.getString("files");
        fs = JSON.parseArray(folders, Folder1.class);//文件夹列表
        ds = JSON.parseArray(docs, Doc.class);//文件列表

        if (fs != null && fs.size() > 0) {
            for (int i = 0; i < fs.size(); i++) {
                Folder1 f = fs.get(i);
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(f.getId());
                folderAndDoc.setpId(f.getPid());
                folderAndDoc.setUserId(f.getUserId());
                folderAndDoc.setName(f.getName());
                folderAndDoc.setCategory(-1);//文件夹
                folderAndDocs.add(folderAndDoc);
            }
        }

        if (ds != null && ds.size() > 0) {
            for (int i = 0; i < ds.size(); i++) {
                Doc d = ds.get(i);
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(d.getId());
                folderAndDoc.setpId(d.getCatalogId());
                folderAndDoc.setUserId(d.getUserId());
                folderAndDoc.setName(d.getName());
                folderAndDoc.setLink(Constant.BASEURL + "testpreview/" + d.getUrl());
                folderAndDoc.setSize(d.getSize());
                folderAndDoc.setTag(d.getTag());
                folderAndDoc.setTime(d.getUploadTime());
                folderAndDoc.setCategory(d.getCategory());//文件类型
                if (d.getCategory() == 4) {//如果是视频去取封面地址
                    JSONObject jsonObject1 = JSON.parseObject(d.getInfo());
                    String cover = jsonObject1.getString("thumbUrl");
                    folderAndDoc.setCover(Constant.BASEURL + "testpreview/" + cover);
                }

                folderAndDocs.add(folderAndDoc);
            }
        }

        return folderAndDocs;

    }

    //解析搜索结果
    public static List<FolderAndDoc> parseSearch(String result) {
        List<FolderAndDoc> folderAndDocs = new ArrayList<>();
        List<Doc> imgs = new ArrayList<>();
        List<Doc> docs = new ArrayList<>();
        List<Doc> videos = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(result);

        String imag = jsonObject.getString("IMAGE");
        String doc = jsonObject.getString("DOCUMENT");
        String video = jsonObject.getString("VIDEO");

        imgs = JSON.parseArray(imag, Doc.class);
        docs = JSON.parseArray(doc, Doc.class);
        videos = JSON.parseArray(video, Doc.class);

        if (imgs != null && imgs.size() > 0) {
            for (int i = 0; i < imgs.size(); i++) {
                Doc d = imgs.get(i);
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(d.getId());
                folderAndDoc.setpId(d.getCatalogId());
                folderAndDoc.setUserId(d.getUserId());
                folderAndDoc.setName(d.getName());
                folderAndDoc.setLink(Constant.BASEURL + "testpreview/" + d.getUrl());
                folderAndDoc.setSize(d.getSize());
                folderAndDoc.setTag(d.getTag());
                folderAndDoc.setTime(d.getUploadTime());
                folderAndDoc.setCategory(d.getCategory());//文件类型
                folderAndDocs.add(folderAndDoc);
            }
        }

        if (docs != null && docs.size() > 0) {
            for (int i = 0; i < docs.size(); i++) {
                Doc d = docs.get(i);
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(d.getId());
                folderAndDoc.setpId(d.getCatalogId());
                folderAndDoc.setUserId(d.getUserId());
                folderAndDoc.setName(d.getName());
                folderAndDoc.setLink(Constant.BASEURL + "testpreview/" + d.getUrl());
                folderAndDoc.setSize(d.getSize());
                folderAndDoc.setTag(d.getTag());
                folderAndDoc.setTime(d.getUploadTime());
                folderAndDoc.setCategory(d.getCategory());//文件类型
                folderAndDocs.add(folderAndDoc);
            }
        }

        if (videos != null && videos.size() > 0) {
            for (int i = 0; i < videos.size(); i++) {
                Doc d = videos.get(i);
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(d.getId());
                folderAndDoc.setpId(d.getCatalogId());
                folderAndDoc.setUserId(d.getUserId());
                folderAndDoc.setName(d.getName());
                folderAndDoc.setLink(Constant.BASEURL + "testpreview/" + d.getUrl());
                folderAndDoc.setSize(d.getSize());
                folderAndDoc.setTag(d.getTag());
                folderAndDoc.setTime(d.getUploadTime());
                folderAndDoc.setCategory(d.getCategory());//文件类型
                JSONObject jsonObject1 = JSON.parseObject(d.getInfo());//取封面地址
                String cover = jsonObject1.getString("thumbUrl");
                folderAndDoc.setCover(Constant.BASEURL + "testpreview/" + cover);
                folderAndDocs.add(folderAndDoc);

            }
        }

        Log.e("TAG", folderAndDocs.size() + "");
        return folderAndDocs;
    }

    //解析归档
    public static List<Guidang> parseFenlei(String response) {
        return JSON.parseArray(response, Guidang.class);
    }

    //解析图片时光轴
    public static List<PicTimeAxis> parseShiGZ(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        List<PicTimeAxis> datas = new ArrayList<>();

        //无序遍历
//        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
//            Log.e("TAG", entry.getKey());
//            PicTimeAxis picTimeAxis = new PicTimeAxis();
//            picTimeAxis.setMonth(entry.getKey());
//            List<PicInfo> pics = JSON.parseArray((String) entry.getValue(), PicInfo.class);
//            picTimeAxis.setPics(pics);
//            datas.add(picTimeAxis);
//        }


        LinkedHashMap<String, String> jsonMap = JSON.parseObject(response, new TypeReference<LinkedHashMap<String, String>>() {
        });
        //顺序遍历月份
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            Log.e("TAG", entry.getKey());
            Log.e("TAG", entry.getValue());
            PicTimeAxis picTimeAxis = new PicTimeAxis();
            picTimeAxis.setMonth(entry.getKey());
            List<PicInfo> pics = JSON.parseArray(entry.getValue(), PicInfo.class);
            if (pics != null && pics.size() > 0)
                for (PicInfo pic : pics) {
                    pic.setUrl(Constant.BASEURL + "testpreview/" + pic.getUrl());
                }
            picTimeAxis.setPics(pics);
            datas.add(picTimeAxis);
        }
        Log.e("TAG", datas.size() + "");
        return datas;
    }

    public static List<FolderAndDoc> parseCollection(String response) {
        List<Fileinfo> files = JSON.parseArray(response, Fileinfo.class);
        List<FolderAndDoc> folderAndDocs = new ArrayList<>();

        if (files != null && files.size() > 0) {
            for (Fileinfo file : files) {
                FolderAndDoc folderAndDoc = new FolderAndDoc();
                folderAndDoc.setId(file.getId());
                folderAndDoc.setName(file.getName());
                folderAndDoc.setSize(file.getSize());
                folderAndDoc.setTag(file.getTag());
                folderAndDoc.setTime(file.getUploadTime() + "");
                folderAndDoc.setpId(file.getCatalogId());
                folderAndDoc.setLink(Constant.BASEURL + "testpreview/" + file.getUrl());
                folderAndDoc.setCategory(file.getCategory());

                if (file.getCategory() == 4) {//如果是视频取封面地址
                    JSONObject jsonObject = JSON.parseObject(file.getInfo());
                    String cover = jsonObject.getString("thumbUrl");
                    folderAndDoc.setCover(Constant.BASEURL + "testpreview/" + cover);
                }

                folderAndDocs.add(folderAndDoc);
            }
        }
        return folderAndDocs;
    }
}
