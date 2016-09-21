package com.scsvn.whc_2016.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Trần Xuân Lộc on 1/7/2016.
 */
public class SettingPref {
    private static final String PREF_SETTING = "pref_setting";
    public static final String IP = "ip";
    public static final String POSITION = "position";
    public static final String ACCESS = "access";
    private static SharedPreferences preferences;

    private SettingPref() {

    }

    public static void setInfoNetwork(Context context, String ip, int position) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(IP, ip);
        edit.putString(POSITION, Integer.toString(position));
        edit.apply();
    }

    public static void setAccessLocation(Context context, boolean isAccess) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(ACCESS, isAccess);
        edit.apply();
    }

    public static String[] getInfoNetwork(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        return new String[]{preferences.getString(IP, "192.168.104.29:810"), preferences.getString(POSITION, "0")};
    }

    public static boolean getAccessLocation(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        return preferences.getBoolean(ACCESS, false);
    }
}
