package com.example.trail.di;

import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.network.retrofit.RetrofitService;
import com.example.trail.reactive.AppRxScheduler;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@InstallIn(ActivityComponent.class)
@Module
public abstract class ActivityModule {
}
