package com.example.sankarmanoj.pushnotif;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class MyGCMReceiver extends WakefulBroadcastReceiver {
    public MyGCMReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

            ComponentName comp = new ComponentName(context.getPackageName(),
                   MyGCMListenerService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
    };
}