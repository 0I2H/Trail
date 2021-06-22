package com.example.trail.network.helper;

import com.example.trail.network.retrofit.RetrofitService;
import com.example.trail.reactive.AppRxScheduler;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.core.Scheduler;

//@Singleton
public class NetworkHelper {
    RetrofitService retrofitService;
    AppRxScheduler scheduler;

    public NetworkHelper(RetrofitService retrofitService, AppRxScheduler scheduler) {
        this.retrofitService = retrofitService;
        this.scheduler = scheduler;
    }

    public RetrofitService getRetrofitService() {   // == NWRN: getService()
        return this.retrofitService;
    }

    public Scheduler getSchedulerIo() {
        return scheduler.io();
    }

    public Scheduler getSchedulerUi() {
        return scheduler.ui();
    }
}
