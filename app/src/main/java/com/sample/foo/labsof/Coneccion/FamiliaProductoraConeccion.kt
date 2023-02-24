package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.Service.FamiliaProductoraService

class FamiliaProductoraConeccion {
    companion object {
        var api = Coneccion.api.create(FamiliaProductoraService::class.java)

        suspend fun post(familiaProductora: FamiliaProductora):FamiliaProductora?{
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
    }

    }