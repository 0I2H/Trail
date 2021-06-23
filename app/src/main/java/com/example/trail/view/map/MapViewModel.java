package com.example.trail.view.map;

import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends BaseViewModel {

    public static final String TAG = "MapViewModel";

    @Inject
    public MapViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    public void onLocationStateBtnClicked() {

    }
    public void pinMarkerSelected() {

    }
}
