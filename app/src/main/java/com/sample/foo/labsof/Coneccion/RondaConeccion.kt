package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.Service.RondaService

class RondaConeccion {
    companion object {
        var api = Coneccion.api.create(RondaService::class.java)

        suspend fun getRondas():List<Ronda>?{
            try{
                val result = this.api.getRonda()
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

        suspend fun postRonda(ronda: Ronda):Ronda?{
            try {
                val result = this.api.postRonda(ronda)
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