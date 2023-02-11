package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

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
        val spinner:Spinner = binding.familiaProductora
        lifecycleScope.launch{
            try {
                val result_ronda = api_ronda.getRonda()
                val result_verduras = api_verdura.getVerdura()
                val result_quinta = api_quinta.getQuintas()
                val result_visitas = api_visita.getVisitas()
                if(result_ronda.isSuccessful) {
                    val ronda_actual = Ronda.getRondaActual(api_ronda.getRonda().body()!!)

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
                                for (each in data.keys) {
                                    verdura = result_verduras.body().orEmpty().get(each)
                                    var parcela =
                                        visita.parcelas?.filter { it.id_verdura == verdura.id_verdura }
                                    if (parcela?.isEmpty()!!) {
                                        // Buscar parcela de otra quinta
                                        count_verduras_otro += 1;
                                        var visitasQuintas = Visita.getUltimaVisita(
                                            result_visitas.body()!!,
                                            result_quinta.body()!!
                                        )
                                        //parcela = visitasQuintas.map { it.parcelas?.filter { it.id_verdura == verdura.id_verdura } }
                                    }
                                    lista_verduras.add(verdura)
                                }
                                val bolson = Bolson(
                                    54,
                                    0,
                                    idFp = result_quinta.body()!!.get(position).fpId,
                                    idRonda = ronda_actual.id_ronda,
                                    verduras = lista_verduras
                                )
                                lifecycleScope.launch { api_bolson.postBolson(bolson) }
                                finish()
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