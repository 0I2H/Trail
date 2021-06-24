package com.example.trail.di.module;

import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.network.cookies.AddCookiesInterceptor;
import com.example.trail.network.cookies.ReceivedCookiesInterceptor;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.network.retrofit.RetrofitClient;
import com.example.trail.network.retrofit.RetrofitService;
import com.example.trail.reactive.AppRxScheduler;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.components.SingletonComponent;

@InstallIn(FragmentComponent.class)
@Module
public class FragmentModule {
//    @Provides
//    AddCookiesInterceptor provideAddCookiesInterceptor (AppPreferencesHelper appPreferencesHelper) {
//        return new AddCookiesInterceptor(appPreferencesHelper);
//    }
//
//    @Provides
//    ReceivedCookiesInterceptor provideReceivedCookiesInterceptor (AppPreferencesHelper appPreferencesHelper) {
//        return new ReceivedCookiesInterceptor(appPreferencesHelper);
//    }
//
//        @FragmentScoped
//    @Provides
//    RetrofitService provideRetrofitService (AddCookiesInterceptor addCookiesInterceptor, ReceivedCookiesInterceptor receivedCookiesInterceptor) {
//        return new RetrofitClient(addCookiesInterceptor, receivedCookiesInterceptor).getService();
//    }
//
//        @FragmentScoped
//    @Provides
//    NetworkHelper provideNetworkHelper (RetrofitService retrofitService, AppRxScheduler scheduler) {
//        return new NetworkHelper(retrofitService, scheduler);
//    }
}
