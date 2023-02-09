package com.sample.foo.labsof.Service

import com.sample.foo.labsof.DataClass.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("/api/users")
    suspend fun getUsers(@Query("page") page:Long):Response<List<User>>
    @GET("/api/users/{id}")
    suspend fun getSingleUser(@Path("id") id: Int?):Response<User>
    @POST("/api/login")
    suspend fun  login(@Body user: User):Response<Map<String, String>>
    @GET("/api/users")
    suspend fun getUserWihtAuth(@Header("autorization") token:String)
    @POST("/api/users")
    suspend fun postUser(@Body user:User):Response<User>
}