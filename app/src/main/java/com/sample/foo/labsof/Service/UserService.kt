package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.Auth
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.DataClass.Visita
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("/api/users")
    suspend fun getUsers(@Query("page") page:Long):Response<List<User>>
    @GET("/api/users/{id}")
    suspend fun getSingleUser(@Path("id") id: Int?):Response<User>
    @POST("/api/auth")
    suspend fun  login(@Body user: User):Response<Auth>
    @POST("/api/users")
    suspend fun postUser(@Body user:User):Response<User>
    @PUT("/api/users")
    suspend fun putUser(@Body user:User):Response<User>
    @DELETE("/api/users/{id}")
    suspend fun delete(@Path("id") id: Int): Response<User>


}