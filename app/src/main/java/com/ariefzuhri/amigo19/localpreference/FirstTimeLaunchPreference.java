package com.ariefzuhri.amigo19.localpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class FirstTimeLaunchPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "first_time_launch_preference";
    private static final String IS_FIRST_TIME_LAUNCH = "is_first_time_launch";

    @SuppressLint("CommitPrefEdits")
    public FirstTimeLaunchPreference(Context context){
        // MODE_PRIVATE -> hanya aplikasi kita yang bisa mengakses
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        // commit() menyimpan secara synchronous
        // apply() menyimpan secara asynchronous, khususnya jika data yang disimpan banyak
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.apply();
    }

    public boolean isFirstTimeLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
