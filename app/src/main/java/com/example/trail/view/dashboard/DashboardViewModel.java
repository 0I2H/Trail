package com.example.trail.view.dashboard;

import android.Manifest;
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

import static com.example.trail.constants.AppConstants.KEY_REQUESTING_LOCATION_UPDATES;

@HiltViewModel
public class DashboardViewModel extends BaseViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "DashboardViewModel";

    private Context context;

    AppPreferencesHelper appPreferencesHelper;

    private final MutableLiveData<PinDTO> pinLiveData;
    private final MutableLiveData<Integer> pinLoggingState;        /** -2: empty, -1: stop, 0: pause, 1: start */

    @Inject
    public DashboardViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.pinLiveData = new MutableLiveData<>();

        /*임시*/
        this.pinLoggingState = new MutableLiveData<>();       // default value to 'stop'

    }

    public MutableLiveData<PinDTO> getPinLiveData() {
        return pinLiveData;
    }

    public MutableLiveData<Integer> getPinLoggingState() {
        return pinLoggingState;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        if(context != null) {
            appPreferencesHelper = new AppPreferencesHelper(context);
            /*TODO*/ //fixme: activity에서 현재 서비스 상태 받아올것
            int state = appPreferencesHelper.getLocationTrackingPref() ? 1 : -2;
            pinLoggingState.setValue(state);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Update the buttons state depending on whether location updates are being requested
        if (key.equals(KEY_REQUESTING_LOCATION_UPDATES)) {
//            setButtonsState(sharedPreferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false));
        }
    }
}
