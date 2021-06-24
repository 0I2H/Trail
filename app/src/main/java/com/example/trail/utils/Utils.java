package com.example.trail.utils;

import android.location.Location;

import com.google.gson.Gson;

public class Utils {

    /**
     * Returns the {@code object} object as a JSON object.
     * @param object  The {@link Object}.
     */
    public static String jsonConverter(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String locationToText (Location location) {
        return location != null ? "" + '(' + location.getLatitude() + ", " + location.getLongitude() + ')' : "Unknown location";
    }
}
