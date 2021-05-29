package com.example.trail.base;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/** Hilt vs Dagger2:
 * ActivityModule.java에서 연결할 필요 없이, @InstallIn 사용 (@ContributesAndroidInjector 대신)
 */

// BaseApplication Class (essentail for Hilt)
@HiltAndroidApp
public class TrailApplication extends Application {

    public static boolean DEBUG;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        DEBUG = isDebuggable(this)
//    }
}
