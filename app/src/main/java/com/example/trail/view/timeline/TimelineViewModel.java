package com.example.trail.view.timeline;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

//@HiltViewModel
public class TimelineViewModel extends BaseViewModel {

    public static final String TAG = "TimelineViewModel";

//    @Inject
    public TimelineViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
