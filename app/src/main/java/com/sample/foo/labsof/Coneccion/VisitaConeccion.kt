package com.sample.foo.labsof.Coneccion

import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.foo.labsof.DataClass.Visita
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.Service.VisitaService

class VisitaConeccion {
    companion object{
        var api = Coneccion.api.create(VisitaService::class.java)
        suspend fun get(): ListVisita {
            try {
                val result = api.getVisitas(1)
                if (result.isSuccessful) {
                    return ListVisita(result.body())
                } else {
                    return ListVisita("Error al obtener las visitas")
                }
            } catch (e: Exception) {

                return ListVisita("Error al intentar conectar a la Base de datos")
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun post(visita: Visita): Visita {
            try {
                val tecnico= UserConeccion.getSingle(visita.id_tecnico)
                val quinta= QuintaConeccion.getSingle(visita.id_quinta)
                if(tecnico.error != null){
                    return Visita(tecnico.error)
                }
                if (quinta.error!= null){
                    return Visita(quinta.error)
                }
                val visitas= this.get()
                if(tecnico.error != null){
                    visita.parcelas= visitas.getUltimavisita(quinta.id_quinta).parcelas
                }
                val result = api.postVisita(visita)
                if (result.isSuccessful) {
                    return Visita(result.body()!!)
                }else{
                    return Visita("Nose se pudo guardar la visita")
                }
            } catch (e: Exception) {
                return Visita("Error al intentar conectar a la Base de datos")
            }
        }
    }
}