package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.ListUsers
import com.sample.foo.labsof.Service.UserService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CrearVisita : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_visita)
        val api = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(UserService::class.java)
        lifecycleScope.launch {
            try {
                val result = api.getUsers(1)
                if (result.isSuccessful) {
                    println("Codigo ${result.code()}")
                    val tecnicos = result.body()?.let { ListUsers(it) }
                    println(tecnicos!!.getTecnicos())
                } else {
                    throw HttpException(result)
                    println("Codigo ${result.code()}")
                    println(result.errorBody())
                }
            } catch (e: IOException) {

            }catch (e:HttpException){

            }
        }
        var fecha:EditText= findViewById(R.id.fecha)
    }
}