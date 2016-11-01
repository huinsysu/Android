package com.study.android.ahu.experimentfour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ahu on 16-10-22.
 */
public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "com.study.android.ahu.experimentfour.dynamicreceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICACTION)) {
            String str = intent.getExtras().getString("text");

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dynamic);

            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle("动态广播")
                    .setContentText(str)
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.dynamic)
                    .setAutoCancel(true);

            Intent mIntent = new Intent(context, MainActivity.class);
            PendingIntent myPendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
            builder.setContentIntent(myPendingIntent);

            Notification notify = builder.build();
            manager.notify(0, notify);
        }
    }
}
