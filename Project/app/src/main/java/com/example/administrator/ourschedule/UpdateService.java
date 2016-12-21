package com.example.administrator.ourschedule;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class UpdateService extends RemoteViewsService {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final Context mContext;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return Mytool.gettoCo().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= Mytool.gettoCo().size())
                return null;
            Mytool.Course ct = Mytool.gettoCo().get(position);
            RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_item);

            ArrayList<Integer> tia = ct._datat;
            int[] tsa0 = Mytool.gettime(tia.get(1), tia.get(2));
            rv.setTextViewText(R.id.wna, ct._dataS.get(0));
            rv.setTextViewText(R.id.wsi, ct._dataS.get(6));
            rv.setTextViewText(R.id.wti, tia.get(1) + "-" + tia.get(2));
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}