package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Coneccion {
    var url= "http://ip172-19-0-7-cg76fnae69v000cegkbg-80.direct.labs.play-with-docker.com"
    var api= Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}