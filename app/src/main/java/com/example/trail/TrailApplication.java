package com.example.trail;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import dagger.Provides;
import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.android.internal.migration.InjectedByHilt;

/** Hilt vs Dagger2:
 * ActivityModule.java에서 연결할 필요 없이, @InstallIn 사용 (@ContributesAndroidInjector 대신)
 */

// BaseApplication Class (essential for Hilt)
@HiltAndroidApp
public class TrailApplication extends Application {

//    public static boolean DEBUG;

    public static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
//        DEBUG = isDebuggable(this)
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
}
