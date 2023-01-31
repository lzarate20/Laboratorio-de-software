package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.Service.BolsonService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ListadoBolsones:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val api = Retrofit.Builder().baseUrl(Coneccion.url.plus("/bolson"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        lifecycleScope.launch{
            try {
                val result = api.getBolson()
                if(result.isSuccessful){
                    initView(result.body().orEmpty())
                }
            }
            catch (e: IOException) {

            }
            catch (e: HttpException){

            }

        }
    }
    fun initView(listaBolsones: List<Bolson>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerBolsones)
        val textView = findViewById<TextView>(R.id.sin_bolsones)
        if (listaBolsones.isEmpty()) {
            recyclerView.setVisibility(View.GONE)
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.setVisibility(View.VISIBLE)
            textView.setVisibility(View.GONE)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BolsonAdapter(listaBolsones)
        }
    }
}