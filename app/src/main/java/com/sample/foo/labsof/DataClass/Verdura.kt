package com.sample.foo.labsof.DataClass

import java.sql.Date

data class Verdura(
    val id_verdura: Int?, val tiempo_cosecha:List<Int>,
    val mes_siembra: List<Int>, val archImg: String?,
    val nombre: String?, val descripcion: String?,
    val parcelas: List<String>?
) {

}