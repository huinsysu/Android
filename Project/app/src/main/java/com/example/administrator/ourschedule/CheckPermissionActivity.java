package com.example.administrator.ourschedule;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class CheckPermissionActivity extends AppCompatActivity {
    private Handler han = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        RxPermissions rxp = new RxPermissions(this);
        rxp
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.VIBRATE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            han.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(CheckPermissionActivity.this
                                            , MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(CheckPermissionActivity.this,
                                    "App will finish in 3 secs...", Toast.LENGTH_LONG).show();
                            han.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        }
                    }
                });
    }
}
