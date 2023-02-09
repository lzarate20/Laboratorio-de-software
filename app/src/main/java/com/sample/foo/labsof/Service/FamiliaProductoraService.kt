package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.FamiliaProductora
import retrofit2.Response
import retrofit2.http.*

interface FamiliaProductoraService {
    @GET("/api/FamiliasProductoras")
    suspend fun getFamiliasProductoras(@Query("page") page: Long): Response<List<FamiliaProductora>>

    @GET("/api/FamiliasProductoras/{id}")
    suspend fun getSingleFamiliaProductora(@Path("id") id: Long): Response<FamiliaProductora>

    @PUT("/api/FamiliasProductoras")
    suspend fun putFamiliasProductoras(@Body familiaProductora: FamiliaProductora): Response<FamiliaProductora>

    @POST("/api/FamiliasProductoras")
    suspend fun postFamiliasProductoras(@Body familiaProductora: FamiliaProductora): Response<FamiliaProductora>

    @DELETE("/api/FamiliasProductoras/{id}")
    suspend fun deleteSingleFamiliasProductoras(@Path("id") id: Long): Response<FamiliaProductora>
}