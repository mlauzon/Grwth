package com.example.grwth.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.grwth.data.DatabaseBroadcast;

public class AlarmHandler {
    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public void setAlarmManager() {
        Intent intent = new Intent(context, com.example.grwth.data.DatabaseBroadcast.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            // 24 Hours * 60 Minutes/hour * 60 Second/minute * 1000 ms / second
            long triggerAfter = 24 * 60 * 60 * 1000;
            long triggerEvery = 24 * 60 * 60 * 1000;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);
        }

    }

    public void cancelAlarmManager() {
        Intent intent = new Intent(context, DatabaseBroadcast.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null ) {
            manager.cancel(sender);
        }
    }
}
