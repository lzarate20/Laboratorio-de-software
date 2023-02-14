package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Visita
import retrofit2.Response
import retrofit2.http.*

interface BolsonService {
    @GET("/api/bolson/{id}")
    suspend fun getBolson(@Path("id") id: Int): Response<Bolson>

    @GET("/api/bolson")
    suspend fun getBolsonByRonda(@Query("id_ronda") id_ronda: Int?): Response<List<Bolson>>

    @POST("/api/bolson")
    suspend fun postBolson(@Body bolson:Bolson): Response<Map<String,String>>

    @PUT("/api/bolson")
    suspend fun putBolson(@Body bolson:Bolson): Response<Map<String,String>>

    @DELETE("/api/bolson/{id}")
    suspend fun deleteSingleBolson(@Path("id") id: Int): Response<Bolson>
}