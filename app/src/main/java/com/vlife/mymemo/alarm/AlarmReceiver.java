package com.vlife.mymemo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vlife.mymemo.mainactivity.EditActivity;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent=new Intent(context, EditActivity.class);
        context.startActivity(alarmIntent);
    }
}
