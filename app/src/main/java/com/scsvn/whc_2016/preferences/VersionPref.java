package com.scsvn.whc_2016.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tranxuanloc on 3/11/2016.
 */
public class VersionPref {
    private static final String PREF_VERSION = "pref_version";
    public static final String VERSION = "version";
    public static final String VERSION_DATE = "version_date";
    private static SharedPreferences preferences;

    public VersionPref() {
    }

    public static void setVersion(Context context, String version) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_VERSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(VERSION, version);
        edit.apply();
    }
    public static void setVersionDate(Context context, String date) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_VERSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(VERSION_DATE, date);
        edit.apply();
    }

    public static String getVersion(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_VERSION, Context.MODE_PRIVATE);
        return preferences.getString(VERSION, "");
    }

    public static String getVersionDate(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_VERSION, Context.MODE_PRIVATE);
        return preferences.getString(VERSION_DATE, "");
    }
}
