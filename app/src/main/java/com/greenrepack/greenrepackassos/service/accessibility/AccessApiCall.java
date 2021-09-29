package com.greenrepack.greenrepackassos.service.accessibility;

import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.Status;
import com.greenrepack.greenrepackassos.service.assos.AssosGet;
import com.greenrepack.greenrepackassos.service.assos.ReponseAssos;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AccessApiCall {
    @POST("/api/association/login")
    Call<ReponseAssos> login(@Body AccessAssosRequest request);
    @POST("api/association/create")
    Call<Status> register(@Body AccessAssosRequest request);
}
