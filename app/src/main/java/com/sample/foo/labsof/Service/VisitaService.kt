package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Visita
import com.sample.foo.labsof.DataClass.VisitaFechaList
import retrofit2.Response
import retrofit2.http.*

interface VisitaService {
    @GET("/api/Visitas")
    suspend fun getVisitas(): Response<List<VisitaFechaList>>

    @GET("/api/Visitas/{id}")
    suspend fun getSingleVisita(@Path("id") id: Int): Response<VisitaFechaList>

    @PUT("/api/visitas")
    suspend fun putVisita(@Body visitas:Visita):Response<VisitaFechaList>

    @POST("/api/visitas")
    suspend fun postVisita(@Body visitas:Visita):Response<VisitaFechaList>

    @DELETE("/api/Visitas/{id}")
    suspend fun deleteSingleVisita(@Path("id") id: Int): Response<Visita>
}