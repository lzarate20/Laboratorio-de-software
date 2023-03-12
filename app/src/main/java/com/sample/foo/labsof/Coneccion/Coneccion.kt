package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Coneccion {
    var url= "http://ip172-18-0-40-cg72qao1k7jg00b0f38g-80.direct.labs.play-with-docker.com"
    var api= Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}