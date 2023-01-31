package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import retrofit2.Response
import retrofit2.http.*

interface BolsonService {
    @GET("/api/Bolson")
    suspend fun getBolson(): Response<List<Bolson>>
}