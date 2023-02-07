package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuintaService {
    @GET("/api/Quintas")
    suspend fun getQuinta(@Query("id") id: Int?): Response<Quinta>

    @GET("/api/Quintas")
    suspend fun getQuintas(): Response<List<Quinta>>
}