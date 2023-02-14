package com.sample.foo.labsof.DataClass

import java.util.Date

data class Ronda(val id_ronda:Int?,val fecha_inicio: List<Int>,val fecha_fin:List<Int>){
    internal object Compare {
        fun maxDate(a: Ronda, b: Ronda): Ronda {
            return if (Date(a.fecha_fin[0],a.fecha_fin[1],a.fecha_fin[2]) >
                Date(b.fecha_fin[0],b.fecha_fin[1],b.fecha_fin[2])) a else b
        }
    }
    companion object {
        fun getRondaActual(listaRondas: List<Ronda>): Ronda {
            return listaRondas.reduce(Ronda.Compare::maxDate)
        }
    }
}