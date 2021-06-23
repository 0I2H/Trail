package com.example.trail.di.component;

import com.example.trail.TrailApplication;
import com.example.trail.di.module.ActivityModule;
import com.example.trail.di.module.AppModule;
import com.example.trail.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

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
