package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import retrofit2.Response
import retrofit2.http.*

interface ParcelaService {
    @GET("/api/Parcelas")
    suspend fun getParcelas(@Query("page") page: Int?): Response<List<Parcela>>

    @GET("/api/Parcelas/{id}")
    suspend fun getSingleParcelas(@Path("id") id: Int?): Response<Parcela>

    @PUT("/api/Parcelas")
    suspend fun putParcelas(@Body parcela:Parcela): Response<ParcelaVerdura>

    @POST("/api/Parcelas")
    suspend fun postParcelas(@Body parcela: Parcela): Response<ParcelaVerdura>

    @DELETE("/api/Parcelas/{id}")
    suspend fun deleteParcela(@Path("id") id: Int?): Response<ParcelaVerdura>

    @GET("/api/Parcelas")
    suspend fun getParcela(): Response<List<Parcela>>
}