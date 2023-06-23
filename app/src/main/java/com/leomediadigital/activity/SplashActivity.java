package com.leomediadigital.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;

import com.leomediadigital.R;
import com.leomediadigital.util.RuntimePermissionsActivity;
import com.leomediadigital.util.SharedPref;

public class SplashActivity  extends RuntimePermissionsActivity {

    private static final int REQUEST_PERMISSIONS = 20;
    int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            SplashActivity.super.requestAppPermissions(new String[]{
                            android.Manifest.permission.INTERNET,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    }, R.string.runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        getSplashscreens();
    }

    public void getSplashscreens() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            if (SharedPref.getLogin(SplashActivity.this)){
                                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }else {
                                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                finish();
                            }

                        }
                    }
                };
                thread.start();
            }
        }, SPLASH_TIME_OUT);

    }
}
