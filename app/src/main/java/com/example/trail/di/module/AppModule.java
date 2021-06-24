package com.example.trail.di.module;

import android.content.Context;

import com.example.trail.TrailApplication;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.reactive.AppRxScheduler;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn({SingletonComponent.class, ServiceComponent.class, FragmentComponent.class})
@Module
public final class AppModule {

    @Provides
//    @Singleton
    Context provideContext(@ApplicationContext Context appContext) {
        return appContext;
    }

    @Provides
//    @Singleton
    AppPreferencesHelper provideAppPreferencesHelper (Context context) {
        return new AppPreferencesHelper(context);
    }

    @Provides
    AppRxScheduler provideAppRxScheduler() {
        return new AppRxScheduler();
    }


}
