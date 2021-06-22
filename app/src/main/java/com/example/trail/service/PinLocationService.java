package com.example.trail.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.trail.R;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.utils.Utils;
import com.example.trail.view.dashboard.DashboardActivity;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.example.trail.constants.AppConstants.EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION;
import static com.example.trail.constants.AppConstants.EXTRA_LOCATION;


/** Service class for location tracking
 * code from 'https://codelabs.developers.google.com/codelabs/while-in-use-location/#1'
 * (2021.05.31)
 */
public final class PinLocationService extends Service {

    @Inject
    AppPreferencesHelper appPreferencesHelper;


    /* (별거 아님)
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private boolean configurationChange = false;
    private boolean serviceRunningInForeground = false;
    private LocalBinder localBinder = new LocalBinder();
    private NotificationManager notificationManager;


    // FusedLocationProviderClient - Main class for receiving location updates.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private LocationRequest locationRequest;
    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private LocationCallback locationCallback;
    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private Location currentLocation = null;

    // companion obj.
    public static final String TAG = "PinLocationService";

    public static final int NOTIFICATION_ID = 1111;
    public static final String NOTIFICATION_CHANNEL_ID = "WHILE_IN_USE_CHANNEL";

//    public static final String ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST = "";
//    public static final String EXTRA_LOCATION = "";
//    private static final String EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION = "/"

    public void onCreate() {
        Log.d(TAG, "onCreate() called");

        // FIXME 임시(temp)
        appPreferencesHelper = new AppPreferencesHelper(this);

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(5));      // 5초에 한번
        locationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(5));  // *log 주기*: location data update time (min)
        locationRequest.setMaxWaitTime(TimeUnit.MINUTES.toMillis(2));       // location data update max (wait) time
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).
                /** 사용자 현재 위치 */
                currentLocation = locationResult.getLastLocation();
                // TODO: 이 정보 데이터베이스에 저장하기 (PinsDBControl)
                Toast.makeText(getApplicationContext(), "위도: " + String.valueOf(currentLocation.getLatitude()) + "\n경도: " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                // Notify our Activity that a new location was added. Again, if this was a
                // production app, the Activity would be listening for changes to a database
                // with new locations, but we are simplifying things a bit to focus on just
                // learning the location side of things.
                Intent intent = new Intent(Intent.ACTION_USER_FOREGROUND);
                intent.putExtra(EXTRA_LOCATION, currentLocation);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//          (원래 코틀린 코드)     val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
//                intent.putExtra(EXTRA_LOCATION, currentLocation)
//                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                // Updates notification content if this service is running as a foreground
                // service.
                if (serviceRunningInForeground) {
                    notificationManager.notify(
                            NOTIFICATION_ID,
                            generateNotification(currentLocation));
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");

        boolean cancelLocationTracking = intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false);

        if(cancelLocationTracking) {
            // todo 여기서부터 코딩하면 됨 :)
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "onBind() called");
        this.stopForeground(true);
        this.serviceRunningInForeground = false;
        this.configurationChange = false;
        return this.localBinder;
    }

    public void onRebind(Intent intent) {
        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.
        Log.d(TAG, "onRebind() called");
        this.stopForeground(true);
        this.serviceRunningInForeground = false;
        this.configurationChange = false;
        super.onRebind(intent);
    }

    public boolean onUnbind(Intent intent) {
        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label.
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.
        Log.d(TAG, "onUnbind() called");
        if (!this.configurationChange && appPreferencesHelper.getLocationTrackingPref()) {
            Log.d(TAG, "==== Start foreground service ====");
            Notification notification = generateNotification(currentLocation);
            this.startForeground(NOTIFICATION_ID, notification);
            this.serviceRunningInForeground = true;
        }

        return true;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.configurationChange = true;
    }

    public final void subscribeToLocationUpdates() {
        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        Log.d(TAG, "subscribeToLocationUpdates() called");
        appPreferencesHelper.saveLocationTrackingPref(1);
        this.startService(new Intent(this.getApplicationContext(), PinLocationService.class));

        try {
            /** Subscribe to location changes */
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            appPreferencesHelper.saveLocationTrackingPref(-2);
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. " + unlikely);
        }
    }

    public final void unsubscribeToLocationUpdate() {
        Log.d(TAG, "unsubscribeToLocationUpdate() called");

        try {
            /** Unsubscribe to location changes */
            Task removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            removeTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Location Callback removed.");
                    stopSelf();
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.");
                }
            });
            appPreferencesHelper.saveLocationTrackingPref(0);
        } catch (SecurityException unlikely) {
            appPreferencesHelper.saveLocationTrackingPref(1);
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. " + unlikely);
        }
    }

    /*
     * Generates a BIG_TEXT_STYLE Notification that represent latest location.
     */
    private final Notification generateNotification(Location location) {
        Log.d(TAG, "generateNotification() called");

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data
        // fixme 임시 출력
        String locationData = Utils.locationToText(location);
        String titleText = getString(R.string.app_name);


        // 1. Create Notification Channel for O+ and beyond devices (26+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT);
            // Adds NotificationChannel to system. Attempting to create an
            // existing notification channel with its original values performs
            // no operation, so it's safe to perform the below sequence.
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // 2. Build the notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(locationData)
                .setBigContentTitle(titleText);

        // 3. Set up main Intent/Pending Intents for notification
        Intent launchActivityIntent = new Intent(this, DashboardActivity.class);

        Intent cancelIntent = new Intent(this, PinLocationService.class);
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true);

        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0, launchActivityIntent, 0);

        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this.getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        return notificationCompatBuilder.setStyle(bigTextStyle)
                .setContentTitle(titleText)
                .setContentText(locationData)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_launch, "sdlkfjls(Launch activity)", activityPendingIntent)
                .addAction(R.drawable.ic_cancel, "Stop receiving location updates", servicePendingIntent)
                .build();

    }

    public final class LocalBinder extends Binder {
        public final PinLocationService getService() {
            return PinLocationService.this;
        }
    }
}
