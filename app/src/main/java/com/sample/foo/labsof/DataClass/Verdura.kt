package com.sample.foo.labsof.DataClass

import com.sample.foo.labsof.helpers.ConversorDate

class Verdura() {

    var id_verdura: Int?= null
    var tiempo_cosecha: String?= null
    var mes_siembra: String?= null
    var archImg: String?= null
    var nombre: String?= null
    var descripcion: String?= null
    var parcelas: List<String>?= null

    constructor(
        id_verdura: Int?, tiempo_cosecha: String?,
         mes_siembra: String?,  archImg: String?,
        nombre: String?,  descripcion: String?,
        parcelas: List<String>?
    ) : this(){
       this.id_verdura=id_verdura
        this.descripcion= descripcion
        this.nombre= nombre
        this.tiempo_cosecha=tiempo_cosecha
        this.mes_siembra= mes_siembra
        this.archImg= archImg
        this.parcelas= parcelas
    }
    constructor(v:VerduraFechaList):this(){
        this.id_verdura=v.id_verdura
        this.descripcion= v.descripcion
        this.nombre= v.nombre
        this.tiempo_cosecha=ConversorDate.convertToInput(v.tiempo_cosecha!!)
        this.mes_siembra= ConversorDate.convertToInput(v.mes_siembra!!)
        this.archImg= v.archImg
        this.parcelas= v.parcelas
    }
}