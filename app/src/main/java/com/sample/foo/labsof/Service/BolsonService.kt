package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import retrofit2.Response
import retrofit2.http.*

interface BolsonService {
    @GET("/api/bolson")
    suspend fun getBolson(@Query("id_ronda") id_ronda: Int?): Response<List<Bolson>>

    @POST("/api/bolson")
    suspend fun putBolson(@Body bolson:Bolson): Response<Map<String,String>>
}