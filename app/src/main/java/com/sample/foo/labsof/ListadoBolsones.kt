package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Ronda
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
    private lateinit var adapter: BolsonAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListaBolsonesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api_bolson = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val api_ronda = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RondaService::class.java)
        lifecycleScope.launchWhenCreated{
            try {
                val rondaActual = Ronda.getRondaActual(api_ronda.getRonda().body()!!)
                val result = api_bolson.getBolsonByRonda(rondaActual.id_ronda)
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
            adapter = BolsonAdapter(listaBolsones,{onItemSelected(it)})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun onItemSelected(bolson: Bolson){
        val intent = Intent(this, EditarBolson::class.java)
        intent.putExtra("bolson",bolson.id_bolson)
        startActivity(intent)
    }
}