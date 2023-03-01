package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import retrofit2.Response
import retrofit2.http.*

interface VerduraService {
    @GET("/api/verdura")
    suspend fun getVerdura(): Response<List<VerduraFechaList>>
    @GET("/api/verdura/{id}")
    suspend fun getSingleVerdura(@Path("id") id: Int?): Response<Verdura>
    @DELETE("/api/verdura/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Verdura>

}