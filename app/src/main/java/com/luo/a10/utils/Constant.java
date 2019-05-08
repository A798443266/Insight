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


    public static List<DocAndFloder> getDocs() {
        List<DocAndFloder> docs = new ArrayList<>();

        DocAndFloder doc1 = new DocAndFloder();
        doc1.setDocName("浅谈电子商务的服务与信誉.doc");
        doc1.setIsFolder(1);
        doc1.setSize("23.7K");
        doc1.setId(1);
        doc1.setTime("1555336478558");
        doc1.setLink("");
        doc1.setContent(0);
        doc1.setParentId(1);
        docs.add(doc1);

        DocAndFloder doc2 = new DocAndFloder();
        doc2.setDocName("移动web开发实战.pdf");
        doc2.setIsFolder(1);
        doc2.setSize("23.7K");
        doc2.setId(1);
        doc2.setTime("1555336478558");
        doc2.setLink("");
        doc2.setContent(0);
        doc2.setParentId(1);
        docs.add(doc2);

        DocAndFloder doc3 = new DocAndFloder();
        doc3.setDocName("软件测试信息表.xls");
        doc3.setIsFolder(1);
        doc3.setSize("23.7K");
        doc3.setTime("1555336478558");
        doc3.setId(1);
        doc3.setLink("");
        doc3.setContent(0);
        doc3.setParentId(1);
        docs.add(doc3);

        DocAndFloder doc4 = new DocAndFloder();
        doc4.setDocName("20190410_623845.PNG");
        doc4.setIsFolder(1);
        doc4.setSize("1.23MB");
        doc4.setTime("2019-02-23 15:45");
        doc4.setId(1);
        doc4.setLink(BASE + "img5.jpg");
        doc4.setContent(0);
        doc4.setParentId(1);
        docs.add(doc4);

        DocAndFloder doc5 = new DocAndFloder();
        doc5.setDocName("悉尼歌剧院.JPG");
        doc5.setIsFolder(1);
        doc5.setSize("2.78MB");
        doc5.setTime("2019-02-23 15:46");
        doc5.setId(1);
        doc5.setLink(BASE + "img30.jpg");
        doc5.setContent(0);
        doc5.setParentId(1);
        docs.add(doc5);

        DocAndFloder doc6 = new DocAndFloder();
        doc6.setDocName("学期工作总结.pptx");
        doc6.setIsFolder(1);
        doc6.setTime("1555336478558");
        doc6.setSize("2.78MB");
        doc6.setId(1);
        doc6.setLink("");
        doc6.setContent(0);
        doc6.setParentId(1);
        docs.add(doc6);

        DocAndFloder doc7 = new DocAndFloder();
        doc7.setDocName("公司团建记录视频.mp4");
        doc7.setIsFolder(1);
        doc7.setTime("2019-03-06 21:20");
        doc7.setSize("135MB");
        doc7.setId(1);
        doc7.setLink(BASE1 + "ziran_videos/ziran_video3.jpg");
        doc7.setContent(0);
        doc7.setParentId(1);
        docs.add(doc7);

        DocAndFloder doc9 = new DocAndFloder();
        doc9.setDocName("一生有你.mp3");
        doc9.setIsFolder(1);
        doc9.setSize("3.4MB");
        doc9.setTime("2019-03-06 21:23");
        doc9.setId(1);
        doc9.setLink("");
        doc9.setContent(0);
        doc9.setParentId(1);
        docs.add(doc9);

        return docs;
    }


    public static List<FolderAndDoc> getHomeSeeFragmentDocs() {
        List<FolderAndDoc> docs = new ArrayList<>();

        FolderAndDoc doc4 = new FolderAndDoc();
        doc4.setName("20190410_623845.PNG");
        doc4.setCategory(2);
        doc4.setSize(1024 * 1024);
        doc4.setId(1);
        doc4.setLink(BASE + "img17.jpg");
        doc4.setTime("1555336478558");
        doc4.setpId(1);
        docs.add(doc4);

        FolderAndDoc doc1 = new FolderAndDoc();
        doc1.setName("浅谈电子商务的服务与信誉.doc");
        doc1.setCategory(1);
        doc1.setSize(1024 * 1024);
        doc1.setTime("1555336479342");
        doc1.setId(1);
        doc1.setLink("");
        doc1.setpId(1);
        docs.add(doc1);

        FolderAndDoc doc2 = new FolderAndDoc();
        doc2.setName("时间敏感数据流上的频繁项集挖掘算法.pdf");
        doc2.setCategory(1);
        doc2.setSize(1024 * 1024);
        doc2.setTime("1555336478558");
        doc2.setId(1);
        doc2.setLink("");
        doc2.setpId(1);
        docs.add(doc2);

        FolderAndDoc doc3 = new FolderAndDoc();
        doc3.setName("资料课程费用.xlsx");
        doc3.setCategory(1);
        doc3.setSize(1024 * 1024);
        doc3.setTime("1555336478558");
        doc3.setId(1);
        doc3.setLink("");
        doc3.setpId(1);
        docs.add(doc3);


        FolderAndDoc doc5 = new FolderAndDoc();
        doc5.setName("20190403_623345.JPG");
        doc5.setCategory(2);
        doc5.setSize(1024 * 1024);
        doc5.setTime("1555336478558");
        doc5.setId(1);
        doc5.setLink(BASE + "img27.jpg");
        doc5.setpId(1);
        docs.add(doc5);

        FolderAndDoc doc6 = new FolderAndDoc();
        doc6.setName("简约通用毕业论文答辩.pptx");
        doc6.setCategory(1);
        doc6.setSize(1024 * 1024);
        doc6.setTime("1555336478558");
        doc6.setId(1);
        doc6.setLink("");
        doc6.setpId(1);
        docs.add(doc6);

        FolderAndDoc doc7 = new FolderAndDoc();
        doc7.setName("草原实拍.mp4");
        doc7.setTime("1555336478558");
        doc7.setCategory(4);
        doc7.setSize(1024 * 1024);
        doc7.setId(1);
        doc7.setCover(BASE1 + "ziran_videos/ziran_video2.jpg");
        doc7.setpId(1);
        docs.add(doc7);

        FolderAndDoc doc9 = new FolderAndDoc();
        doc9.setName("一生有你.mp3");
        doc9.setCategory(3);
        doc9.setSize(1024 * 1024);
        doc9.setTime("1555336478558");
        doc9.setId(1);
        doc9.setLink("");
        doc9.setpId(1);
        docs.add(doc9);

        return docs;
    }


}
