package com.sample.foo.labsof.DataClass

data class Quinta(val id_quinta:Int,val nombre:String,val direccion:String,val geoImg:String,val fpId:Int?){

    companion object{
        fun findQuintaByFp(lista:List<Quinta>,id:Int?):Quinta?{
            return lista.find { it.fpId?.equals(id) ?:false  }
        }

    }


}

