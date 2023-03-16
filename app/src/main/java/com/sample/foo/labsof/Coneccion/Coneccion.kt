package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Coneccion {
    var url= "http://ip172-18-0-61-cg7kv5osf2q000bv7d2g-80.direct.labs.play-with-docker.com"
    var api= Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}