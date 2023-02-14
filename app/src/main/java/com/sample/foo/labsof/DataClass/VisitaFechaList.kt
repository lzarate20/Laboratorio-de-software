package com.sample.foo.labsof.DataClass

import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.foo.labsof.helpers.ConversorDate
import java.time.LocalDate
import java.util.*

data class VisitaFechaList(val error: String? = null) {
    var parcelas: List<ParcelaVerdura>? = null
    var id_visita: Int? = null
    var fecha_visita: kotlin.collections.List<Int>? = null
    var descripcion: String? = null
    var id_tecnico: Int? = null
    var id_quinta: Int? = null

    constructor(
        id_visita: Int?, fecha_visita: kotlin.collections.List<Int>?,
        descripcion: String?, id_tecnico: Int?,
        id_quinta: Int?, parcelas: List<ParcelaVerdura>?
    ) : this(
        fecha_visita,
        descripcion, id_tecnico,
        id_quinta
    ) {
        this.id_visita = id_visita
        this.parcelas = parcelas
    }

    constructor(
        fecha_visita: List<Int>?,
        descripcion: String?,
        id_tecnico: Int?,
        id_quinta: Int?
    ) : this() {
        this.fecha_visita = fecha_visita
        this.id_tecnico = id_tecnico
        this.descripcion = descripcion
        this.id_quinta = id_quinta
    }

    fun fechaDate(): Date {
        return ConversorDate.getDate(fecha_visita!!)
    }

    fun fechaString(): String {
        return ConversorDate.convertToInput(fecha_visita!!)
    }

    override fun toString(): String {
        var text = "["
        parcelas?.forEach { v -> text += "\n" + v.id_parcela }
        text += "\n ]"
        return "id: ${(id_visita)} fecha: ${(fechaString())} tecnico: ${(id_tecnico)} parcelas ${(text)}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun esHoy(): Boolean {
        var year: Int
        var month: Int
        var day: Int
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
        return fecha_visita?.get(0)  == year && fecha_visita?.get(1)  == month && fecha_visita?.get(2)  == day
    }
    fun visitaPasada():Boolean{
        return fechaDate().before(Date())
    }
}