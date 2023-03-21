package com.sample.foo.labsof.Coneccion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Coneccion {
    companion object {
        var url = "http://ip172-19-0-47-cgch68g1k7jg008sjp8g-80.direct.labs.play-with-docker.com/"

        var api = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}