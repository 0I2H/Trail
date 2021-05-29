package com.example.trail.database;

import java.util.HashSet;
import java.util.Set;

public interface PreferencesHelper {

    Set<String> getCookie();

    void setCookie(HashSet<String> cookie);

    String getUserID();
}
