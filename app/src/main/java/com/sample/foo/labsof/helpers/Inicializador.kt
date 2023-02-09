package com.sample.foo.labsof.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.*
import com.sample.foo.labsof.Service.FamiliaProductoraService
import com.sample.foo.labsof.Service.QuintaService
import com.sample.foo.labsof.Service.UserService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class Inicializador {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun cargar(lifecycleScope: LifecycleCoroutineScope) {
            var user: User
            var fp: FamiliaProductora
            var quinta: Quinta
            var api = Coneccion.api.create(UserService::class.java)
            user = createUser()
            lifecycleScope.launch {
                val isEmpty = api.getUsers(1)
                if (isEmpty.isSuccessful && isEmpty.body()?.isEmpty() != false) {
                    val result = api.postUser(user)
                    if (result.isSuccessful) {
                        user = result.body()!!
                    } else {
                        println("Codigo ${result.code()} linea 37")
                        println(result.errorBody())
                    }
                }


            }
            var api2 = Coneccion.api.create(FamiliaProductoraService::class.java)
            fp = createFp()
            lifecycleScope.launch {
                val isEmpty = api2.getFamiliasProductoras(1)
                if (isEmpty.isSuccessful && isEmpty.body()?.isEmpty() == true) {
                    val result = api2.postFamiliasProductoras(fp)
                    if (result.isSuccessful) {
                        fp = result.body()!!
                        var api3 = Coneccion.api.create(QuintaService::class.java)
                        quinta = createQuinta(fp.id_fp!!)
                        val isEmpty = api3.getQuintas(1)
                        if (isEmpty.isSuccessful && isEmpty.body()?.isEmpty() == true) {
                            val result = api3.postQuintas(quinta)
                            if (result.isSuccessful) {
                                quinta = result.body()!!
                            } else {
                                println("Codigo ${result.code()} linea 79")
                                println(result.errorBody())
                            }
                        } else {
                            println("Codigo ${result.code()} linea 58")
                            println(result.errorBody())
                        }
                    }
                }
            }
        }

        fun createUser(): User {
            var aux = RandomString(10)
            return User(aux, aux, aux, aux, aux, aux, 0)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createFp(): FamiliaProductora {
            var aux = RandomString(10)
            return FamiliaProductora(
                aux, ConversorDate.formatDateListInt(LocalDateTime.now())
            )
        }

        fun createQuinta(id_fp: Int): Quinta {
            var aux = RandomString(10)
            return Quinta(aux, aux, aux, id_fp)
        }

        fun createParcela(id_visita: Int): Parcela {
            return Parcela(id_visita)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createVisita(id_tecnico: Int, id_quinta: Int): Visita {
            var aux = RandomString(10)
            return Visita(
                ConversorDate.formatDate(LocalDateTime.now()),
                aux,
                id_tecnico,
                id_quinta
            )
        }

        fun RandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z')
            return (1..length).map { allowedChars.random() }.joinToString("")
        }

        fun RandomAlfaNumerico(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length).map { allowedChars.random() }.joinToString("")
        }
    }
}