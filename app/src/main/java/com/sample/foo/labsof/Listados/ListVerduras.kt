package com.sample.foo.labsof.Listados

import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList


class ListVerduras() {
    var verduras: List<VerduraFechaList>?=null
    var error:String?=null
    init {
        if (verduras!= null && verduras?.isEmpty() == true){
            error="No hay verduras guardadas"
        }
    }
    constructor(verduras: List<VerduraFechaList>?):this(){
        this.verduras=verduras
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<VerduraFechaList>, error: String?):this(lista){
        this.error=error
    }
    fun getById(id:Int): VerduraFechaList?{
        return verduras?.find { v-> v.id_verdura==id }
    }
    fun getPos(id: Int):Int{
        return verduras!!.indexOfFirst { v->v.id_verdura==id }
    }
    fun size():Int{
        return verduras!!.size
    }
}