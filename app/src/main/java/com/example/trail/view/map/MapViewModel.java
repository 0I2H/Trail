package com.example.trail.view.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends BaseViewModel {

    public static final String TAG = "MapViewModel";

    private MutableLiveData<String> intentActionLiveData;

    @Inject
    public MapViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        intentActionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getIntentActionLiveData() {
        return intentActionLiveData;
    }

    public void onLocationStateBtnClicked() {

    }
    public void pinMarkerSelected() {

    }

    public void onBackBtnClicked() {
//        intentActionLiveData.setValue("goBack");
        intentActionLiveData.setValue("DashboardActivity");
    }

    public void onSettingsBtnClicked() {
        intentActionLiveData.setValue("SettingsActivity");
    }
}
