package com.study.android.ahu.experimentfour;

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
import android.widget.RemoteViews;

/**
 * Created by ahu on 16-10-22.
 */
public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "com.study.android.ahu.experimentfour.dynamicreceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICACTION)) {
            String str = intent.getExtras().getString("text");
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);

            rv.setTextViewText(R.id.appwidget_text, str);
            rv.setImageViewResource(R.id.appwidget_image, R.mipmap.dynamic);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int [] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),
                    WidgetDemo.class.getName()));
            appWidgetManager.updateAppWidget(widgetIds, rv);

        }
    }
}
