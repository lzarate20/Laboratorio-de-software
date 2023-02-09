package com.sample.foo.labsof.Listados

import com.sample.foo.labsof.DataClass.Quinta

class ListQuintas() {
    var quintas: List<Quinta>?=null
    var error:String?=null
    init {
        if (quintas!= null && quintas?.isEmpty() == true){
            error="No hay quintas guardadas"
        }
    }
    constructor(quintas: List<Quinta>?):this(){
        this.quintas=quintas
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<Quinta>, error: String?):this(lista){
        this.error=error
    }
}