package com.example.administrator.ourschedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MReceiver extends BroadcastReceiver {
    public MReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notin = new Intent(context, CurrentActivity.class);
        PendingIntent penin = PendingIntent.getActivity(context, 1, notin, 0);

        String st3;
        Mytool.Course ct = Mytool.getncCo(1);
        if (ct != null) {
            String st0 = ct._dataS.get(0);
            ArrayList<Integer> tia = ct._datat;
            int[] tsa0 = Mytool.gettime(tia.get(1), tia.get(2));
            String st1 = ct._dataS.get(6);
            String st2 = Mytool.getTS(tsa0[0], tsa0[1]) + "--" + Mytool.getTS(tsa0[2], tsa0[3]);
            st3 = st0 + "\n" + st1 + "\n" + st2;
        } else {
            st3 = "暂无信息";
        }

        Notification.Builder builder = new Notification.Builder(context);
        try {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_course_table);
            builder.setContentTitle("下一节课")
                    .setContentIntent(penin)
                    .setContentText(st3)
                    .setTicker("下一节课")
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationManager manager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = builder.build();
        manager.notify(0, noti);

        Mytool.updateWidget(context);
    }
}