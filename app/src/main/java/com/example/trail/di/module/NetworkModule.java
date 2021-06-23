package com.example.trail.di.module;

import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.network.cookies.AddCookiesInterceptor;
import com.example.trail.network.cookies.ReceivedCookiesInterceptor;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.network.retrofit.RetrofitClient;
import com.example.trail.network.retrofit.RetrofitService;
import com.example.trail.reactive.AppRxScheduler;
import com.example.trail.service.PinLocationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.components.SingletonComponent;

@InstallIn({SingletonComponent.class, ServiceComponent.class})
@Module
public class NetworkModule {

    @Provides
    AddCookiesInterceptor provideAddCookiesInterceptor (AppPreferencesHelper appPreferencesHelper) {
        return new AddCookiesInterceptor(appPreferencesHelper);
    }

    @Provides
    ReceivedCookiesInterceptor provideReceivedCookiesInterceptor (AppPreferencesHelper appPreferencesHelper) {
        return new ReceivedCookiesInterceptor(appPreferencesHelper);
    }

//    @Singleton
    @Provides
    RetrofitService provideRetrofitService (AddCookiesInterceptor addCookiesInterceptor, ReceivedCookiesInterceptor receivedCookiesInterceptor) {
        return new RetrofitClient(addCookiesInterceptor, receivedCookiesInterceptor).getService();
    }

//    @Singleton
    @Provides
    NetworkHelper provideNetworkHelper (RetrofitService retrofitService, AppRxScheduler scheduler) {
        return new NetworkHelper(retrofitService, scheduler);
    }
}
