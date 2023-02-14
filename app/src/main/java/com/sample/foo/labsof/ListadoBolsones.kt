package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.Service.QuintaService
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
    lateinit var listaBolsones : List<Bolson>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListaBolsonesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        val api_bolson = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val api_ronda = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RondaService::class.java)
        val api_quinta = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(QuintaService::class.java)
        lifecycleScope.launchWhenCreated{
            try {
                val rondaActual = Ronda.getRondaActual(api_ronda.getRonda().body()!!)
                val result = api_bolson.getBolsonByRonda(rondaActual.id_ronda)
                val result_quinta = api_quinta.getQuintas().body()!!
                val quintas = result.body()!!.flatMap { each -> result_quinta.filter{ it.id_quinta==each.idFp } }
                listaBolsones = result.body().orEmpty()
                if (result.isSuccessful) {
                    initView(listaBolsones,quintas)
                }
            }
            catch (e: IOException) {

            }
            catch (e: HttpException){

            }
        }
    }
    fun initView(listaB: List<Bolson>,listaQ: List<Quinta>) {
        val recyclerView = binding.recyclerBolsones
        val textView = binding.sinBolsones
        if (listaB.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = BolsonAdapter(listaB,listaQ,{onItemSelected(it)},{deleteItem(it)})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun onItemSelected(bolson: Bolson){
        val intent = Intent(this, EditarBolson::class.java)
        intent.putExtra("bolson",bolson.id_bolson)
        startActivity(intent)
    }

    fun deleteItem(bolson: Bolson){
        val api_bolson = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Estas seguro que queres eliminar el bolson?")
        builder.setPositiveButton("Si"){dialogInterface, which ->
            lifecycleScope.launchWhenCreated{
                api_bolson.deleteSingleBolson(bolson.id_bolson!!)
                listaBolsones.dropWhile { it.id_bolson == bolson.id_bolson }
                startActivity(intent)
                finish()
            }
            Toast.makeText(applicationContext,"Se ha borrado correctamente",Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}