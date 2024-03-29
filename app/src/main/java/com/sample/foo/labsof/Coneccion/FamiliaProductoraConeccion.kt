package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.Service.FamiliaProductoraService

class FamiliaProductoraConeccion {
    companion object {
        var api = Coneccion.api.create(FamiliaProductoraService::class.java)


        suspend fun get(): List<FamiliaProductora>? {
            try {
                val result = FamiliaProductoraConeccion.api.getFamiliasProductoras()
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

        suspend fun getSingle(id: Int): FamiliaProductora? {
            try {
                val result = FamiliaProductoraConeccion.api.getSingleFamiliaProductora(id)
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

        suspend fun post(familiaProductora: FamiliaProductora): FamiliaProductora? {
            try {
                val result = api.postFamiliasProductoras(familiaProductora)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }

        suspend fun put(familiaProductora: FamiliaProductora): FamiliaProductora? {
            try {
                val result = api.putFamiliasProductoras(familiaProductora)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }

        suspend fun delete(id:Int):FamiliaProductora?{
            try {
                val result = FamiliaProductoraConeccion.api.deleteSingleFamiliasProductoras(id)
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