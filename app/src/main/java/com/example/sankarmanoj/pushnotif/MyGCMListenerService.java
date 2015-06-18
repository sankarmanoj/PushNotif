package com.example.sankarmanoj.pushnotif;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyGCMListenerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
   public static int NotID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public MyGCMListenerService() {
        super("MyGCMListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
   Bundle extras =intent.getExtras();
        GoogleCloudMessaging GCM = GoogleCloudMessaging.getInstance(this);
        String messageType = GCM.getMessageType(intent);
        Log.d("GCM",messageType);
        if(!extras.isEmpty())
        {
            sendNotification(extras.getString("message"));
        }
        MyGCMReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Log.d("NOTIF", "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(android.R.drawable.radiobutton_off_background)
                .setContentTitle("Sankar Says")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NotID, mBuilder.build());
        Log.d("NOTIF", "Notification sent successfully.");
        NotID+=1;
    }
}
