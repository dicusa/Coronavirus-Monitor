package com.example.coronachecker.application;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;


public class ApplicationMain extends Application {
    @Override
    public void onCreate() {
        MapsInitializer.initialize(this);
        super.onCreate();
    }
}
