package com.Gone.Shop;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class FCMMessageReceiverService extends FirebaseMessagingService {

    Random rand = new Random();
    Intent i;

    int n = rand.nextInt(50) + 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String time = remoteMessage.getNotification().getTag();
        // Log.e("p",from);

        i = new Intent(this, MainActivity.class);
        if (title.equals("problem add")) {
            i = new Intent(this, AllStatus.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(n, builder.build());
        } else if (title.equals("comment")) {
            i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(n, builder.build());


        }
        else {
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            //Adding notification data to the intent
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("from", title);
            pushNotification.putExtra("time", time);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Intent i = new Intent(this,Message.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(n,builder.build());
        }


    }
}

