package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Quinta
import retrofit2.Response
import retrofit2.http.*

interface QuintaService{
    @GET("/api/Quintas")
    suspend fun getQuintas(@Query("page") page: Long): Response<List<Quinta>>

    @GET("/api/Quintas/{id}")
    suspend fun getSingleQuintas(@Path("id") id: Int?): Response<Quinta>

    @PUT("/api/Quintas")
    suspend fun putQuintas(@Body quinta: Quinta): Response<Quinta>

    @POST("/api/Quintas")
    suspend fun postQuintas(@Body quinta: Quinta): Response<Quinta>

    @DELETE("/api/Quintas/{id}")
    suspend fun deleteSingleQuintas(@Path("id") id: Long): Response<Quinta>

}