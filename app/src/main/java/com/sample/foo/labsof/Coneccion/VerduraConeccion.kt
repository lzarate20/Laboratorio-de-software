package com.sample.foo.labsof.Coneccion

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
                    println(result.code())
                    return null
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return null
            }
        }
        suspend fun getSingle(id: Int?):Verdura? {
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


    }
}