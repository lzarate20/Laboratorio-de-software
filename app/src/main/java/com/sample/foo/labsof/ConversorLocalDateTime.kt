package com.sample.foo.labsof

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class ConversorLocalDateTime {
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
    }
}