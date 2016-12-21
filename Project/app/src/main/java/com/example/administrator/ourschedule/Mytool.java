package com.example.administrator.ourschedule;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class Mytool {
    public static ArrayList<Course> Cdata = null;
    public final static String[] WSa = {"星期一", "星期二", "星期三", "星期四", "星期五",
            "星期六", "星期日"};
    private static Toast mytoast;
    public final static double[] Locla = {23.07263, 23.102726,
            23.134613, 22.348054};
    public final static double[] Loclo = {113.397157, 113.305314,
            113.29652, 113.595626};

    public static boolean isRunning = false;

    public static void displayToast(String str, Context context) {
        if (mytoast == null)
            mytoast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        else
            mytoast.setText(str);
        mytoast.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getMD5(String str) {
        MessageDigest md = null;
        String Mst = "";
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            Mst = buf.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mst;
    }

    public static class Course {
        public ArrayList<String> _dataS;
        public ArrayList<Integer> _datat;

        public Course(ArrayList<String> Csl, ArrayList<Integer> Cit) {
            _dataS = Csl;
            _datat = Cit;
        }
    }

    public static int WeektoInt(String Wst) {
        int Wnt = 0;
        for (int i = 0; i < 7; i++) {
            if (Wst.equals(WSa[i]))
                Wnt = i + 1;
        }
        return Wnt;
    }

    public static String InttoWeek(int i) {
        return WSa[i - 1];
    }

    public static int[] gettime(int st, int en) {
        int[] TAt = new int[4];
        TAt[0] = 8 + ((st - 1) * 55) / 60;
        TAt[1] = ((st - 1) * 55) % 60;
        TAt[2] = 8 + ((en - 1) * 55 + 45) / 60;
        TAt[3] = ((en - 1) * 55 + 45) % 60;
        return TAt;
    }

    public static String getTS(int st, int en) {
        if (en < 10)
            return st + ":0" + en;
        return st + ":" + en;
    }

    public static int inc(int x, int y, int h, int m) {
        int[] xt = gettime(x, y);
        if (!more_than(xt[0], xt[1], h, m) && more_than(xt[2], xt[3], h, m))
            return 0;
        else if (!more_than(xt[0], xt[1], h, m))
            return 1;
        return -1;
    }

    public static boolean more_than(int h0, int m0, int h1, int m1) {
        if (h0 > h1)
            return true;
        else if (h0 == h1 && m0 > m1)
            return true;
        return false;
    }

    public static boolean comp(Course ct0, Course ct1, int e, int h, int m) {
        if (ct1._datat.get(0) == e && inc(ct1._datat.get(1), ct1._datat.get(2), h, m) != -1)
            return false;
        if ((ct1._datat.get(0) - e + 7) % 7 < (ct0._datat.get(0) - e + 7) % 7)
            return true;
        else if ((ct1._datat.get(0) - e + 7) % 7 == (ct0._datat.get(0) - e + 7) % 7
                && ct1._datat.get(1) < ct0._datat.get(1))
            return true;
        return false;
    }

    public static Course getncCo(int ty) {
        if (Cdata == null)
            return null;
        Date curDate = new Date(System.currentTimeMillis());
        int cue = Mytool.WeektoInt((new SimpleDateFormat("EEEE")).format(curDate));
        int cuh = Integer.parseInt((new SimpleDateFormat("HH")).format(curDate));
        int cum = Integer.parseInt((new SimpleDateFormat("mm")).format(curDate));
        Mytool.Course ct0 = null, ct1 = null;
        for (Mytool.Course crt : Mytool.Cdata) {
            if (crt._datat.get(0) == cue && Mytool.inc(crt._datat.get(1),
                    crt._datat.get(2), cuh, cum) == 0)
                ct0 = crt;
            if (ct1 == null && !(crt._datat.get(0) == cue && Mytool.inc(crt._datat.get(1),
                    crt._datat.get(2), cuh, cum) != -1))
                ct1 = crt;
            else if (Mytool.comp(ct1, crt, cue, cuh, cum))
                ct1 = crt;
        }

        if (ty == 0)
            return ct0;
        return ct1;
    }

    public static ArrayList<Course> gettoCo() {
        ArrayList<Course> ACt = new ArrayList<>();
        if (Cdata == null)
            return ACt;
        Date curDate = new Date(System.currentTimeMillis());
        int cue = Mytool.WeektoInt((new SimpleDateFormat("EEEE")).format(curDate));
        for (Mytool.Course crt : Mytool.Cdata) {
            if (crt._datat.get(0) == cue) {
                ArrayList<Course> ACtt = new ArrayList<>();
                for (int i = ACt.size() - 1; i >= 0 && crt._datat.get(1) < ACt.get(i)._datat.get(1);
                     i--) {
                    ACtt.add(ACt.get(i));
                    ACt.remove(i);
                }
                ACt.add(crt);
                for (int i = ACtt.size() - 1; i >= 0; i--)
                    ACt.add(ACtt.get(i));
            }
        }
        return ACt;
    }

    public static void updateWidget(Context context) {
        Date curDate = new Date(System.currentTimeMillis());
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.mwidget);
        Intent inte = new Intent(context, UpdateService.class);
        rv.setRemoteAdapter(R.id.wlsv, inte);
        if (Mytool.gettoCo().size() == 0)
            rv.setViewVisibility(R.id.wnha, View.VISIBLE);
        else
            rv.setViewVisibility(R.id.wnha, View.GONE);
        rv.setTextViewText(R.id.wtv, (new SimpleDateFormat("EEEE")).format(curDate));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
                context.getApplicationContext());
        ComponentName componentName = new ComponentName(context.getApplicationContext(),
                MWidget.class);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context.getApplicationContext(), MWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.wlsv);
        appWidgetManager.updateAppWidget(componentName, rv);
    }
}