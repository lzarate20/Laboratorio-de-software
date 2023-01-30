package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Visita
import retrofit2.Response
import retrofit2.http.*

interface VisitaServise {
    @GET("/api/Visitas")
    suspend fun getVisitass(@Query("page") page: Long): Response<List<Visita>>

    @GET("/api/Visitas/{id}")
    suspend fun getSingleVisita(@Path("id") id: Long): Response<Visita>

    @PUT("/api/visitas")
    suspend fun putVisita(@Body visitas:Visita):Response<Visita>

    @POST("/api/visitas")
    suspend fun postVisita(@Body visitas:Visita):Response<Visita>

    @DELETE("/api/Visitas/{id}")
    suspend fun deleteSingleVisita(@Path("id") id: Long): Response<Visita>
}