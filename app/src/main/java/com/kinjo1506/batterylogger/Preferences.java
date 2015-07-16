package com.kinjo1506.batterylogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static final String KEY_LOGGER_START_AT = "logger_start_at";

    private Preferences() {
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static long getLoggerStartAt(Context context, long defaultValue) {
        return getPrefs(context).getLong(KEY_LOGGER_START_AT, defaultValue);
    }

    public static void setLoggerStartAt(Context context, long startTimeInMillis) {
        getPrefs(context).edit()
                .putLong(KEY_LOGGER_START_AT, startTimeInMillis)
                .apply();
    }

    public static void removeLoggerStartAt(Context context) {
        getPrefs(context).edit()
                .remove(KEY_LOGGER_START_AT)
                .apply();
    }
}
