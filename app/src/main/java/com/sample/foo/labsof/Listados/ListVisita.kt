package com.sample.foo.labsof.Listados

import android.os.Build
import androidx.annotation.RequiresApi
import com.sample.foo.labsof.DataClass.Visita
import com.sample.foo.labsof.DataClass.VisitaFechaList
import com.sample.foo.labsof.helpers.ConversorDate
import java.util.*

class ListVisita() {
    var visitas: List<VisitaFechaList>?=null
    var error:String?=null
    init {
        if (visitas!= null && visitas?.isEmpty() == true){
            error="No hay quintas guardadas"
        }
    }
    constructor(visitas: List<VisitaFechaList>?):this(){
        this.visitas=visitas
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<VisitaFechaList>, error: String?):this(lista){
        this.error=error
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getUltimavisita(id_quinta:Int?):Visita{
       var visitas_quinta= visitas?.filter { v-> v.id_quinta== id_quinta }
        if (visitas_quinta != null) {
           var v= visitas_quinta.maxBy { v-> ConversorDate.getCurrentDate(ConversorDate.convertToInput(
                v.fecha_visita!!
            )) }
            return Visita(v)
        }
        return Visita()
    }
    fun getVisitasPasadas():ListVisita{
        return ListVisita(visitas?.filter { v-> v.fechaDate().before( Date()) })
    }
    fun getVisitasFuturas():ListVisita{
        return ListVisita(visitas?.filter { v-> v.fechaDate().after( Date()) })
    }
    fun ordenarFecha():ListVisita{
        return ListVisita(visitas?.sortedBy { v-> v.fechaDate()})
    }
    fun ordenartecnico():ListVisita{
        return ListVisita(visitas?.sortedBy { v-> v.id_tecnico})
    }
    fun ordenarFechaYTecnico():ListVisita{
        return ListVisita(visitas).ordenarFecha().ordenartecnico()
    }

    override fun toString(): String {
        var text= "["
        visitas?.forEach {v-> text += "\n"+ v.toString() }
        text+= "\n ]"
        return text
    }
}