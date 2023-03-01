package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import retrofit2.Response
import retrofit2.http.*

interface RondaService {
    @GET("/api/rondas")
    suspend fun getRonda(): Response<List<Ronda>>

    @GET("/api/rondas/{id}")
    suspend fun getRondaById(@Path("id") id: Int?): Response<Ronda>

    @POST("/api/rondas/")
    suspend fun postRonda(@Body ronda:Ronda): Response<Ronda>

    @DELETE("/api/rondas/{id}")
    suspend fun deleteRonda(@Path("id") id: Int?): Response<Ronda>
}