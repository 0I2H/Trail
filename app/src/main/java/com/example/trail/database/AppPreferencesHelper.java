package com.example.trail.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trail.model.user.UserDTO;

import java.util.HashSet;
import java.util.Set;

import static com.example.trail.constants.AppConstants.PREFS_NAME;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_COOKIE_NAME = "PREF_KEY_COOKIE_NAME";
    private final SharedPreferences sharedPreferences;

    public AppPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", false);
        editor.putBoolean("FIRST_EXECUTE", true);


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
    public String getUserID() {
        return sharedPreferences.getString("USER_ID", null);
    }

    public void setUserAuth(UserDTO userDTO) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USER_ID", userDTO.getUserId());
        editor.putString("EMAIL", userDTO.getEmail());
        editor.putString("NICKNAME", userDTO.getUserName());
        editor.putString("USER_IMG", userDTO.getUserImg());
        editor.putString("JOURNEY_TYPE", userDTO.getJourneyType());
        editor.putString("LIFE_STYLE", userDTO.getLifeStyle());
        editor.apply();
    }
}
