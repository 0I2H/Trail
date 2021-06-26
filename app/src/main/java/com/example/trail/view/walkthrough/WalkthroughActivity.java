package com.example.trail.view.walkthrough;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityWalkthroughBinding;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.view.login.LoginViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WalkthroughActivity extends BaseActivity<ActivityWalkthroughBinding, WalkthroughViewModel> {

    public static final String TAG = "WalkthroughActivity";

    WalkthroughViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityWalkthroughBinding binding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_walkthrough;
    }

    @Override
    public WalkthroughViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this)).get(WalkthroughViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();
    }

    @Override
    public void observeViewModel() {

    }
}
