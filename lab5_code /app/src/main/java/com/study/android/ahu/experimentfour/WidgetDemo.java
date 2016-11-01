package com.study.android.ahu.experimentfour;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetDemo extends AppWidgetProvider {

    private static final String STATICACTION = "com.study.android.ahu.experimentfour.staticreceiver";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent clickInt = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        rv.setOnClickPendingIntent(R.id.appwidget_image, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(STATICACTION)) {
            Bundle bundle = intent.getExtras();
            serializedItem item = (serializedItem) bundle.getSerializable("object");
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);

            rv.setTextViewText(R.id.appwidget_text, item.getName());
            rv.setImageViewResource(R.id.appwidget_image, item.getId());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int [] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),
                    WidgetDemo.class.getName()));
            appWidgetManager.updateAppWidget(widgetIds, rv);
        }
    }

}

