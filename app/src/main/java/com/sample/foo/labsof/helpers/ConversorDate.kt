package com.sample.foo.labsof.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class ConversorDate {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalDateTime(tiempo: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(tiempo), TimeZone.getDefault().toZoneId()
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLong(localDateTime: LocalDateTime): Long {
            val zdt: ZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
            return zdt.toInstant().toEpochMilli()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDate(fecha: String): LocalDate {
            return LocalDate.parse(fecha, formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(year: Int, month: Int, day: Int): String {
            val monthAux: Int = month + 1
            return LocalDate.of(year, monthAux, day).format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(time: LocalDateTime): String {
            var year: Int
            var month: Int
            var day: Int
            time.let { now ->

                year = now.year
                month = now.monthValue
                day = now.dayOfMonth
            }
            return formatDate(year, month - 1, day)
        }
        fun getDate(f:List<Int>):Date{
           return Date(f[0]-1900,f[1]-1,f[2],23,59,59)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDateListInt(time: LocalDateTime): kotlin.collections.List<Int> {
            var year: Int
            var month: Int
            var day: Int
            time.let { now ->

                year = now.year
                month = now.monthValue
                day = now.dayOfMonth
            }
            return listOf<Int>(year, month , day)
        }

        fun convertToBD(fecha:String):String{
            val f = fecha.split("/")
            return "${(f[2])}-${(f[1])}-${(f[0])}"
        }
        fun convertToBD(fecha: List<Int>):String{
            var dia: String="${(fecha[2])}"
            if(fecha[2]/10 < 1) dia= "0${(fecha[2])}"
            var mes: String="${(fecha[1])}"
            if(fecha[1]/10 < 1) mes= "0${(fecha[1])}"
            return "${(fecha[0])}-${(mes)}-${(dia)}"
        }
        fun convertToInput(fecha: List<Int>):String{
            var dia: String="${(fecha[2])}"
            if(fecha[2]/10 < 1) dia= "0${(fecha[2])}"
            var mes: String="${(fecha[1])}"
            if(fecha[1]/10 < 1) mes= "0${(fecha[1])}"
            return "${(dia)}/${(mes)}/${(fecha[0])}"
        }
        fun convertToArray(fecha:String):List<Int>{
            val f = fecha.split("/") as List<Int>
            return f
        }
    }
}