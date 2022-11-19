package com.example.viewpage2autoscroll2;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
