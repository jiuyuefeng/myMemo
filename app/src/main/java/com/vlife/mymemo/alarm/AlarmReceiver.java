package com.vlife.mymemo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.mainactivity.EditActivity;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class AlarmReceiver extends BroadcastReceiver{

    private Notepad returnAlarm=null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.returnAlarm=new Notepad();
        this.returnAlarm=(Notepad) intent.getSerializableExtra("Alarm");
        Log.d("my","11111111111receiver");
        Intent alarmIntent=new Intent(context, EditActivity.class);
        intent.putExtra("returnAlarm",returnAlarm);
        context.startActivity(alarmIntent);
    }
}
