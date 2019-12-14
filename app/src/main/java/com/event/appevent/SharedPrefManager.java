package com.event.appevent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.event.appevent.model.User;
import com.google.gson.Gson;

public class SharedPrefManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EventPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // user_session (make variable public to access from outside)
    public static final String KEY_USER = "user_session";

    // Constructor
    public SharedPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing user_session in pref
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);

        // Storing api_token in pref
        //editor.putString(KEY_USER, api_token);

        // commit changes
        editor.commit();

        // After login redirect user to ListEventActivity
        Intent i = new Intent(_context, ListEventActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public User getUserDetails() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER, null);
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Main Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
