package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Coneccion {
    var url= "http://ip172-18-0-64-cg4di1osf2q000bobsng-80.direct.labs.play-with-docker.com"
    var api= Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}