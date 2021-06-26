package com.example.trail.network.retrofit;

import com.example.trail.constants.ApiConstants;
import com.example.trail.network.cookies.AddCookiesInterceptor;
import com.example.trail.network.cookies.ReceivedCookiesInterceptor;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private RetrofitService service;

    public RetrofitClient(AddCookiesInterceptor addCookiesInterceptor, ReceivedCookiesInterceptor receivedCookiesInterceptor) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(addCookiesInterceptor)
                .addInterceptor(receivedCookiesInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getService() {
        return service;
    }
}
