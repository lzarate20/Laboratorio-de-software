package com.sample.foo.labsof.DataClass

import com.sample.foo.labsof.helpers.ConversorDate
import java.util.*

data class VisitaFechaList(val parcelas: List<Parcela>?,
                           val id_visita: Int?,
                           val fecha_visita: List<Int>?,
                           val descripcion: String?,
                           val id_tecnico: Int?,
                           val id_quinta: Int?,) {
    fun fechaDate(): Date {
        return ConversorDate.getDate(fecha_visita!!)
    }

    override fun toString(): String {
        return "id: ${(id_visita)} fecha: ${(fechaDate())} tec: ${(id_tecnico)}"
    }
}