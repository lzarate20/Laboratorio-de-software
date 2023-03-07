package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Coneccion.BolsonConeccion
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.databinding.ActivityListaBolsonesBinding
import okhttp3.internal.notifyAll
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListadoBolsonesActivity:AppCompatActivity() {

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
        lifecycleScope.launchWhenCreated {
            val rondas = RondaConeccion.getRondas()
            if (rondas != null) {
                val rondaActual = Ronda.getRondaActual(rondas)
                val result = BolsonConeccion.getBolsonByRonda(rondaActual.id_ronda)
                listaBolsones = result!!
                val result_quinta = QuintaConeccion.get()
                val quintas = result!!.flatMap { each -> result_quinta.quintas!!.filter { it.id_quinta == each.idFp } }
                initView(listaBolsones, quintas, rondaActual)
            }
        }
    }
    fun initView(listaB: List<Bolson>,listaQ: List<Quinta>,ronda:Ronda) {
        val recyclerView = binding.recyclerBolsones
        val textView = binding.sinBolsones
        if (listaB.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = BolsonAdapter(listaB,listaQ,ronda,{onItemSelected(it)},{deleteItem(it)})
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
                var res = api_bolson.deleteSingleBolson(bolson.id_bolson!!)
                if(res.isSuccessful) {
                    listaBolsones.toMutableList().removeIf{ it.id_bolson == bolson.id_bolson }
                    startActivity(intent)
                    finish()
                    Toast.makeText(applicationContext,"Se ha borrado correctamente",Toast.LENGTH_LONG).show()
                }
            }
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}