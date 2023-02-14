package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Ronda
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RondaService {
    @GET("/api/rondas")
    suspend fun getRonda(): Response<List<Ronda>>

    @GET("/api/rondas/{id}")
    suspend fun getRondaById(@Path("id") id: Int?): Response<Ronda>
}