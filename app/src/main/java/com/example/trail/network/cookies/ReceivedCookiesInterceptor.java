package com.example.trail.network.cookies;

import com.example.trail.database.AppPreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    private AppPreferencesHelper appPreferencesHelper;

    public ReceivedCookiesInterceptor(AppPreferencesHelper appPreferencesHelper) {
        this.appPreferencesHelper = appPreferencesHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
//        BufferedSource source = originalResponse.body().source();

        if (!originalResponse.headers("Set-Cookie").isEmpty()) { // fix: 인자이름 (06.22 맞음)
            HashSet<String> cookies = new HashSet<>(originalResponse.headers("Set-Cookie"));

            // Preference에 cookies를 넣어주는 작업을 수행
            appPreferencesHelper.setCookie(cookies);
        }

        return originalResponse;
    }
}
