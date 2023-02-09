package com.sample.foo.labsof.DataClass

class Quinta() {
    var geoImg: String?=null
    var direccion:String?=null
    var nombre:String?=null
    var error:String?=null
    var fpId: Int? = null
    var id_quinta: Int? = null

    constructor(error:String?):this(){
        this.error=error
    }

    constructor(
        nombre: String?, direccion: String?,
        geoImg: String?, fpId: Int?
    ) : this() {
        this.nombre = nombre
        this.direccion = direccion
        this.geoImg = geoImg
        this.fpId = fpId
    }

    constructor(
        nombre: String?, direccion: String?,
        geoImg: String?, fpId: Int?, id_quinta: Int?
    ) : this(
        nombre, direccion, geoImg, fpId
    ) {
        this.id_quinta = id_quinta
    }
}