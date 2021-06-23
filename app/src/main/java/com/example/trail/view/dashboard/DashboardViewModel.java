package com.example.trail.view.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.R;
import com.example.trail.base.BaseViewModel;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.model.pin.PinDTO;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

import static com.example.trail.constants.AppConstants.PREF_KEY_LOCATION_SERVICE_STATE;


@HiltViewModel
//public class DashboardViewModel extends BaseViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {
public class DashboardViewModel extends BaseViewModel {

    public static final String TAG = "DashboardViewModel";


    private final MutableLiveData<PinDTO> pinLiveData;
    private final MutableLiveData<Integer> pinLoggingState;        /** -2: empty, -1: stop, 0: pause, 1: start */
    private final MutableLiveData<String> intentActionLiveData;

    @Inject
    public DashboardViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.pinLiveData = new MutableLiveData<>();

        /*임시*/
        this.pinLoggingState = new MutableLiveData<>();       // default value to 'stop'

        this.intentActionLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<PinDTO> getPinLiveData() {
        return pinLiveData;
    }

    public MutableLiveData<Integer> getPinLoggingState() {
        return pinLoggingState;
    }

    public MutableLiveData<String> getIntentActionLiveData() {
        return intentActionLiveData;
    }

    public void startLoggingService() {
    // todo RESEARCH- 'Service'는 액티비티에 붙는건가, vm 쪽에 붙는 건가
        pinLoggingState.setValue(1);
        Log.i(TAG, "-----------------");
    }

    public void pauseLoggingService() {
        pinLoggingState.setValue(0);
    }

    public void stopLoggingService() {
        pinLoggingState.setValue(-1);
    }

    public void snipPath() {
        // TODO 위치기록 중단, path 저장할지 묻는 다이얼로그에서 사용자가 예 누를 시 서버업로드

    }

    public void setServiceState (int state) {
        pinLoggingState.setValue(state);
    }

    public void goToProfileActivity() {
        intentActionLiveData.setValue("ProfileActivity");
    }

    public void goToSettingsActivity() {
        intentActionLiveData.setValue("SettingsActivity");
    }

    public void goToMapActivity () {
        intentActionLiveData.setValue("MapActivity");
    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        // Update the buttons state depending on whether location updates are being requested
//        if (key.equals(PREF_KEY_LOCATION_SERVICE_STATE)) {
////            setButtonsState(sharedPreferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false));
//            setServiceState(sharedPreferences.getInt(PREF_KEY_LOCATION_SERVICE_STATE, -2));
//        }
//    }
}
