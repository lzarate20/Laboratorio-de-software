package com.sample.foo.labsof.DataClass

class ParcelaVerdura() {
    var error: String? = null
    var id_visita: Int? = null
    var id_parcela: Int? = null
    var verdura: VerduraFechaList? = null
    var cosecha: Boolean? = null
    var cubierta: Boolean? = null
    var cantidad_surcos: Int? = null

    constructor(id_visita: Int?) : this() {
        this.id_visita = id_visita
    }

    constructor(
        id_visita: Int?, cantidad_surcos: Int?,
        cubierta: Boolean?,
        cosecha: Boolean?,
        verdura: VerduraFechaList?
    ) : this(id_visita) {
        this.cantidad_surcos = cantidad_surcos
        this.cubierta = cubierta
        this.cosecha = cosecha
        this.verdura = verdura
    }

    constructor(
        id_visita: Int?,
        cantidad_surcos: Int?,
        cubierta: Boolean?,
        cosecha: Boolean?,
        verdura: VerduraFechaList?,
        id_parcela: Int?
    ) : this(id_visita, cantidad_surcos, cubierta, cosecha, verdura) {
        this.id_parcela = id_parcela
    }

    constructor(error: String?) : this() {
        this.error = error
    }


    override fun toString(): String {

        return "${(id_parcela)} ${(cantidad_surcos)} ${(verdura?.nombre)}"
    }
}