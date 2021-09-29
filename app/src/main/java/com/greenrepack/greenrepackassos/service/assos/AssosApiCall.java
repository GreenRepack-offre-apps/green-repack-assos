package com.greenrepack.greenrepackassos.service.assos;

import com.greenrepack.greenrepackassos.service.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AssosApiCall {
    @GET("/api/association/get")
    Call<ResponseData<AssosGet>> getAssos(@Query(value = "rna") String rna);
}
