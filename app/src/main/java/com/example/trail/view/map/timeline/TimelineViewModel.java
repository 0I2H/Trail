package com.example.trail.view.map.timeline;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.model.pin.PinDTO;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TimelineViewModel extends BaseViewModel {

    public static final String TAG = "TimelineViewModel";

    private MutableLiveData<List<PinDTO>> pinListLiveData;

    @Inject
    public TimelineViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        pinListLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<PinDTO>> getPinListLiveData() {
        return pinListLiveData;
    }
}
