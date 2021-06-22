package com.example.trail.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.trail.model.user.UserDTO;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.example.trail.constants.AppConstants.PREFS_NAME;

public class AppPreferencesHelper implements PreferencesHelper {

//   FIXME 설마 Hilt가 private 지원 안하는 것 때문에?
//    (temp) private final SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences;

    private static final String PREF_KEY_COOKIE_NAME = "PREF_KEY_COOKIE_NAME";

    // true: is foreground service / false: service is stopped, or mainactivity is on focus
    private static final String PREF_KEY_LOCATION_SERVICE_FOREGROUND_ENABLED = "PREF_KEY_LOCATION_SERVICE_FOREGROUND_ENABLED";
    // -2: error or 'empty path' / -1: stopped locating / 0: paused locating / 1: locating
    private static final String PREF_KEY_LOCATION_SERVICE_STATE = "PREF_KEY_LOCATION_SERVICE_STATE";

    public AppPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", false);
        editor.putBoolean("FIRST_EXECUTE", true);
        editor.apply();
    }

    @Override
    public Set<String> getCookie() {
        return sharedPreferences.getStringSet(PREF_KEY_COOKIE_NAME, new HashSet<>());
    }

    @Override
    public void setCookie(HashSet<String> cookie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_KEY_COOKIE_NAME, cookie);
        editor.apply();
    }

    @Override
    public int getUserID() {
        return sharedPreferences.getInt("USER_ID", -1);
    }

    public void setUserAuth(UserDTO userDTO) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USER_ID", userDTO.getUserId());
        editor.putString("EMAIL", userDTO.getEmail());  // todo 지우기?
        editor.putString("NICKNAME", userDTO.getUserName());
        editor.putString("USER_IMG", userDTO.getUserImg());
        editor.putString("JOURNEY_TYPE", userDTO.getJourneyType());
        editor.putString("LIFE_STYLE", userDTO.getLifeStyle());
        //todo 'token' 추가? --> cookie에 동일 정보 있긴 함
        editor.apply();
    }

    /**
     * Returns true if requesting location updates, otherwise returns false.
     */
    @Override
    public boolean getLocationTrackingPref() {
        return sharedPreferences.getBoolean(PREF_KEY_LOCATION_SERVICE_FOREGROUND_ENABLED, false);
    }

    /**
     * Returns true if requesting location updates, otherwise returns false.
     */
    @Override
    public int getLocationTrackingState() {
        return sharedPreferences.getInt(PREF_KEY_LOCATION_SERVICE_STATE, -2);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationState The location updates state.
     */
    @Override
    public void saveLocationTrackingPref(int requestingLocationState) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean state = requestingLocationState < 1;
        editor.putBoolean(PREF_KEY_LOCATION_SERVICE_FOREGROUND_ENABLED, state);
        editor.putInt(PREF_KEY_LOCATION_SERVICE_STATE, requestingLocationState);
        editor.apply();

        // https://github.com/android/location-samples/blob/main/LocationUpdatesForegroundService/app/src/main/java/com/google/android/gms/location/sample/locationupdatesforegroundservice/Utils.java
        // 다른 방식의 SharedPreference
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
//                .apply();
    }


//    static String getLocationTitle(Context context) {
//        return context.getString(R.string.location_updated,
//                DateFormat.getDateTimeInstance().format(new Date()));
//    }
}
