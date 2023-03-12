package com.sample.foo.labsof.Listados

import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.foo.labsof.DataClass.VisitaFechaList
import java.util.*

class ListVisita() {
    var visitas: List<VisitaFechaList>? = null
    var error: String? = null
    init {
        if (visitas!= null && visitas?.isEmpty() == true){
            error = "No hay visitas guardadas"
        }
    }
    constructor(visitas: List<VisitaFechaList>?):this(){
        this.visitas=visitas
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<VisitaFechaList>, error: String?):this(lista){
        this.error=error
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUltimavisita(id_quinta: Int?): VisitaFechaList? {
        if (!this.visitas.isNullOrEmpty()) {
            var visitas_quinta = this.visitas!!.filter { v -> v.id_quinta == id_quinta }
            if (!visitas_quinta.isNullOrEmpty()) {
                return  visitas_quinta.maxBy { it.fechaDate() }

            }
        }
        return null
    }

    fun getVisitasPasadas(): ListVisita {
        return ListVisita(visitas?.filter { v -> v.visitaPasada() })
    }

    fun getVisitasFuturas(): ListVisita {
        return ListVisita(visitas?.filter { v -> !v.visitaPasada() })
    }

    fun ordenarFecha(): ListVisita {
        return ListVisita(visitas?.sortedBy { v -> v.fechaDate() })
    }

    fun ordenartecnico(): ListVisita {
        return ListVisita(visitas?.sortedBy { v -> v.id_tecnico })
    }

    fun ordenarFechaYTecnico(): ListVisita {
        return ListVisita(visitas).ordenartecnico().ordenarFecha()
    }

    override fun toString(): String {
        var text = "["
        visitas?.forEach { v -> text += "\n" + v.toString() }
        text += "\n ]"
        return text
    }

    fun size(): Int {
        if (visitas.isNullOrEmpty()) return 0 else return visitas!!.size
    }

    fun get(pos: Int): VisitaFechaList {
        return visitas!![pos]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun union(tecnicos: ListUsers, quinta: ListQuintas): List<Union> {
        var aux: MutableList<Union> = mutableListOf()
        visitas?.forEach { v ->
            val t = tecnicos.users!!.single { t -> t.id_user == v.id_tecnico }
            val q = quinta.quintas!!.single { q -> q.id_quinta == v.id_quinta }
            aux.add(
                Union(
                    "${(t.nombre!!)} ${(t.apellido!!)}",
                    v.fechaString(),
                    !v.visitaPasada(),
                    v.id_visita!!,
                    q.nombre!!
                )
            )
        }
        return aux
    }

    class Union(
        val nombre: String,
        val fecha: String,
        val futura: Boolean,
        val id: Int,
        val nombre_q: String
    )

}