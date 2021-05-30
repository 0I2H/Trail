package com.example.trail.view.signup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.databinding.ActivitySignupBinding;
import com.example.trail.view.splash.SplashViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends BaseActivity<ActivitySignupBinding, SignUpViewModel> {

    public static final String TAG = "SignUpActivity";

    SignUpViewModel viewModel;
    private ActivitySignupBinding binding;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public SignUpViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void observeViewModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this))
                .get(SignUpViewModel.class);

        binding = getViewDataBinding();
    }
}
