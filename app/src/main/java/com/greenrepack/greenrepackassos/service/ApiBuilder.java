package com.greenrepack.greenrepackassos.service;

import android.content.Context;
import android.content.res.Resources;

import com.greenrepack.greenrepackassos.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    public static Retrofit builder(Context context){
        String url = context.getString(R.string.api);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
