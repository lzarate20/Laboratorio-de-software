package com.sample.foo.labsof.Listados

import com.sample.foo.labsof.DataClass.Verdura


class ListVerduras() {
    var verduras: List<Verdura>?=null
    var error:String?=null
    init {
        if (verduras!= null && verduras?.isEmpty() == true){
            error="No hay verduras guardadas"
        }
    }
    constructor(quintas: List<Verdura>?):this(){
        this.verduras=quintas
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<Verdura>, error: String?):this(lista){
        this.error=error
    }
}