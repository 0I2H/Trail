package com.example.trail.view.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.model.pin.PinDTO;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DashboardViewModel extends BaseViewModel {

    public static final String TAG = "DashboardViewModel";

    private final MutableLiveData<PinDTO> pinLiveData;

    @Inject
    public DashboardViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.pinLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PinDTO> getPinLiveData() {
        return pinLiveData;
    }

    public void startLoggingService() {

    }

    public void pauseLoggingService() {

    }

    public void stopLoggingService() {

    }
}
