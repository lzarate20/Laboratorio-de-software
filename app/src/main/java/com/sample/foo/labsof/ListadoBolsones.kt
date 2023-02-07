package com.sample.foo.labsof

import android.os.Bundle
import android.util.Log
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
import com.sample.foo.labsof.Service.RondaService
import com.sample.foo.labsof.databinding.ActivityCrearBolsonBinding
import com.sample.foo.labsof.databinding.ActivityListaBolsonesBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ListadoBolsones:AppCompatActivity() {

    lateinit var binding: ActivityListaBolsonesBinding


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListaBolsonesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val api_ronda = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RondaService::class.java)
        lifecycleScope.launchWhenCreated{
            try {
                val result = api.getBolson(1)
                if (result.isSuccessful) {
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
        val recyclerView = binding.recyclerBolsones
        val textView = binding.sinBolsones
        if (listaBolsones.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BolsonAdapter(listaBolsones)
            textView.setVisibility(View.GONE)
        }
    }
}