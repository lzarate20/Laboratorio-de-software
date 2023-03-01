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
    val mes= arrayOf<String>("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
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
    fun mes_siembra():String{
        return mes.get(mes_siembra!!.get(2) -1)
    }
    fun mes_cosecha():String{
        return mes.get(tiempo_cosecha!!.get(2)-1)
    }
}