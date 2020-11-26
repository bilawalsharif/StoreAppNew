package com.example.chofem.store.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.chofem.store.responses.LoginResponse;

import java.util.HashMap;

public class SharedPref {
    public static final String IS_ALARM_SET = "is_alarm_set";
    public static final String IS_DAILY = "is_daily";
    public static final String IS_MEAL = "is_meal";
    public static final String IS_WEEKLY = "is_weekly";
    public static final String IS_RATEUS_SHOWN = "is_rateus_shown";
    public static final String DAY_NO = "day_no";
    public static final String DB_VERSION = "db_version";
    public static final String KEY_RATEUS_SHOWN = "rateus_shown";
    private static final String PREF_NAME = "SharedPref";
    public static SharedPref sSharedPref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    SharedPreferences pref;

    private SharedPref(Context context) {
        this._context = context.getApplicationContext();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SharedPref getInstance(Context context) {
        if (sSharedPref == null) {
            sSharedPref = new SharedPref(context);
        }
        return sSharedPref;
    }

    public void savePref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void savePref(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void savePref(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void savePref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStringPref(String key, String defaultVal) {
        return pref.getString(key, defaultVal);
    }

    public int getIntPref(String key, int defaultVal) {
        return pref.getInt(key, defaultVal);
    }

    public long getLongPref(String key, int defaultVal) {
        return pref.getLong(key, defaultVal);
    }

    public boolean getBooleanPref(String key, boolean defaultVal) {
        return pref.getBoolean(key, defaultVal);
    }

    public HashMap<String, Boolean> checkSettings() {
        HashMap<String, Boolean> settings = new HashMap<String, Boolean>();
        settings.put(IS_DAILY, pref.getBoolean(IS_DAILY, true));
//        settings.put(IS_WEEKLY, pref.getBoolean(IS_WEEKLY, true));
//        settings.put(IS_MEAL, pref.getBoolean(IS_MEAL, true));
        return settings;
    }

    public void clearStoredData() {
        editor.clear();
        editor.commit();
    }

    public void saveData(LoginResponse value) {
        String storeID=value.getSTORE_ID();
        String first_name = value.getFIRST_NAME();
        String last_name = value.getLAST_NAME();
        String Email = value.getEMAIL();
        String language = value.getLANGUAGE();
        String category = value.getCATEGORY();
        String profile_image = value.getPROFILE_IMAGE();
        String location = value.getLOCATION();
        String latitude = value.getLATITUDE();
        String longitude = value.getLONGITUDE();
        String phone=  value.getPHONE();

        editor.putString("storeID", storeID);
        editor.putString("first_name", first_name);
        editor.putString("last_name", last_name);
        editor.putString("Email", Email);
        editor.putString("language", language);
        editor.putString("category", category);
        editor.putString("profile_image", profile_image);
        editor.putString("location", location);
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.putString("phone", phone);
        editor.commit();
    }
    public LoginResponse getData() {
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setSTORE_ID(pref.getString("storeID", ""));
        loginResponse.setFIRST_NAME(pref.getString("first_name", ""));
        loginResponse.setLAST_NAME(pref.getString("last_name", ""));
        loginResponse.setEMAIL(pref.getString("Email", ""));
        loginResponse.setLANGUAGE(pref.getString("language", ""));
        loginResponse.setCATEGORY(pref.getString("category", ""));
        loginResponse.setPROFILE_IMAGE(pref.getString("profile_image", ""));
        loginResponse.setLOCATION(pref.getString("location", ""));
        loginResponse.setLATITUDE(pref.getString("latitude", ""));
        loginResponse.setLONGITUDE(pref.getString("longitude", ""));
        loginResponse.setPHONE(pref.getString("phone", ""));
        return loginResponse;
    }
    public void setUserName(String userID) {
        editor.putString("userID", userID);
        editor.commit();
    }
    public String getUserName() {
        return pref.getString("userID", "");
    }

    public void setFirstTimeLogin(boolean isnewUser) {
        editor.putBoolean("isnewUser", isnewUser);
        editor.commit();
    }
    public boolean getFirstTimeLogin() {
        return pref.getBoolean("isnewUser", false);
    }
}