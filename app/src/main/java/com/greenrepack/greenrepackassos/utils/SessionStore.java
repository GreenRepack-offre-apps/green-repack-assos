package com.greenrepack.greenrepackassos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;


public class SessionStore {

    private static Logger LOGGER = Logger.getLogger("LoginActivity");
    private SharedPreferences sharedPref;

    public SessionStore() {
        this.sharedPref = null;
    }

    public SessionStore(Context context) {
        this.sharedPref = context.getSharedPreferences(AppContextValue.APP_USER_CONTEXT, Context.MODE_PRIVATE);
        LOGGER.log(Level.INFO,"User/App store", "{context <"+AppContextValue.APP_USER_CONTEXT + ">, action => start}");
    }
//    public void init(Context context){
//        SharedPreferences sharedPref = context.getSharedPreferences(ValueUtils.USER_CONTEXT, Context.MODE_PRIVATE);
//        Log.println(Log.INFO,"User/App store", "{context <"+ValueUtils.USER_CONTEXT + ">, action => start}");
//    }

    public void save(String key, String value) {
        //init Menu
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        //init redirectionContexte
        LOGGER.log(Level.INFO,"User/App store", "{key <"+ key + ">, value <"+ value + ">" +
                ", action => save}");
    }

    public void clean(String key) {
        //init Menu
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, "");
        editor.apply();
        //init redirectionContexte
        LOGGER.log(Level.INFO,"User/App store", "{key <"+ key + ">, action => clean}");
    }

    public String get(String key, String defaultVal) {
        return sharedPref.getString(key, defaultVal);
    }

}
