package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.Service.VerduraService

class VerduraConeccion {
    companion object {
        var api = Coneccion.api.create(VerduraService::class.java)
        suspend fun get(): List<VerduraFechaList>? {
            try {
                val result = api.getVerdura()
                if (result.isSuccessful) {
                    return result.body()
                } else {
                    return null
                }
            } catch (e: Exception) {
                return null
            }
        }
        suspend fun getSingle(id: Int?):VerduraFechaList? {
            try {
                val result = api.getSingleVerdura(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun delete(id: Int):Boolean? {
            return try {
                val result = api.delete(id)
                result.isSuccessful

            } catch (e: Exception) {
                false
            }
        }
        suspend fun post(v:Verdura): VerduraFechaList {
            return try {
                val result =api.postVerdura(v)
                if (result.isSuccessful) {
                    result.body()!!
                } else {
                    VerduraFechaList("No se pudo crear la verdura")
                }
            } catch (e: Exception) {

                VerduraFechaList("Error al intentar conectar a la base de datos")
            }
        }
        suspend fun put(v: Verdura): VerduraFechaList{
            return try {
                val result = api.putverdura(v)
                if (result.isSuccessful) {
                    result.body()!!
                } else {
                    println(result.code())
                    println(result.message())
                    VerduraFechaList("No se pudo guardar los cambios de la verdura")
                }
            } catch (e: Exception) {
                VerduraFechaList("Error al intentar conectar a la base de datos")
            }
        }

    }
}