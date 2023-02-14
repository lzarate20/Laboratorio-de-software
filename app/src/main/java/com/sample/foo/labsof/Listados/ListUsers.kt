package com.sample.foo.labsof.Listados

import com.sample.foo.labsof.DataClass.User

class ListUsers() {
    var users: List<User>?=null
    var error:String?=null
    constructor(users: List<User>?):this(){
        this.users=users
    }
    constructor(error: String?):this(){
        this.error=error
    }
    constructor(lista: List<User>,error: String?):this(lista){
        this.error=error
    }

    fun getTecnicos(): ListUsers {
        if (users != null) {
            if (users!!.isEmpty() == true){
                error="No hay tecnicos guardados"
            }
            return ListUsers(users!!.filter {
                it.roles == 1
            }, error)
        }
        return ListUsers(error)
    }
    fun getAdmin(): ListUsers {
        if (users != null) {

            var admin =ListUsers(users!!.filter {
                it.roles ==0
            },error)
            if (admin!!.users!!.isEmpty() == true){
                admin.error="No hay Administradores guardados"
            }
            return admin
        }

        return ListUsers(error)
    }
    fun getById(id:Int):User?{
        return users?.find { u-> u.id_user==id }
    }
    fun getPos(id: Int):Int{
        return users!!.indexOfFirst { u->u.id_user==id }
    }

}