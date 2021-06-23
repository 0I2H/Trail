package com.example.trail.view.history;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;

//@HiltViewModel
public class HistoryViewModel extends BaseViewModel {

    public static final String TAG = "";

    public HistoryViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }
}
