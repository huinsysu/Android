package com.example.administrator.ourschedule;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

public class MService extends Service {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private IntentFilter dyfil;
    private Vibrator vibrator;
    private BroadcastReceiver DR;
    private long [] pattern = {100,400,100,400};

    public MService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        dyfil = new IntentFilter();
        dyfil.addAction("com.OurSchedule.receiver");
        DR = new MReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mSensorManager.registerListener(sensorEventListener, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        registerReceiver(DR, dyfil);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accv = null;
        long lastt = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    long curTime = java.lang.System.currentTimeMillis();
                    if (accv == null)
                        lastt = curTime;
                    if ((curTime - lastt) > 10 && accv != null) {
                        long diffTime = (curTime - lastt);
                        lastt = curTime;
                        float[] at = event.values;
                        float speed = Math.abs(at[0] + at[1] + at[2] -
                                accv[0] - accv[0] - accv[0])
                                / diffTime * 10000;
                        if (speed > 30000d) {
                            vibrator.vibrate(pattern,-1);
                            Intent intent = new Intent("com.OurSchedule.receiver");
                            sendBroadcast(intent);
                        }
                    }
                    accv = event.values;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


}
