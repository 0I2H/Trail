package com.example.trail.database;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public interface PreferencesHelper {

    Set<String> getCookie();

    void setCookie(HashSet<String> cookie);

    String getUserID();

    boolean getLocationTrackingPref();

    /**
     * requestingLocationState: 현재 사용자가 설정한 위치기록 상태를 저장
     * 1: logging, 0: paused, -1: stopped, 2: empty
     *
     * @param requestingLocationState
     */
    void saveLocationTrackingPref(int requestingLocationState);
}
