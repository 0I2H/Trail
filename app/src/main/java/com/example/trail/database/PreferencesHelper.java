package com.example.trail.database;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public interface PreferencesHelper {

    Set<String> getCookie();

    void setCookie(HashSet<String> cookie);

    int getUserID();

    boolean getLocationTrackingPref();

    int getLocationTrackingState();

    /**
     * requestingLocationState: 현재 사용자가 설정한 위치기록 상태를 저장
     * 1: logging, 0: paused, -1: stopped, 2: empty
     * 1 만 PREF_KEY_LOCATION_SERVICE_FOREGROUND_ENABLED true, 나머지는 false(i.e. 위치기록 foreground에서도 하지 않음)
     *
     * @param requestingLocationState
     */
    void saveLocationTrackingPref(int requestingLocationState);

}
