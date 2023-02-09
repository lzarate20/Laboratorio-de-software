package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.Service.UserService
import com.sample.foo.labsof.Listados.ListUsers

class UserConeccion {
    companion object {
        var api = Coneccion.api.create(UserService::class.java)
        suspend fun get(): ListUsers {
            try {
                val result = api.getUsers(1)
                if (result.isSuccessful) {
                    return ListUsers(result.body())
                } else {
                    return ListUsers("Error al obtener los usuarios")
                }
            } catch (e: Exception) {

                return ListUsers("Error al intentar conectar a la Base de datos")
            }
        }
        suspend fun getSingle(id: Int?): User {
            try {
                val result = api.getSingleUser(id)
                if (result.isSuccessful) {
                    return result.body()!!
                } else {
                    return User("el usuario seleccionado no existe en la base de datos")
                }
            } catch (e: Exception) {

                return User("Error al intentar conectar a la Base de datos")
            }
        }


    }
}