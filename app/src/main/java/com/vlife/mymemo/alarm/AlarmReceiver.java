package com.vlife.mymemo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.mainactivity.EditActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private Notepad returnAlarm;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if("MyBroadcast".equals(intent.getAction())){
            this.returnAlarm=new Notepad();
            this.returnAlarm=(Notepad) intent.getSerializableExtra("Alarm");
            Log.d("my","11111111111receiver");
            Intent alarmIntent=new Intent(context, EditActivity.class);
            intent.putExtra("returnAlarm",returnAlarm);
            context.startActivity(alarmIntent);}
    }
}
