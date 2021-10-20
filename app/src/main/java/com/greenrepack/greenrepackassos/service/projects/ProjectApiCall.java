package com.greenrepack.greenrepackassos.service.projects;

import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.Status;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProjectApiCall {
    @POST("/api/association/projets/add")
    Call<Status> create(@Body Projet projet);

    @GET("/api/association/projets/list")
    Call<ResponseData<List<Projet>>> getAllWithIdAssos(@Query(value = "statut") String status, @Query(value = "refassos") String refassos);

    @GET("/api/association/projets/list")
    Call<ResponseData<List<Projet>>> getAllWithRna(@Query(value = "rna") String rna);

    @GET("/api/association/projets/remove")
    Call<Status> removeOne(@Query(value = "idproj") String idproj);
}
