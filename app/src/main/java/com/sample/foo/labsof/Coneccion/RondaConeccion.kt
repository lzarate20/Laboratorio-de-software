package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Quinta
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

        suspend fun getRonda(id: Int):Ronda?{
            try{
                val result = this.api.getRondaById(id)
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
        suspend fun putRonda(ronda: Ronda):Ronda?{
            try {
                val result = this.api.putRonda(ronda)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {
                return null
            }
        }
        suspend fun delete(id:Int): Ronda?{
            try {
                val result = this.api.deleteRonda(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    println(result.code())
                    return null
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return null
            }
        }
    }
    }