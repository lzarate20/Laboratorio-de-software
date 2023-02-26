package com.sample.foo.labsof.DataClass

data class User(var error: String? = null) {
    var nombre: String? = null
    var id_user: Int? = null
    var apellido: String? = null
    var direccion: String? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var roles: Int? = null

    constructor(
        nombre: String?, apellido: String?,
        direccion: String?, username: String?,
        password: String?, email: String?,
        roles: Int?
    ) : this(username,password) {
        this.nombre = nombre
        this.apellido = apellido
        this.direccion = direccion
        this.email = email
        this.roles = roles
    }

    constructor(
        username: String?,
        password: String?
    ) : this() {
        this.username = username
        this.password = password
    }

    constructor(
        nombre: String?, apellido: String?,
        direccion: String?, username: String?,
        password: String?, email: String?,
        roles: Int?, id_user: Int?
    ) : this(nombre, apellido, direccion, username, password, email, roles) {
        this.id_user = id_user
    }

    fun isTecnico(): Boolean {
        return roles == 1
    }
    fun rol():String{
        return if (isTecnico()) "Técnico" else "Administrador"
    }
}