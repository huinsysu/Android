package com.study.android.ahu.experiment_six;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

public class MusicService extends Service {

    public static MediaPlayer mp = new MediaPlayer();
    private int state = 0;
    private int rotation = 0;
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/song.mp3");
            mp.prepare();
            mp.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public void play() {
        if (mp.isPlaying()) {
            mp.pause();
            state = 2;
        } else {
            mp.start();
            state = 1;
        }
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
            state = 3;
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getState() {
        return state;
    }

    public void initRotation() {
        rotation = 0;
    }

    public void setRotation() {
        rotation = (rotation + 1) % 360;
    }

    public int getRotation() {
        return rotation;
    }
}
