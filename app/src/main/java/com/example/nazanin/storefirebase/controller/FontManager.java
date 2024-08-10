package com.example.nazanin.storefirebase.controller;

import android.app.Application;

import com.example.nazanin.storefirebase.R;

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
