package com.example.nazanin.store.controller;

import android.app.Application;

import com.example.nazanin.store.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FontManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/byekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
