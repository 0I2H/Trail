package com.example.trail.view.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.R;
import com.example.trail.databinding.ActivitySplashBinding;
import com.example.trail.base.BaseActivity;
import com.example.trail.view.login.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.PREFS_NAME;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    public static final String TAG = "SplashActivity";

    // @Inject 이 Hilt에서는 OnCreate()에서처럼으로
    SplashViewModel viewModel;
    private ActivitySplashBinding binding;

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

//        viewModel.setNavigator(this);   // fixme Necessary?
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkApkVersion();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);  // todo vm ㅇㅡ로 옮기기
//        if(!sharedPreferences.getBoolean(
//                MyO
//        ))
        //fixme
        goToLoginActivity();
    }

    public void checkApkVersion() {     // check for APK version updates
        // not necessary
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

//    public void showWalkThrough() {
//        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        boolean hasLoginInfo = hasLoginInfo();
//        if (!hasLoginInfo && sharedPreferences.getBoolean(IS_FIRST_IN, true)) {
//            goToActivity(Wa);
//        } else {
//            if (hasLoginInfo)
//                goToActivity()
//        }
//    }


}
