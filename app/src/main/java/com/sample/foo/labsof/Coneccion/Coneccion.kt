package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Coneccion {
    companion object {
        var url = "http://ip172-18-0-90-cgb0jbie69v00087t6lg-80.direct.labs.play-with-docker.com"

        var api = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}