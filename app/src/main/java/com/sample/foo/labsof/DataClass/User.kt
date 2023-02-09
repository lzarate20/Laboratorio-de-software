package com.sample.foo.labsof.DataClass

data class User(var error: String?=null) {
    var nombre: String?=null
    var id_user:Int?=null
    var apellido:String?=null
    var direccion:String?=null
    var username:String?=null
    var password:String?=null
    var email:String?=null
    var roles:Int?=null

    constructor(nombre: String?,  apellido: String?,
                 direccion: String?,  username: String?,
                 password: String?, email: String?,
                 roles: Int?
    ):this(){
        this.nombre=nombre
        this.apellido=apellido
        this.direccion=direccion
        this.username=username
        this.password=password
        this.email=email
        this.roles=roles
    }
    constructor(
        nombre: String?, apellido: String?,
        direccion: String?, username: String?,
        password: String?, email: String?,
        roles: Int?, id_user: Int?
    ) : this(nombre, apellido, direccion, username, password, email, roles) {
         this.id_user=id_user
    }
}