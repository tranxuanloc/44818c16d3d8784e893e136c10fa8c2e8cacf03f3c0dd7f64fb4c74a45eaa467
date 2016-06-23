package com.scsvn.whc_2016.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class LoginPref {
    private static final String PREF_LOGIN = "pref_login";
    public static final String USERNAME = "username";
    public static final String POSITION_GROUP = "position_group";
    public static final String REAL_NAME = "real_name";
    public static final String WAREHOUSE_ID = "warehouse_id";
    public static final String AUTO_SIGN_OUT = "auto_sign_out";

    private static SharedPreferences preferences;

    private LoginPref() {
    }

    public static void putInfoUser(Context context, String username, String position_group, String real_name, int warehouseID, boolean isAuto) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(USERNAME, username);
        edit.putString(POSITION_GROUP, position_group);
        edit.putString(REAL_NAME, real_name);
        edit.putInt(WAREHOUSE_ID, warehouseID);
        edit.putBoolean(AUTO_SIGN_OUT, isAuto);
        edit.apply();
    }

    public static void resetInfoUserByUser(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(USERNAME, "-1");
        edit.putString(POSITION_GROUP, "-1");
        edit.putString(REAL_NAME, "-1");
        edit.putInt(WAREHOUSE_ID, -1);
        edit.putBoolean(AUTO_SIGN_OUT, true);
        edit.apply();
    }

    public static String getInfoUser(Context context, String key) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(key, "-1");
    }

    public static String getUsername(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(USERNAME, "-1");
    }

    public static String getPositionGroup(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(POSITION_GROUP, "-1");
    }

    public static String getRealName(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(REAL_NAME, "-1");
    }

    public static boolean isAutoSignOut(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getBoolean(AUTO_SIGN_OUT, true);
    }


    public static int getWarehoueID(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getInt(WAREHOUSE_ID, 0);
    }
}