package com.sample.foo.labsof.DataClass

import com.sample.foo.labsof.helpers.ConversorDate
import java.time.LocalDate
import java.util.Date

data class Ronda(val id_ronda:Int?,val fecha_inicio: List<Int>,val fecha_fin:List<Int>){
    internal object Compare {
        fun maxDate(a: Ronda, b: Ronda): Ronda {
            return if (Date(a.fecha_fin[0],a.fecha_fin[1],a.fecha_fin[2]) >
                Date(b.fecha_fin[0],b.fecha_fin[1],b.fecha_fin[2])) a else b
        }
        fun betweenDate(a: Ronda,b: Ronda): Boolean{
            if(Date(a.fecha_fin[0],a.fecha_fin[1],a.fecha_fin[2]) >
                        Date(b.fecha_fin[0],b.fecha_fin[1],b.fecha_fin[2]) &&
                Date(a.fecha_inicio[0],a.fecha_inicio[1],a.fecha_inicio[2]) <
                Date(b.fecha_fin[0],b.fecha_fin[1],b.fecha_fin[2])){
                 return true
            }
            else if(Date(a.fecha_fin[0],a.fecha_fin[1],a.fecha_fin[2]) <
                Date(b.fecha_inicio[0],b.fecha_inicio[1],b.fecha_inicio[2]) &&
                Date(a.fecha_inicio[0],a.fecha_inicio[1],a.fecha_inicio[2]) >
                Date(b.fecha_inicio[0],b.fecha_inicio[1],b.fecha_inicio[2])){
                return true
            }
            else {
                return false
            }
        }

        fun isAfterToday(r:Ronda):Boolean{
            var today = LocalDate.now()
            var day = LocalDate.of(r.fecha_inicio[0],r.fecha_inicio[1],r.fecha_inicio[2])
            return day.isAfter(today)
        }

        fun isBeforeOrEqualToday(r:Ronda):Boolean{
            var today = LocalDate.now()
            var day = LocalDate.of(r.fecha_inicio[0],r.fecha_inicio[1],r.fecha_inicio[2])
            return day.isBefore(today) || day.isEqual(today)
        }
        fun endAfterOrEqualToday(r:Ronda):Boolean{
            var today = LocalDate.now()
            var day = LocalDate.of(r.fecha_fin[0],r.fecha_fin[1],r.fecha_fin[2])
            return day.isAfter(today) || day.isEqual(today)
        }

        fun endBeforeOrEqualToday(r:Ronda):Boolean{
            var today = LocalDate.now()
            var day = LocalDate.of(r.fecha_fin[0],r.fecha_fin[1],r.fecha_fin[2])
            return day.isBefore(today) || day.isEqual(today)
        }

    }
    companion object {
        fun getRondaActual(listaRondas: List<Ronda>): Ronda {
            return listaRondas.first { ConversorDate.getCurrentDate(ConversorDate.convertToInput(it.fecha_inicio)).isBefore(LocalDate.now()) && ConversorDate.getCurrentDate(ConversorDate.convertToInput(it.fecha_fin)).isAfter(LocalDate.now()) }
        }
    }
}