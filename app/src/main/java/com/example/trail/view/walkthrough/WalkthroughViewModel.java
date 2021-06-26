package com.example.trail.view.walkthrough;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WalkthroughViewModel extends BaseViewModel {

    @Inject
    public WalkthroughViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
