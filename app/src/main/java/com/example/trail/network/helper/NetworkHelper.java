package com.example.trail.network.helper;

import com.example.trail.network.retrofit.RetrofitService;
import com.example.trail.reactive.AppRxScheduler;

import io.reactivex.rxjava3.core.Scheduler;

public class NetworkHelper {
    private RetrofitService retrofitService;
    private AppRxScheduler scheduler;

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
