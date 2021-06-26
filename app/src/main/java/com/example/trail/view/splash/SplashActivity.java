package com.example.trail.view.splash;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.base.BaseActivity;
import com.example.trail.databinding.ActivitySplashBinding;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.login.LoginActivity;
import com.example.trail.view.walkthrough.WalkthroughActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    public static final String TAG = "SplashActivity";

    SplashViewModel viewModel;
    private ActivitySplashBinding binding;

    // @Inject 이 Hilt에서는 OnCreate()에서처럼으로
    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void observeViewModel() { }  // 05.29 nothing in viewModel

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
         viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this)).get(SplashViewModel.class);
        // == <Kotlin> private val viewModel: MyViewModel by viewModels()

        binding = getViewDataBinding();

        getPermission(this);

        // TODO make splash screen wait untill the animation finishes (thread로 강제 sleep은 좋은 방법이 아님)
        /** 애니메이션!! */
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
        checkApkVersion();

        if (appPreferencesHelper.isFirstExecution())
            showWalkThrough();  // 첫 실행: first execution after app install
        else
            goToLoginActivity();
    }


    /**
     * Returns the current state of the permissions needed.
     */
    // TODO 더 추가하기
    private boolean checkPermissions() {
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission_group.CAMERA);
        return true;
    }

    public void checkApkVersion() {     // check for APK version updates
        // not necessary
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showWalkThrough() {
        Intent intent = new Intent(this, WalkthroughActivity.class);
        startActivity(intent);
//        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        boolean hasLoginInfo = hasLoginInfo();
//        if (!hasLoginInfo && sharedPreferences.getBoolean(IS_FIRST_IN, true)) {
//            goToActivity(Wa);
//        } else {
//            if (hasLoginInfo)
//                goToActivity()
//        }
        finish();
    }


}
