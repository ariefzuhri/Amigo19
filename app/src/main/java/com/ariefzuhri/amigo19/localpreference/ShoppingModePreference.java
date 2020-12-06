package com.ariefzuhri.amigo19.localpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ShoppingModePreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "shopping_mode_preference";
    private static final String IS_SHOPPING_MODE = "is_shopping_mode";
    private static final String STEP_COUNT = "step_count";

    private static final String IS_CB1_CHECKED = "is_cb1_checked";
    private static final String IS_CB2_CHECKED = "is_cb2_checked";
    private static final String IS_CB3_CHECKED = "is_cb3_checked";
    private static final String IS_CB4_CHECKED = "is_cb4_checked";
    private static final String IS_CB5_CHECKED = "is_cb5_checked";

    @SuppressLint("CommitPrefEdits")
    public ShoppingModePreference(Context context){
        // MODE_PRIVATE -> hanya aplikasi kita yang bisa mengakses
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsShoppingMode(boolean isShoppingMode){
        // commit() menyimpan secara synchronous
        // apply() menyimpan secara asynchronous, khususnya jika data yang disimpan banyak
        editor.putBoolean(IS_SHOPPING_MODE, isShoppingMode);
        editor.apply();
    }

    public boolean isShoppingMode(){
        return sharedPreferences.getBoolean(IS_SHOPPING_MODE, false);
    }

    public void setStepCountShoppingMode(int stepCount){
        editor.putInt(STEP_COUNT, stepCount);
        editor.apply();
    }

    public int getStepCount(){
        return sharedPreferences.getInt(STEP_COUNT, 1);
    }

    public void setIsCb1Checked(boolean isChecked){
        editor.putBoolean(IS_CB1_CHECKED, isChecked);
        editor.apply();
    }

    public boolean isCb1Checked(){
        return sharedPreferences.getBoolean(IS_CB1_CHECKED, false);
    }

    public void setIsCb2Checked(boolean isChecked){
        editor.putBoolean(IS_CB2_CHECKED, isChecked);
        editor.apply();
    }

    public boolean isCb2Checked(){
        return sharedPreferences.getBoolean(IS_CB2_CHECKED, false);
    }

    public void setIsCb3Checked(boolean isChecked){
        editor.putBoolean(IS_CB3_CHECKED, isChecked);
        editor.apply();
    }

    public boolean isCb3Checked(){
        return sharedPreferences.getBoolean(IS_CB3_CHECKED, false);
    }

    public void setIsCb4Checked(boolean isChecked){
        editor.putBoolean(IS_CB4_CHECKED, isChecked);
        editor.apply();
    }

    public boolean isCb4Checked(){
        return sharedPreferences.getBoolean(IS_CB4_CHECKED, false);
    }

    public void setIsCb5Checked(boolean isChecked){
        editor.putBoolean(IS_CB5_CHECKED, isChecked);
        editor.apply();
    }

    public boolean isCb5Checked(){
        return sharedPreferences.getBoolean(IS_CB5_CHECKED, false);
    }
}