package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.Service.ParcelaService

class BolsonConeccion {
    companion object {
        var api = Coneccion.api.create(BolsonService::class.java)

        suspend fun getBolson(id:Int):Bolson?{

            try{
                val result = api.getBolson(id)
                if (result.isSuccessful){
                    return result.body()!!
                }
                else{
                    return null
                }

            }
            catch (e: Exception) {
                return null
            }
        }

        suspend fun getBolsonByRonda(id:Int?):List<Bolson>?{

            try{
                val result = api.getBolsonByRonda(id)
                if (result.isSuccessful){
                    return result.body()!!
                }
                else{
                    return null
                }

            }
            catch (e: Exception) {
                return null
            }
        }

        suspend fun post(bolson:Bolson):Bolson?{
            try {
                val result = this.api.postBolson(bolson)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun put(bolson:Bolson):Bolson?{
            try {
                val result = this.api.putBolson(bolson)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }

    }
}