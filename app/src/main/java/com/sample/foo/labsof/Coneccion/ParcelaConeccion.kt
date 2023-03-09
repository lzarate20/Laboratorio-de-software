package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.Service.ParcelaService
import com.sample.foo.labsof.Service.VerduraService

class ParcelaConeccion {
    companion object {
        var api = Coneccion.api.create(ParcelaService::class.java)
        suspend fun get(): List<Parcela>? {
            try {
                val result = api.getParcelas()
                if (result.isSuccessful) {
                    return result.body()
                } else {
                    println(result.code())
                    return null
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return null
            }
        }
        suspend fun getSingle(id: Int?): Parcela? {
            try {
                val result = api.getSingleParcelas(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun post(parcela: Parcela): ParcelaVerdura? {
            try {
                val result = api.postParcelas(parcela)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun put(parcela: Parcela): ParcelaVerdura? {
            try {
                val result = api.putParcelas(parcela)
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
        suspend fun delete(id:Int): Boolean?{
            try {
                val result = api.deleteParcela(id)
                    return result.isSuccessful
            } catch (e: Exception) {
                return null
            }
        }


    }
}