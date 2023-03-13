package com.sample.foo.labsof.DataClass

class VerduraFechaList() {

    var id_verdura: Int? = null
    var tiempo_cosecha: List<Int>? = null
    var mes_siembra: List<Int>? = null
    var archImg: String? = null
    var nombre: String? = null
    var descripcion: String? = null
    var parcelas: List<String>? = null
    var error: String? = null

    constructor(
        tiempo_cosecha: List<Int>?,
        mes_siembra: List<Int>?, archImg: String?,
        nombre: String?, descripcion: String?,
        parcelas: List<String>?
    ) : this() {
        this.descripcion = descripcion
        this.nombre = nombre
        this.tiempo_cosecha = tiempo_cosecha
        this.mes_siembra = mes_siembra
        this.archImg = archImg
        this.parcelas = parcelas
    }

    constructor(
        id_verdura: Int?, tiempo_cosecha: List<Int>?,
        mes_siembra: List<Int>?, archImg: String?,
        nombre: String?, descripcion: String?,
        parcelas: List<String>?
    ) : this(
        tiempo_cosecha, mes_siembra, archImg, nombre,
        descripcion, parcelas
    ) {
        this.id_verdura = id_verdura
    }

    constructor(e: String?) : this() {
        this.error = e
    }
    fun getMes():Array<String>{
        return arrayOf<String>("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
    }

    fun mes_siembra():String{
        return this.getMes().get(mes_siembra!![1] -1)
    }
    fun mes_cosecha():String{
        return this.getMes().get(tiempo_cosecha!![1] -1)
    }
}