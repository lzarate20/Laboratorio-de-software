package com.sample.foo.labsof.DataClass

import android.os.Parcel
import android.os.Parcelable



data class Bolson(val id_bolson:Int?,val cantidad:Int,val idFp:Int?,val idRonda:Int?,val verduras:List<Verdura>?):java.io.Serializable{
}