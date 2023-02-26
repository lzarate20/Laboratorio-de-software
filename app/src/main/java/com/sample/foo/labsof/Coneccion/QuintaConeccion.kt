package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.Service.QuintaService
import com.sample.foo.labsof.Listados.ListQuintas

class QuintaConeccion {
    companion object{
        var api = Coneccion.api.create(QuintaService::class.java)
        suspend fun get(): ListQuintas {
            try {
                val result = api.getQuintas(1)
                if (result.isSuccessful) {
                    return ListQuintas(result.body())
                } else {
                    return ListQuintas("Error al obtener las quintas")
                }
            } catch (e: Exception) {

                return ListQuintas("Error al intentar conectar a la Base de datos")
            }
        }
        suspend fun getSingle(id: Int?): Quinta {
            try {
                val result = api.getSingleQuintas(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return Quinta("Error al obtener la quinta solicitada")
                }
            } catch (e: Exception) {

                return Quinta("Error al intentar conectar a la Base de datos")
            }
        }
        suspend fun post(quinta:Quinta): Quinta?{
            try {
                val result = QuintaConeccion.api.postQuintas(quinta)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun put(quinta:Quinta): Quinta?{
            try {
                val result = QuintaConeccion.api.putQuintas(quinta)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return null
                }
            } catch (e: Exception) {

                return null
            }
        }
        suspend fun delete(id:Int):Quinta?{
            try {
                val result = QuintaConeccion.api.deleteSingleQuintas(id)
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
