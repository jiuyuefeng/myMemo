package com.vlife.mymemo.adapter;

import java.io.Serializable;

/**
 * 封装基本数据类
 * Created by Administrator on 2016/4/27 0027.
 */
public class Notepad implements Serializable{

    public String content;
    public String date;
    public String id;
    public String title;
    public Integer background;
    public Integer alarm;

    public String getContent() {
        return this.content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getId() {return this.id;}

    public Integer getBackground() {return this.background;}

    public Integer getAlarm() {return this.alarm;}

    public void setContent(String paramString) {
        this.content = paramString;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public void setDate(String paramString) {this.date = paramString;}

    public void setId(String paramString) {this.id = paramString;}

    public void setBackground(Integer integer) {this.background = integer;}

    public void setAlarm(Integer integer) {this.alarm = integer;}

}
