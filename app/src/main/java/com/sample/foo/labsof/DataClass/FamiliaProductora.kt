package com.sample.foo.labsof.DataClass

class FamiliaProductora(
) {
    var error: String?=null
    var fecha_afiliacion: List<Int>? = null
    var nombre: String? = null
    var id_fp: Int? = null
    constructor(
        nombre: String?,
        fecha_afiliacion: List<Int>?
    ) : this() {
        this.nombre = nombre
        this.fecha_afiliacion = fecha_afiliacion
    }
    constructor(nombre: String?, fecha_afiliacion: List<Int>?, id_fp: Int?) : this(
        nombre, fecha_afiliacion
    ) {
        this.id_fp = id_fp
    }
    constructor(error:String?):this(){
        this.error=error
    }


}