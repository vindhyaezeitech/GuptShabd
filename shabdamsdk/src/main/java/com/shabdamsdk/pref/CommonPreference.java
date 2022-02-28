package com.shabdamsdk.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class CommonPreference {
    private static final String SETTINGS_NAME = "shabdam_pref";
    private static CommonPreference sSharedPrefs;
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private final boolean mBulkUpdate = false;

    private CommonPreference(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static CommonPreference getInstance(Context context) {
        if (sSharedPrefs == null) {
            synchronized (CommonPreference.class) {
                if (sSharedPrefs == null) {
                    sSharedPrefs = new CommonPreference(context.getApplicationContext());
                }
            }
        }
        return sSharedPrefs;
    }

    public void put(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void put(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public void put(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public void put(String key, float val) {
        doEdit();
        mEditor.putFloat(key, val);
        doCommit();
    }

    public void put(String key, double val) {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    public void put(String key, long val) {
        doEdit();
        mEditor.putLong(key, val);
        doCommit();
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public Long getLong(String key) {
        return mPref.getLong(key, 5);
    }

    public Long getLongDefaultZero(String key) {
        return mPref.getLong(key, 0);
    }

    public Integer getInteger(String key) {
        return mPref.getInt(key, 0);
    }

    public void remove(String... keys) {
        doEdit();
        for (String key : keys) {
            mEditor.remove(key).commit();
        }
        doCommit();
    }

    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    /*public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }*/

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

    public class Key{
        public static final String USER_ID = "user_id";
        public static final String IS_FIRST_TIME = "is_first_time";
    }

}
