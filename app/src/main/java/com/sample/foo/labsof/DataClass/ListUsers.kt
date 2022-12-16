package com.sample.foo.labsof.DataClass

class ListUsers(val users:List<User>) {
    fun getTecnicos(): List<User> {
          return users.filter {
              it.roles != 1
          }
      }
    fun getAdmin(): List<User> {
        return users.filter {
            it.roles == 1
        }
    }

}