package com.vlife.mymemo.date;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class Date {

    public String getDate() {
         Calendar c = Calendar.getInstance();
//         int year = c.get(Calendar.YEAR);
//         int month = c.get(Calendar.MONTH)+1;
//         int date = c.get(Calendar.DATE);
//         int hour = c.get(Calendar.HOUR_OF_DAY);
//         int minute = c.get(Calendar.MINUTE);
//         int second = c.get(Calendar.SECOND);
//         return year + "-" + month + "-" + date + "           " +hour + ":"
//         +minute + ":" + second;

        return(String.format("%tF",c)+"           "+String.format("%tT",c));
    }
}
