package com.example.administrator.ourschedule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;

public class MapActivity extends AppCompatActivity {
    private TextureMapView mTextureMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        final int dir = this.getIntent().getIntExtra("dir", 0);

        mTextureMapView = (TextureMapView) findViewById(R.id.bmap);
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.point), 100, 100, true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        mTextureMapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
        mTextureMapView.getMap().setMyLocationConfigeration(configuration);

        final MyLocationData.Builder data = new MyLocationData.Builder();
        data.latitude(Mytool.Locla[dir]);
        data.longitude(Mytool.Loclo[dir]);
        data.direction(0);

        mTextureMapView.getMap().setMyLocationData(data.build());

        Button cen = (Button) findViewById(R.id.cen);
        Button bac = (Button) findViewById(R.id.bac_);
        Button nav = (Button) findViewById(R.id.nav);

        cen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng des = new LatLng(Mytool.Locla[dir], Mytool.Loclo[dir]);
                MapStatus mapStatus = new MapStatus.Builder().target(des).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mTextureMapView.getMap().setMapStatus(mapStatusUpdate);
            }
        });

        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this
                        , CurrentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = Intent.getIntent("intent://map/geocoder?location=" +
                            +Mytool.Locla[dir] + "," + Mytool.Loclo[dir] +
                            "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;" +
                            "package=com.baidu.BaiduMap;end");
                    if (isInstallByread("com.baidu.BaiduMap")) {
                        startActivity(intent);
                    } else {
                        Mytool.displayToast("没有安装百度地图", MapActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < 2; i++)
            mTextureMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
        cen.callOnClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTextureMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTextureMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextureMapView.onResume();
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
