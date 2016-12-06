package com.study.android.ahu.mymapsensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

public class MainActivity extends AppCompatActivity {

    private TextureMapView mMapView;
    private ToggleButton mToggleButton;

    private SensorManager mSensorManager;
    private Sensor mMagneticSensor;
    private Sensor mAccelerrometerSensor;

    private LocationManager mLocationManager;
    private Location lastLocation;

    private String provider;
    private float curRotationDegree;
    private SoundPool mSoundPool = null;
    private int soundAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mToggleButton = (ToggleButton) findViewById(R.id.tb_center);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerrometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mSoundPool = new SoundPool(10, AudioManager.STREAM_RING, 100);
        soundAudio = mSoundPool.load(this, R.raw.shake, 1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mAccelerrometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

        try {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.pointer), 100, 100, true);
            BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromBitmap(bitmap);
            mMapView.getMap().setMyLocationEnabled(true);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bitmapD);
            mMapView.getMap().setMyLocationConfigeration(config);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);

            provider = mLocationManager.getBestProvider(criteria, true);

            mLocationManager.getLastKnownLocation(provider);
            lastLocation = mLocationManager.getLastKnownLocation(provider);

            if (lastLocation != null) {
                navigateTo(lastLocation);
            }

            mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
        } catch (SecurityException e) {
            Log.e("Error: ", e.getCause().toString());
        }

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToggleButton.isChecked()) {
                    try {
                        lastLocation = mLocationManager.getLastKnownLocation(provider);
                        navigateTo(lastLocation);
                    } catch (SecurityException e) {
                        Log.e("Error: ", e.getCause().toString());
                    }
                }
            }
        });

        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mToggleButton.setChecked(false);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);

        try {
            mLocationManager.removeUpdates(mLocationListener);
            mMapView.getMap().setMyLocationEnabled(false);
        } catch (SecurityException e) {
            Log.e("Error: ", e.getCause().toString());
        }
    }

    private void navigateTo(Location location) {
        float rotationDegree;

        CoordinateConverter mConverter = new CoordinateConverter();
        mConverter.from(CoordinateConverter.CoordType.GPS);
        mConverter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng desLatLng = mConverter.convert();

        if (mToggleButton.isChecked()) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(desLatLng);
            mMapView.getMap().animateMapStatus(update);
        }

        MyLocationData.Builder data = new MyLocationData.Builder();
        data.latitude(desLatLng.latitude);
        data.longitude(desLatLng.longitude);
        if (curRotationDegree < 0f) {
            rotationDegree  = curRotationDegree + 180f;
        } else {
            rotationDegree = curRotationDegree - 180f;
        }
        data.direction(rotationDegree);

        mMapView.getMap().setMyLocationData(data.build());
    }



    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        private static final int SHAKE_TIMEOUT = 1000;
        private float [] accValues = null;
        private float [] magValues = null;
        private long lastShakeTime = 0;
        private long curShakeTiem = 0;

        private float [] newAccValues = new float[3];
        private float oldRotationDegree = 0f;

        @Override
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    curShakeTiem = System.currentTimeMillis();
                    if (curShakeTiem - lastShakeTime > SHAKE_TIMEOUT) {
                        newAccValues[0] = event.values[0];
                        newAccValues[1] = event.values[1];
                        newAccValues[2] = event.values[2];

                        if (accValues == null) {
                            accValues = new float[3];
                            accValues[0] = newAccValues[0];
                            accValues[1] = newAccValues[1];
                            accValues[2] = newAccValues[2];
                        } else {
                            if (Math.abs(accValues[0] - newAccValues[0]) > 0.5 || Math.abs(accValues[1] - newAccValues[1]) > 0.5
                                    || Math.abs(accValues[2] - newAccValues[2]) > 0.5) {
                                accValues[0] = newAccValues[0];
                                accValues[1] = newAccValues[1];
                                accValues[2] = newAccValues[2];
                                Toast.makeText(MainActivity.this, "Phone is shaking!", Toast.LENGTH_SHORT).show();
                                mSoundPool.play(soundAudio, 1, 1, 0, 0, 1);
                            }
                        }
                        lastShakeTime = curShakeTiem;
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    if (magValues == null) {
                        magValues = new float[3];
                    }
                    magValues[0] = event.values[0];
                    magValues[1] = event.values[1];
                    magValues[2] = event.values[2];
                    float [] R  = new float[9];
                    float [] values = new float[3];
                    if (accValues != null && magValues != null) {
                        SensorManager.getRotationMatrix(R, null, accValues, magValues);
                        SensorManager.getOrientation(R, values);
                        curRotationDegree = (float) Math.toDegrees(values[0]);
                    }
                    if (Math.abs(curRotationDegree - oldRotationDegree) > 1) {
                        try {
                            lastLocation = mLocationManager.getLastKnownLocation(provider);
                            navigateTo(lastLocation);
                        } catch (SecurityException e) {
                            Log.e("Error: ", e.getCause().toString());
                        }
                        oldRotationDegree = curRotationDegree;
                    }
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                lastLocation = mLocationManager.getLastKnownLocation(provider);
                navigateTo(lastLocation);
            } catch (SecurityException e) {
                Log.e("Error: ", e.getCause().toString());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
}
