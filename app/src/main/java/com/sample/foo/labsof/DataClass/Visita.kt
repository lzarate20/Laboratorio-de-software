package com.sample.foo.labsof.DataClass

import java.time.LocalDate

class Visita(
    val fecha_visita: List<Int>?,
    val descripcion: String?, val id_tecnico: Int?,
    val id_quinta: Int?
) {
    var parcelas: List<Parcela>?
    var id_visita:Int?

    init {
        parcelas = null
        id_visita=null
    }

    constructor(
        id_visita: Int?, fecha_visita: List<Int>?,
        descripcion: String?, id_tecnico: Int?,
        id_quinta: Int?, parcelas: List<Parcela>?
    ) : this(
        fecha_visita,
        descripcion, id_tecnico,
        id_quinta
    ){
        this.id_visita=id_visita
        this.parcelas=parcelas
    }
    internal object Compare {
        fun maxDate(a: Visita, b: Visita): Visita {
            return if (LocalDate.parse(a.fecha_visita.toString()) > LocalDate.parse(b.fecha_visita.toString())) a else b
        }
    }
}