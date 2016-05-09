package com.vlife.mymemo.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.mainactivity.EditActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private Notepad returnAlarm;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("my","receive");
        if("com.MyBroadcast.alarm".equals(intent.getAction())){
            this.returnAlarm=(Notepad) intent.getSerializableExtra("Alarm");
            Log.d("my","receiver"+returnAlarm.getId());
            Intent alarmIntent=new Intent(context, EditActivity.class);
            alarmIntent.putExtra("returnAlarm",returnAlarm);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            String message=returnAlarm.getContent();
            if(message.length()>30){
                message=message.substring(0,30)+"...";
            }
            builder.setContentTitle(context.getString(R.string.app_name)).setContentText(message).
                    setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL).
                    setContentIntent(pendingIntent).setAutoCancel(true);
            manager.notify(1, builder.build());

            //alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(alarmIntent);
            }
    }
}
