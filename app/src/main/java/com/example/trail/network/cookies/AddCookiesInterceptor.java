package com.example.trail.network.cookies;

import android.content.SharedPreferences;

import com.example.trail.database.AppPreferencesHelper;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

// Reference: https://gun0912.tistory.com/50
// Preference에 저장되어 있는 cookie 값을 Request 'header'에 함께 넣어 보냄
public class AddCookiesInterceptor implements Interceptor {

    private AppPreferencesHelper appPreferencesHelper;

    public AddCookiesInterceptor(AppPreferencesHelper appPreferencesHelper) {
        this.appPreferencesHelper = appPreferencesHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        // Preference에서 cookies를 가져오는 작업을 수행
        Set<String> preferences = appPreferencesHelper.getCookie();

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        int userID = appPreferencesHelper.getUserID();
        if (userID > 0)         // fixme: match key in database (server doc)
            builder.addHeader("userId", String.valueOf(userID));

        //todo 더 추가
        // todo Web, Android, iOS 구분을 위해 User-Agent setting
        builder.removeHeader("USER_AGENT").addHeader("USER_AGENT", "Android");

        return chain.proceed(builder.build());
    }
}
