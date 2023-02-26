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
                    var user = result.body()!!
                    user.password= null
                    return user
                } else {
                    return User("el usuario seleccionado no existe en la base de datos")
                }
            } catch (e: Exception) {

                return User("Error al intentar conectar a la Base de datos")
            }
        }
        suspend fun auth(u:User):User{
            try {
                val result = api.login(u)
                if (result.isSuccessful) {
                    val id=result.body()!!.id_user
                    val user=getSingle(id)
                    if(user.error != null){
                        return User("Hubo problemas al intentar obtener el usuario")
                    }
                    return user
                } else {
                    return User("El usuario o la contraseña son incorrectos")
                }
            } catch (e: Exception) {
                return User("Error al intentar conectar a la Base de datos")
            }
        }

        suspend fun delete(id: Int):Boolean? {
            try {
                val result = api.delete(id)
                if (result.isSuccessful) {
                    return true
                } else {
                    println(result.code())
                    return false
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                return null
            }
        }

    }
}