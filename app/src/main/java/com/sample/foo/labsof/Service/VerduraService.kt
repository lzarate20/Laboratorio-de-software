package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Verdura
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VerduraService {
    @GET("/api/verdura/")
    suspend fun getVerdura(): Response<List<Verdura>>
}