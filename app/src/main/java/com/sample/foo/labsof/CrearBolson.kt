package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doBeforeTextChanged

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.sample.foo.labsof.Adapter.VerduraAdapter
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.*

import com.sample.foo.labsof.Service.*
import com.sample.foo.labsof.databinding.ActivityCrearBolsonBinding

import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CrearBolson : AppCompatActivity() {

    lateinit var binding: ActivityCrearBolsonBinding
    lateinit var  adapter:VerduraAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityCrearBolsonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api_visita = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(VisitaService::class.java)
        val api_quinta = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(QuintaService::class.java)
        val api_verdura = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(VerduraService::class.java)
        val api_ronda = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RondaService::class.java)
        val api_bolson = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BolsonService::class.java)
        val api_parcela = Retrofit.Builder().baseUrl(Coneccion.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ParcelaService::class.java)
        val spinner:Spinner = binding.familiaProductora
        lifecycleScope.launch{
            try {
                val result_ronda = api_ronda.getRonda()
                val result_verduras = api_verdura.getVerdura()
                val result_quinta = api_quinta.getQuintas()
                val result_visitas = api_visita.getVisitas()
                val result_bolson = api_bolson.getBolsonByRonda(null)
                val result_parcelas = api_parcela.getParcela().body()!!
                val id_ultimo = result_bolson.body()!!.reduce(Bolson.Compare::maxId).id_bolson
                if(result_ronda.isSuccessful) {
                    val ronda_actual = Ronda.getRondaActual(api_ronda.getRonda().body()!!)
                    var cantidad_input:Int? = null
                    binding.cantidad.doBeforeTextChanged { text, start, count, after ->
                        cantidad_input = text.toString().toIntOrNull()
                    }
                    initSpinner(spinner, result_quinta.body().orEmpty())
                    initView(result_verduras.body().orEmpty())
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            if(result_visitas.isSuccessful){
                            var visita = Visita.getVisitaById(result_quinta.body()!!.get(position).id_quinta, result_visitas.body().orEmpty())
                            binding.submit.isEnabled = true
                            binding.submit.isClickable = true
                            binding.submit.setBackgroundResource(R.color.green)
                            binding.submit.setOnClickListener {
                                val data = adapter.getData()
                                var verdura: Verdura
                                var lista_verduras = ArrayList<Verdura>()
                                var count_verduras_otro = 0
                                var count_verduras = 0
                                var verdura_en_parcela = true
                                for (each in data.keys) {
                                    verdura = result_verduras.body().orEmpty().get(each)
                                    var id_parcela = visita.parcelas!!.map { it.id_parcela }
                                    var parcela = result_parcelas.filter{each -> id_parcela.any { it == each.id_parcela  }}.any{it.id_verdura == verdura.id_verdura}
                                    if (!parcela) {
                                        // Buscar parcela de otra quinta
                                        var visitasQuintas = Visita.getUltimaVisita(
                                            result_visitas.body()!!,
                                            result_quinta.body()!!
                                        )
                                        id_parcela = visitasQuintas.flatMap { it.parcelas!!.map { it.id_parcela } }
                                        parcela = result_parcelas.filter{each -> id_parcela.any { it == each.id_parcela  }}.any{it.id_verdura == verdura.id_verdura}
                                        if(parcela){
                                            count_verduras_otro += 1
                                        }
                                        else{
                                            verdura_en_parcela = false
                                        }
                                    }
                                    count_verduras +=1
                                    lista_verduras.add(verdura)
                                }
                                var id_fp = result_quinta.body()!!.get(position).fpId
                                if (result_bolson.body()!!.any { it.idFp == id_fp }){
                                    binding.errores.text =
                                        "Ya existe un bolson para dicha familia"
                                    binding.errores.visibility = View.VISIBLE
                                }
                                else if (count_verduras < 1 || count_verduras_otro>2 ) {
                                    binding.errores.text =
                                        "Se deben seleccionar 7 verduras, con al menos 5 de producción propia"
                                    binding.errores.visibility = View.VISIBLE
                                } else if (cantidad_input == null || cantidad_input!! <=0) {
                                    binding.errores.text =
                                        "La cantidad de bolsones debe ser mayor a 0"
                                    binding.errores.visibility = View.VISIBLE
                                }
                                else if(verdura_en_parcela.not()){
                                    binding.errores.text = "Se debe seleccionar una verdura de produccion propia o que haya producido otra familia"
                                    binding.errores.visibility = View.VISIBLE
                                }
                                else {
                                    val bolson = Bolson(
                                        id_ultimo!! + 1,
                                        cantidad_input!!,
                                        idFp = result_quinta.body()!!.get(position).fpId,
                                        idRonda = ronda_actual.id_ronda,
                                        verduras = lista_verduras
                                    )
                                    lifecycleScope.launch { api_bolson.postBolson(bolson) }
                                    finish()
                                }
                            }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            binding.submit.isEnabled = false
                            binding.submit.isClickable = false
                        }
                    }
                }

            }
            catch (e: IOException) {

            }
            catch (e: HttpException){

            }

        }
    }
    fun initSpinner(spinner: Spinner,listQuinta: List<Quinta>){
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listQuinta.map{it.nombre}
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
    fun initView(listaVerdura: List<Verdura>) {
        adapter = VerduraAdapter(listaVerdura)
        binding.recyclerVerduras.layoutManager = LinearLayoutManager(this)
        binding.recyclerVerduras.adapter = adapter
    }





}