package com.sample.foo.labsof.Coneccion

import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.Service.UserService
import com.sample.foo.labsof.Listados.ListUsers

class UserConeccion {
    companion object {
        val api = Coneccion.api.create(UserService::class.java)
        suspend fun get(): ListUsers {
           println(Coneccion.url)
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
                    val user = result.body()!!
                    return user
                } else {
                    return User("El usuario seleccionado no existe en la base de datos")
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
                return result.isSuccessful
            } catch (e: Exception) {
                return null
            }
        }

        suspend fun put(user: User):User{
            try {
                val u = this.getSingle(user.id_user)
                if (u.error != null) {
                    return User(u.error)
                }
                user.password= u.password
                user.roles= u.roles
                val result = api.putUser(user)
                return if (result.isSuccessful) {
                    result.body()!!
                } else {
                    User("Nose se pudo guardar los cambios")
                }
            } catch (e: Exception) {
                return User("Error al intentar conectar a la Base de datos")
            }
        }
        suspend fun post(user:User): User {
            return try {
                val result = api.postUser(user)
                if (result.isSuccessful) {
                    result.body()!!
                } else {
                    User("No se pudo crear el usuario")
                }
            } catch (e: Exception) {

                User("Error al intentar conectar a la Base de datos")
            }
        }

    }
}