package com.example.trail.view.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.TrailApplication;
import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.Provides;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModel extends BaseViewModel {

    public static final String TAG = "SplashViewModel";

    @Inject
    public SplashViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    public void requestApkVersion(String apkVersion) {      // check for APK updates

    }


}
