package com.sample.foo.labsof.DataClass

import java.time.LocalDate
import java.util.*

class Visita(
    val fecha_visita: List<Int>?,
    val descripcion: String?, val id_tecnico: Int?,
    val id_quinta: Int
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
        id_quinta: Int, parcelas: List<Parcela>?
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
            return if (Date(a.fecha_visita!![0],a.fecha_visita!![1],a.fecha_visita!![2]) > Date(b.fecha_visita!![0],b.fecha_visita!![1],b.fecha_visita!![2])) a else b
        }
    }
}