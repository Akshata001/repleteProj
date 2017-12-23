package com.optionsmoneymaker.optionsmoneymaker.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.optionsmoneymaker.optionsmoneymaker.LoginActivity;
import com.optionsmoneymaker.optionsmoneymaker.model.LoginResult;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageData;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sagar on 02-10-2016.
 */
public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared Preferences file name
    private String PREF_NAME = "MoneyMaker";

    private String IS_LOGIN = "isLoggedIn";

    private String KEY_FIRST_NAME = "firstName";
    private String KEY_LAST_NAME = "lastName";
    private String KEY_EMAIL = "emailID";
    private String KEY_USER_ID = "userID";
    private String KEY_MOBILE_NO = "mobileNo1";
    private String KEY_USER_PIC = "userPic";
    private String KEY_LEVEL_NAME = "levelName";
    private String FONT_SIZE = "fontSize";
    private String NOTIFICATION_INDEX = "index";
    private String REGISTER_ID = "registerID";
    private String LATEST_MESSAGE = "latestMessage";
    private String IS_FIRST_TIME = "firstTime";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String userId, String mobileNo, String fName, String lName, String levelName, String userPic){

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing all data into shared pref.
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_MOBILE_NO, mobileNo);
        editor.putString(KEY_FIRST_NAME, fName);
        editor.putString(KEY_LAST_NAME, lName);
        editor.putString(KEY_USER_PIC, userPic);
        editor.putString(KEY_LEVEL_NAME, levelName);

        // commit changes
        editor.commit();
    }

    /**
     * Get remembered login credential
     * */
    public LoginResult getLoginCredential(){
        LoginResult loginResult = new LoginResult();

        loginResult.setEmail(pref.getString(KEY_EMAIL, null));
        loginResult.setUserID(pref.getString(KEY_USER_ID, ""));
        loginResult.setMobileNo(pref.getString(KEY_MOBILE_NO, null));
        loginResult.setFirstName(pref.getString(KEY_FIRST_NAME, ""));
        loginResult.setLastName(pref.getString(KEY_LAST_NAME, ""));
        loginResult.setUserPic(pref.getString(KEY_USER_PIC, ""));
        loginResult.setLevelName(pref.getString(KEY_LEVEL_NAME, ""));

        return loginResult;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * To get userID from Shared Pref.
     * @return userId
     */
    public String getUserID(){

        return pref.getString(KEY_USER_ID, "");
    }

    /**
     * To save User Pic to Shared Pref.
     * @param userPic
     */
    public void setUserPic(String userPic){

        // save User Pic
        editor.putString(KEY_USER_PIC, userPic);
        editor.commit();

    }

    /**
     * To get User Pic from Shared Pref.
     * @return UserPic
     */
    public String getUserPic(){

        return pref.getString(KEY_USER_PIC, "");
    }

    /**
     * To get User MobileNo from Shared Pref.
     * @return MobileNo
     */
    public String getMobileNo(){

        return pref.getString(KEY_MOBILE_NO, "");
    }

    /**
     * To save Font Size to Shared Pref.
     * @param fontSize
     */
    public void setFontSize(Integer fontSize){

        editor.putInt(FONT_SIZE, fontSize);
        editor.commit();

    }

    /**
     * To get Font Size from Shared Pref.
     * @return fontSize
     */
    public Integer getFontSize(){

        return pref.getInt(FONT_SIZE, 16);
    }

    /**
     * To save Notification Index to Shared Pref.
     * @param index
     */
    public void setNotiIndex(Integer index){

        editor.putInt(NOTIFICATION_INDEX, index);
        editor.commit();

    }

    /**
     * To get Notification Index from Shared Pref.
     * @return notiIndex
     */
    public Integer getNotiIndex(){

        return pref.getInt(NOTIFICATION_INDEX, 0);
    }

    /**
     * To save Register ID to Shared Pref.
     * @param id
     */
    public void setRegisterID(String id){

        editor.putString(REGISTER_ID, id);
        editor.commit();

    }

    /**
     * To get Register ID from Shared Pref.
     * @return regID
     */
    public String getRegisterID(){

        return pref.getString(REGISTER_ID, "");
    }

    /**
     * To save latest message to Shared Pref.
     * @param list
     */
    public void setLatestMessage(String list){

        editor.putString(LATEST_MESSAGE, list);
        editor.commit();

    }

    /**
     * To get latest message from Shared Pref.
     * @return list
     */
    public MessageResult getLatestMessage(){
        MessageResult result = new MessageResult();
        String msg = pref.getString(LATEST_MESSAGE, "");
        try {
            JSONObject json = new JSONObject(msg);
            Gson gson = new Gson();
            result = gson.fromJson(json.toString(), MessageResult.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void setFirstTime(boolean flag){

        editor.putBoolean(IS_FIRST_TIME, flag);
        editor.commit();

    }

    public boolean getFirstTime(){

        return pref.getBoolean(IS_FIRST_TIME, true);
    }
}
