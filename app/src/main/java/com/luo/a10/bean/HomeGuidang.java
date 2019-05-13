package com.luo.a10.bean;

//首页归档信息类
public class HomeGuidang {
    private String name;
    private String pic;
    private int num;

    public HomeGuidang() {
    }

    public HomeGuidang(String name, String pic, int num) {
        this.name = name;
        this.pic = pic;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
