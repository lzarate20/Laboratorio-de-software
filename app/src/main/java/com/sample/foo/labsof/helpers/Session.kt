package com.sample.foo.labsof.helpers

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.sample.foo.labsof.DataClass.User

class Session(private val activity: Activity) {
    private val sharedPreferences: SharedPreferences = activity.getSharedPreferences("sesion", Context.MODE_PRIVATE)
    var editor= sharedPreferences.edit()

        fun getSession(): User{
            var nombre: String?=sharedPreferences.getString("nombre","")
            var id_user:Int=sharedPreferences.getInt("id",0)
            var apellido:String?=sharedPreferences.getString("apellido","")
            var direccion:String?=sharedPreferences.getString("direccion","")
            var username:String?=sharedPreferences.getString("username","")
            var email:String?=sharedPreferences.getString("email","")
            var roles:Int=sharedPreferences.getInt("roles",2)
            return User( nombre, apellido,
            direccion, username,
            password= null, email,
            roles, id_user)

        }

        fun saveSession(u:User){
            editor.putString("nombre",u.nombre)
            u.id_user?.let { editor.putInt("id", it) }
            editor.putString("apellido",u.apellido)
            editor.putString("direccion",u.direccion)
            editor.putString("username",u.username)
            editor.putString("email",u.email)
            u.roles?.let { editor.putInt("roles", it) }
            editor.commit()
        }
        fun haveSesion():Boolean{
            return this.getSession().nombre != ""
        }

}