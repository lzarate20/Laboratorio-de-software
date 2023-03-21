package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Coneccion {
    companion object {
        var url = "http://ip172-18-0-3-cgd04d2e69v0008ht6ug-80.direct.labs.play-with-docker.com/#/"

        var api = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}