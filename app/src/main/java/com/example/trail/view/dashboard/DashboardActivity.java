package com.example.trail.view.dashboard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.trail.BR;
import com.example.trail.BuildConfig;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityDashboardBinding;
import com.example.trail.service.PinLocationService;
import com.example.trail.view.map.MapActivity;
import com.example.trail.view.profile.ProfileActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST;
import static com.example.trail.constants.AppConstants.EXTRA_LOCATION;
import static com.example.trail.constants.AppConstants.EXTRA_TRAIL_ID;
import static com.example.trail.constants.AppConstants.PREF_KEY_LOCATION_SERVICE_STATE;
import static com.example.trail.utils.Utils.locationToText;

@AndroidEntryPoint
public class DashboardActivity extends BaseActivity<ActivityDashboardBinding, DashboardViewModel> implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "DashboardActivity";

    DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;

    @Inject
    AppPreferencesHelper appPreferencesHelper;

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private LocationServiceBroadcastReceiver broadcastReceiver;
    // A reference to the service used to get location updates.
    public PinLocationService locationService = null;
    // Tracks the bound state of the service.
    private boolean boundState = false;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public DashboardViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this)).get(DashboardViewModel.class);
//        viewModel.setContext(this);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        broadcastReceiver = new LocationServiceBroadcastReceiver();

        observeViewModel();

        // fixme: VM에서 해야 할 일
        // on
        // Check permissions
//        if (appPreferencesHelper.getLocationTrackingPref(this)) {
//            if (!checkPermissions()) {
//                requestPermissions();
//            }
//        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter((ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)));     // fixme: IntentFilter 내용
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (boundState) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(serviceConnection);
            boundState = false;
        }
        super.onStop();
    }



    @Override
    public void observeViewModel() {

        viewModel.getPinLoggingState().observe(this, state -> {
            switch (state) {
                case 1:
                    /** Start Location tracking */
                    if (!checkPermissions()) requestPermissions();
                    else locationService.subscribeToLocationUpdates();
                    setButtonsState(1);        // Restore the state of the buttons when the activity (re)launches.
                    break;

                case 0:
                    /** Pause Location tracking */
                    // fixme: 일시정지하는 법
                    locationService.unsubscribeToLocationUpdate();
                    setButtonsState(0);
                    break;

                case -1:
                    /** Abort(Stop) Location tracking */
                    locationService.unsubscribeToLocationUpdate();
                    setButtonsState(-1);        // Restore the state of the buttons when the activity (re)launches.
                    // TODO snip path
                    viewModel.snipPath();
                    break;

                case -2:
                    Log.i(TAG, "Empty path");
                    setButtonsState(-2);
                    break;

                default:
                    Log.e(TAG, "Invalid data in pinLoggingState livedata");
                    setButtonsState(-2);
                    break;
            }
        });

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, PinLocationService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        viewModel.getIntentActionLiveData().observe(this, action -> {
            Intent intent;
            switch (action) {
                // TODO
                case "ProfileActivity":
                    intent = new Intent(this, ProfileActivity.class);
//                    intent.putExtra();
                    startActivity(intent);
                    break;
                case "SettingsActivity":
                case "MapActivity":
                    intent = new Intent(this, MapActivity.class);
//                    intent.putExtra(EXTRA_TRAIL_ID, binding.);  // todo journeysId
                    startActivity(intent);
                    break;
            }
        });
    }




    // Monitors the state of the connection to the service.
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PinLocationService.LocalBinder binder = (PinLocationService.LocalBinder) service;
            locationService = binder.getService();
            boundState = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            boundState = false;
        }
    };


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // 06.03 VM으로 옮겨짐
        // todo 지우고 해결할 것
        // Update the buttons state depending on whether location updates are being requested
        if (key.equals(PREF_KEY_LOCATION_SERVICE_STATE)) {
            int state = sharedPreferences.getInt(PREF_KEY_LOCATION_SERVICE_STATE, -2);
//            setButtonsState(state);
            viewModel.setServiceState(state);
        }

    }



    /**
     * Receiver for broadcasts sent by {@link PinLocationService}.
     */
    private class LocationServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(DashboardActivity.this, locationToText(location), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(binding.getRoot(), R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, v -> {
                        // Request permission
                        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
      * Callback received when a permissions request has been completed.
      */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult() called");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was canceled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                locationService.subscribeToLocationUpdates();
            } else {
                // Permission was denied.
                setButtonsState(Integer.MIN_VALUE);
                Snackbar.make(binding.getRoot(), R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, v -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show();
            }
        }
    }

    private void setButtonsState(int requestingLocationState) {
        switch (requestingLocationState) {
            case 1:
                // 현재 위치 기록 중
                binding.gpsTestButton.setEnabled(false);
                binding.gpsTestPause.setEnabled(true);
                binding.gpsTestStop.setEnabled(true);
                break;
            case 0:
                // 위치 기록 일시중단
                binding.gpsTestButton.setEnabled(true);
                binding.gpsTestPause.setEnabled(false);
                binding.gpsTestStop.setEnabled(false);
                break;
            case -1:
                // 위치 기록 끝난 상태 but path summary 기록 아직 X
                // todo 수정
                binding.gpsTestButton.setEnabled(false);
                binding.gpsTestButton.setText("New Path");
                binding.gpsTestPause.setEnabled(false);
                binding.gpsTestStop.setEnabled(false);
                binding.gpsTestStop.setText("path 마무리 기록");
                break;
            case -2:
                // 현재 진행 중인 path 없음
                binding.gpsTestButton.setEnabled(true);
                binding.gpsTestButton.setText("새로운 Path 생성 및 기록 시작");
                binding.gpsTestPause.setEnabled(false);
                binding.gpsTestStop.setEnabled(false);
                break;
            default:
                binding.gpsTestButton.setEnabled(false);
                binding.gpsTestPause.setEnabled(false);
                binding.gpsTestStop.setEnabled(false);
                break;
        }
    }
}
