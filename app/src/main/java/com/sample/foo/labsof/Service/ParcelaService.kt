package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Parcela
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParcelaService {
    @GET("/api/Parcelas/{id}")
    suspend fun getParcela(@Path("id") id: Int?): Response<Parcela>

}