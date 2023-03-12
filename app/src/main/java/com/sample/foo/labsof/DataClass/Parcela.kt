package com.sample.foo.labsof.DataClass

class Parcela() {
    var error: String?=null
    var id_visita: Int?=null
    var id_parcela: Int?= null
    var id_verdura: Int? = null
    var cosecha: Boolean? = null
    var cubierta: Boolean? = null
    var cantidad_surcos: Int? = null
    constructor(id_visita: Int?):this(){
        this.id_visita=id_visita
    }
    constructor(
        id_visita: Int?, cantidad_surcos: Int?,
        cubierta: Boolean?,
        cosecha: Boolean?,
        id_verdura: Int?
    ) : this(id_visita) {
        this.cantidad_surcos = cantidad_surcos
        this.cubierta = cubierta
        this.cosecha = cosecha
        this.id_verdura = id_verdura
    }
    constructor(
        id_visita: Int?, cantidad_surcos: Int?,
        cubierta: Boolean?,
        cosecha: Boolean?,
        id_verdura: Int?,id_parcela:Int?
    ) : this(id_visita,cantidad_surcos, cubierta, cosecha, id_verdura) {
        this.id_parcela= id_parcela
    }
    constructor(error:String?):this(){
        this.error=error
    }
    constructor(p:ParcelaVerdura):this(){
        this.cantidad_surcos = p.cantidad_surcos
        this.cubierta = p.cubierta
        this.cosecha = p.cosecha
        this.id_verdura = p.verdura!!.id_verdura
        this.id_parcela= p.id_parcela
        this.id_visita= p.id_visita
    }

    override fun toString(): String {
        return "surcos:${(this.cantidad_surcos)}" +
                " cubierta ${(this.cubierta)} " +
                "cosecha ${(this.cosecha)} " +
                "id verdura ${(this.id_verdura)} " +
                "id parcela ${(this.id_parcela)} " +
                "id visita ${(this.id_visita)}"
    }


}