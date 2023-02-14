package com.sample.foo.labsof.Coneccion

import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.Visita
import com.sample.foo.labsof.DataClass.VisitaFechaList
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

        suspend fun getSingle(id: Int): VisitaFechaList {
            try {
                val result = api.getSingleVisita(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return VisitaFechaList("Error al obtener las visitas")
                }
            } catch (e: Exception) {
                return VisitaFechaList("Error al intentar conectar a la Base de datos")
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun post(visita: Visita): Visita {
            try {
                val tecnico = UserConeccion.getSingle(visita.id_tecnico)
                val quinta = QuintaConeccion.getSingle(visita.id_quinta)
                if (tecnico.error != null) {
                    return Visita(tecnico.error)
                }
                if (quinta.error != null) {
                    return Visita(quinta.error)
                }

                val visitas = this.get()

                if (visitas.error == null) {
                    println("1")
                    val visitas_pasadas = visitas.getVisitasPasadas()
                    var parcelas: List<ParcelaVerdura>? = null
                    if (visitas_pasadas.size() != 0) {
                        val ult_v = visitas_pasadas.getUltimavisita(quinta.id_quinta)
                        if (ult_v != null) {
                            parcelas = ult_v.parcelas
                            if (parcelas!= null) {
                                parcelas.forEach { p ->
                                    println(p.toString())
                                    p.id_parcela = null
                                }
                            }
                        }
                    }

                    visita.parcelas = parcelas
                } else println("error " + visitas.error)
                val result = api.postVisita(visita)
                if (result.isSuccessful) {
                    return Visita(result.body()!!)
                } else {
                    return Visita("Nose se pudo guardar la visita")
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return Visita("Error al intentar conectar a la Base de datos")
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun put(visita: Visita): Visita {
            try {
                val tecnico = UserConeccion.getSingle(visita.id_tecnico)
                val quinta = QuintaConeccion.getSingle(visita.id_quinta)
                if (tecnico.error != null) {
                    return Visita(tecnico.error)
                }
                if (quinta.error != null) {
                    return Visita(quinta.error)
                }
                val result = api.putVisita(visita)
                if (result.isSuccessful) {
                    return Visita(result.body()!!)
                } else {

                    println(result.code())
                    println(result.errorBody())
                    return Visita("Nose se pudo guardar la visita")
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return Visita("Error al intentar conectar a la Base de datos")
            }
        }
    }
}