package com.greenrepack.greenrepackassos.utils;

import com.google.gson.Gson;

public class AppContextValue {
    public static final String APP_USER_CONTEXT = "ASSOCIATION";
    public static final String APP_ERROR_MSG = "L'application est indisponible pour le moment !! ";
    public static final String SERVICE_ERROR_MSG = "Le service ne répond pas pour le moment !! ";

    public static String jsonString(Object obj){
        try{
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            return json;
        } catch (Exception e) {
            return null;
        }
    }
}
