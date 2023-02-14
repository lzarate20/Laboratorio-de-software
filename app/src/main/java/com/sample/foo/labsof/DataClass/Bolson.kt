package com.sample.foo.labsof.DataClass

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class Bolson(val id_bolson:Int?,val cantidad:Int,val idFp:Int?,val idRonda:Int?,val verduras:List<Verdura>?):java.io.Serializable{

    internal object Compare {
        fun maxId(a: Bolson, b: Bolson): Bolson {
            return if (a.id_bolson!! > b.id_bolson!!) a else b
        }
    }
}