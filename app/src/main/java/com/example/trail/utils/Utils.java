package com.example.trail.utils;

import android.location.Location;

public class Utils {

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String locationToText (Location location) {
        return location != null ? "" + '(' + location.getLatitude() + ", " + location.getLongitude() + ')' : "Unknown location";
    }
}
