package com.study.android.ahu.experimentfour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * Created by ahu on 16-10-21.
 */
public class StaticReceiver extends BroadcastReceiver {

    private static final String STATICACTION = "com.study.android.ahu.experimentfour.staticreceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(STATICACTION)) {
            Bundle bundle = intent.getExtras();
            serializedItem item = (serializedItem) bundle.getSerializable("object");

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), item.getId());

            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle("静态广播")
                    .setContentText(item.getName())
                    .setLargeIcon(bm)
                    .setSmallIcon(item.getId())
                    .setAutoCancel(true);

            Intent mIntent = new Intent(context, MainActivity.class);
            PendingIntent myPendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
            builder.setContentIntent(myPendingIntent);

            Notification notify = builder.build();
            manager.notify(0, notify);
        }
    }
}
