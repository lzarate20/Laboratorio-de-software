package com.sample.foo.labsof

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.databinding.ActivityListaBolsonesBinding
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListadoBolsonesActivity:AppCompatActivity() {

    lateinit var binding: ActivityListaBolsonesBinding
    private lateinit var adapter: BolsonAdapter
    lateinit var listaBolsones : List<Bolson>
    lateinit var rondaActual: Ronda
    lateinit var listaQuintas:ListQuintas

    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result:ActivityResult? ->
        if(result!!.resultCode == RESULT_OK){
            val dCreate= DialogHelper.espera(this@ListadoBolsonesActivity)
            dCreate.show()
            lifecycleScope.launch {
                var bolson_id = result.data!!.getIntExtra("bolson",-1)
                val bolson = BolsonConeccion.getBolson(bolson_id)!!
                dCreate.dismiss()
                var index = adapter.listaBolsones.indexOfFirst { it.id_bolson==bolson.id_bolson }
                adapter.listaBolsones.removeAt(index)
                adapter.listaBolsones.add(index,bolson)
                adapter.notifyItemChanged(index)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
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
        val dCreate =DialogHelper.espera(this)
        dCreate.show()
        lifecycleScope.launchWhenCreated {
            val rondas = RondaConeccion.getRondas()
            if (rondas != null) {
                rondaActual = Ronda.getRondaActual(rondas)
                val result = BolsonConeccion.getBolsonByRonda(rondaActual.id_ronda)
                listaBolsones = result!!
                listaQuintas = QuintaConeccion.get()
//                val quintas = result!!.flatMap { each -> listaQuintas.quintas!!.filter { it.fpId == each.idFp } }
                initView(listaBolsones, listaQuintas.quintas!!, rondaActual)
            }
            dCreate.dismiss()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun initView(listaB: List<Bolson>, listaQ: List<Quinta>, ronda:Ronda) {
        val recyclerView = binding.recyclerBolsones
        val textView = binding.sinBolsones
        if (listaB.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = BolsonAdapter(listaB.toMutableList(),listaQ.toMutableList(),ronda,{onItemSelected(it)},{deleteItem(it)})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun onItemSelected(bolson: Bolson){
        val intent = Intent(this, EditarBolson::class.java)
        intent.putExtra("bolson",bolson.id_bolson)
        getContent.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteItem(bolson: Bolson){
        val api_bolson = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Esta seguro que desea eliminar el bolson?")
        builder.setPositiveButton("Si"){dialogInterface, which ->
            val dCreate = DialogHelper.espera(this@ListadoBolsonesActivity)
            dCreate.show()
            lifecycleScope.launchWhenCreated{

                var res = api_bolson.deleteSingleBolson(bolson.id_bolson!!)
                dCreate.dismiss()
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