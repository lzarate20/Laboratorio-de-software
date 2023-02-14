package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Coneccion {
    var url= "http://192.168.0.27:80"
    var api= Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}