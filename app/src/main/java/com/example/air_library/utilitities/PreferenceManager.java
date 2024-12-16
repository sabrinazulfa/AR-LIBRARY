package com.airlibrary.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "AIRLibraryPrefs";
    
    // Kunci untuk SharedPreferences
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_FIRST_TIME_LAUNCH = "firstTimeLaunch";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_DOWNLOAD_WIFI_ONLY = "downloadWifiOnly";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Metode untuk login
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Metode untuk menyimpan dan mengambil ID pengguna
    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Metode untuk menyimpan dan mengambil email pengguna
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    // Metode untuk peluncuran pertama kali
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    // Metode untuk mode gelap
    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.commit();
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    // Metode untuk download hanya via WiFi
    public void setDownloadWifiOnly(boolean wifiOnly) {
        editor.putBoolean(KEY_DOWNLOAD_WIFI_ONLY, wifiOnly);
        editor.commit();
    }

    public boolean isDownloadWifiOnly() {
        return sharedPreferences.getBoolean(KEY_DOWNLOAD_WIFI_ONLY, false);
    }

    // Metode untuk menghapus semua preferensi (misalnya saat logout)
    public void clear() {
        editor.clear();
        editor.commit();
    }
}