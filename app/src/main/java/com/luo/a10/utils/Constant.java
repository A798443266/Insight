package com.luo.a10.utils;

import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;

import java.util.ArrayList;
import java.util.List;

public class Constant {


    public static String BASE = "http://192.168.191.1:8080/A10/img2/";
    public static String BASE1 = "http://192.168.191.1:8080/A10/fenlei_img/";
    public static final String BASEURL = "http://192.168.0.133:8080/";
//    public static final String BASEURL = "http://pxqsce.natappfree.cc/";

    public static final String SP_NAME = "sp_a10";//sp名称
    public static final String ISLOGIN = "islogin";//是否已经登录
    public static final String USERNAME = "username";//用户名
    public static final String USERID = "userid";
    public static final String save_myfolders = "save_myfolders";//缓存文件信息json数据
    public static final String save_guidang1 = "save_guidang1";
    public static final String save_guidang2 = "save_guidang2";


//    接口----------------------------------------------------------------

    public static final String LOGIN = BASEURL + "validation";//登录接口
    public static final String UPLOAD = BASEURL + "upload";//上传接口
    public static final String SEARCH = BASEURL + "search";//搜索接口
    public static final String GETFOLDER_FILE = Constant.BASEURL + "catalogcontent";//获取文件夹和文件
    public static final String get_defined = Constant.BASEURL + "getdefined";//自定义归档
    public static final String get_insight = Constant.BASEURL + "getinsight";//智能归档
    public static final String Createdefined = Constant.BASEURL + "createdefined";//添加归档
    public static final String Fenelei = Constant.BASEURL + "filefiling/";//智能归档+id
    public static final String Sortimage = Constant.BASEURL + "sortimage";//时光轴
    public static final String Collections = Constant.BASEURL + "getcollections";//我的收藏
    public static final String Guanzhu = Constant.BASEURL + "getattentions";//我的关注
    public static final String Videoinfo = Constant.BASEURL + "videoinfo";//视频
    public static final String Audioinfo = Constant.BASEURL + "audioinfo";//音频
    public static final String Otherinfo = Constant.BASEURL + "otherinfo";//其他
    public static final String Docinfo = Constant.BASEURL + "docuinfo";//文档
    public static final String NewFolder = Constant.BASEURL + "createcatalog";//新建文件夹




    public static String[] imgs = {
            BASE + "img1.jpg",
            BASE + "img2.jpg",
            BASE + "img3.jpg",
            BASE + "img4.jpg",
            BASE + "img5.jpg",
            BASE + "img6.jpg",
            BASE + "img7.jpg",
            BASE + "img8.jpg",
            BASE + "img9.jpg",
            BASE + "img10.jpg",
            BASE + "img11.jpg",
            BASE + "img12.jpg",
            BASE + "img13.jpg",
            BASE + "img14.jpg",
            BASE + "img15.jpg",
            BASE + "img16.jpg",
            BASE + "img17.jpg",
            BASE + "img18.jpg"
    };



}
