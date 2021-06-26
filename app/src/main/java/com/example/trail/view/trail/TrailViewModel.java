package com.example.trail.view.trail;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TrailViewModel extends BaseViewModel {

    @Inject
    public TrailViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    public void onCreateTrailClick() {

    }
}
