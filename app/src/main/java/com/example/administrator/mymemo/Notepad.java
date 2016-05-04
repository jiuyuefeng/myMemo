package com.example.administrator.mymemo;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class Notepad {

    public String content;
    public String data;
    public String id;
    public String title;
    public Integer background;

    public String getContent() {
        return this.content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getdata() {
        return this.data;
    }

    public String getid() {
        return this.id;
    }

    public Integer getBackground() {return this.background;}

    public void setContent(String paramString) {
        this.content = paramString;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public void setdata(String paramString) {
        this.data = paramString;
    }

    public void setid(String paramString) {this.id = paramString;}

    public void setBackground(Integer integer) {this.background = integer;}

}
