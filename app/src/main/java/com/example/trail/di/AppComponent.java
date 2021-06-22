package com.example.trail.di;

import android.content.Context;

import com.example.trail.TrailApplication;
import com.example.trail.network.helper.NetworkHelper;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.hilt.DefineComponent;
import dagger.hilt.components.SingletonComponent;

@Singleton
@Component(modules = {AppModule.class, ActivityModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(TrailApplication trailApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(TrailApplication trailApplication);
//        Builder context(@BindsInstance Context context);

        AppComponent build();
    }
//
//    @Binds
//    public abstract NetworkHelper bindNetworkHelper (
//
//    );
}
