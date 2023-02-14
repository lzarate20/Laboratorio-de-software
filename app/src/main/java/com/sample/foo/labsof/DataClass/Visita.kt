package com.sample.foo.labsof.DataClass

import com.sample.foo.labsof.helpers.ConversorDate

class Visita(val error:String?=null) {
    var parcelas: List<ParcelaVerdura>? = null
    var id_visita: Int? = null
    var fecha_visita: String? = null
    var descripcion: String? = null
    var id_tecnico: Int? = null
    var id_quinta: Int? = null

    constructor(
        id_visita: Int?, fecha_visita: String?,
        descripcion: String?, id_tecnico: Int?,
        id_quinta: Int?, parcelas: List<ParcelaVerdura>?
    ) : this(
        fecha_visita,
        descripcion, id_tecnico,
        id_quinta
    ) {
        this.id_visita = id_visita
        this.parcelas = parcelas
    }

    constructor(
        fecha_visita: String?,
        descripcion: String?,
        id_tecnico: Int?,
        id_quinta: Int?
    ) : this() {
        this.fecha_visita=fecha_visita
        this.id_tecnico=id_tecnico
        this.descripcion=descripcion
        this.id_quinta=id_quinta
    }
    constructor(v: VisitaFechaList):this(){
        val f=v.fecha_visita
        this.fecha_visita=ConversorDate.convertToBD(v.fecha_visita!!)
        this.id_tecnico=v.id_tecnico
        this.descripcion=v.descripcion
        this.id_quinta=v.id_quinta
        this.id_visita = v.id_visita
        this.parcelas = v.parcelas
    }


    override fun toString():String{
        return "${(id_quinta)} ${(fecha_visita)} ${(id_tecnico)} ${(descripcion)}"
    }
}