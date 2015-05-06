package com.linkin.mtv.digi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sonnet on 3/22/15.
 */
public class PreferenceManager {

    private Context mContext;
    private SharedPreferences pref;

    public static final String IS_LOGGED_IN = "logged_in";

    public static final String PHONE_NUMBER = "number";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_ID = "user_id";
    public static final String RIGHTS = "rights";
    public static final String PURCHASE = "purchase";
    public static final String VALIDITY = "validity";


    public PreferenceManager(Context context) {
        mContext = context;
        pref = mContext.getSharedPreferences("dgtv", Context.MODE_PRIVATE);
    }


    public void setLoggedIn(boolean flag) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGGED_IN, flag);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }


    public void setPhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    public String getPhoneNumber() {
        return pref.getString(PHONE_NUMBER, null);
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }


    public void setUserID(String id) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ID, id);
        editor.apply();
    }

    public String getUserId() {
        return pref.getString(USER_ID, null);
    }


    public void setHasRights(boolean hasRights) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(RIGHTS, hasRights);
        editor.apply();
    }

    public boolean hasRights() {
        return pref.getBoolean(RIGHTS, false);
    }

    public void setPurchased(boolean purchased) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PURCHASE, purchased);
        editor.apply();
    }

    public boolean hasPurchased() {
        return pref.getBoolean(PURCHASE, false);
    }

    public void setValidity(String date) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(VALIDITY, date);
        editor.apply();
    }


    public String getValidity() {
        return pref.getString(VALIDITY, "");
    }












}
