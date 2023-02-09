package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Parcela
import retrofit2.Response
import retrofit2.http.*

interface ParcelaService {
    @GET("/api/Parcelas")
    suspend fun getParcelas(@Query("page") page: Long): Response<List<Parcela>>

    @GET("/api/Parcelas/{id}")
    suspend fun getSingleParcelas(@Path("id") id: Long): Response<Parcela>

    @PUT("/api/Parcelas")
    suspend fun putParcelas(@Body parcela:Parcela): Response<Parcela>

    @POST("/api/Parcelas")
    suspend fun postParcelas(@Body parcela: Parcela): Response<Parcela>

    @DELETE("/api/Parcelas/{id}")
    suspend fun deleteSingleParcelas(@Path("id") id: Long): Response<Parcela>
}