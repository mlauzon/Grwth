package com.example.grwth.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DatabaseBroadcast extends BroadcastReceiver {
    final private String TAG = "BroadcastReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, com.example.grwth.data.DatabaseService.class);
        Log.d(TAG, "Starting DatabaseService");
        context.startService(serviceIntent);
    }
}
