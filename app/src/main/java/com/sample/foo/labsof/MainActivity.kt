package com.sample.foo.labsof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.Service.UserService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()

        val menu_inicio: Fragment = Menu()
        FT.add(R.id.inicio_menu,menu_inicio)

        FT.commit()

        val api = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(UserService::class.java)
        lifecycleScope.launch {
            val result = api.getUsers(1)
            if(result.isSuccessful) {
                println("Codigo ${result.code()}")
                println(result.body())
            } else{
                println("Codigo ${result.code()}")
                println(result.errorBody())
            }
        }


    }
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

}