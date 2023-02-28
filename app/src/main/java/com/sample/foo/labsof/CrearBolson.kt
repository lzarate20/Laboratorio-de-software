package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.sample.foo.labsof.Adapter.VerduraAdapter
import com.sample.foo.labsof.Coneccion.BolsonConeccion
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
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
    lateinit var  adapterPropia:VerduraAdapter
    lateinit var  adapterAjena:VerduraAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityCrearBolsonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
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
                val result_quinta = QuintaConeccion.get()
                val result_visitas = VisitaConeccion.get()
                val result_bolson = BolsonConeccion.api.getBolsonByRonda(null)


                if(result_ronda.isSuccessful) {
                    val ronda_actual = Ronda.getRondaActual(api_ronda.getRonda().body()!!)
                    var cantidad_input:Int? = null
                    binding.cantidad.doOnTextChanged{ text, start, count, after ->
                        cantidad_input = text.toString().toIntOrNull()
                    }
                    initSpinner(spinner, result_quinta.quintas!!)
                    var visita = VisitaFechaList.getVisitaById(result_quinta.quintas!!.get(0).id_quinta!!, result_visitas.visitas!!)
                    var listaVerduraPropia =  visita.parcelas!!.map { it.verdura!! }.distinctBy { it.id_verdura }
                    var listaVerduraAjena = ArrayList<VerduraFechaList>()
                    var quintas = result_quinta.quintas!!
                    var visitaAjena:VisitaFechaList
                    for(each in quintas.subList(1,quintas.size-1)){
                        visitaAjena = VisitaFechaList.getVisitaById(each.id_quinta!!,result_visitas.visitas!!)
                        var verduras = visitaAjena.parcelas!!.map{it.verdura}.distinctBy { it!!.id_verdura }
                        verduras.dropWhile{ listaVerduraPropia.any{each -> each.id_verdura == it!!.id_verdura} || listaVerduraAjena.any{each -> each.id_verdura == it!!.id_verdura}}
                        listaVerduraAjena.addAll(verduras.asIterable() as Collection<VerduraFechaList>)
                    }
                    initView(listaVerduraPropia,listaVerduraAjena)

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            if(result_visitas.error !=null){
                                visita = VisitaFechaList.getVisitaById(result_quinta.quintas!!.get(position).id_quinta!!, result_visitas.visitas!!)
                                listaVerduraPropia =  visita.parcelas!!.map { it.verdura!! }
                                listaVerduraAjena = ArrayList<VerduraFechaList>()
                                quintas = result_quinta.quintas!!
                                quintas.toMutableList().removeAt(position)
                                for(each in quintas){
                                    visitaAjena = VisitaFechaList.getVisitaById(each.id_quinta!!,result_visitas.visitas!!)
                                    var verduras = visitaAjena.parcelas!!.map{it.verdura}
                                    verduras.dropWhile{ listaVerduraPropia.any{each -> each.id_verdura == it!!.id_verdura} || listaVerduraAjena.any{each -> each.id_verdura == it!!.id_verdura}}
                                    listaVerduraAjena.addAll(verduras.asIterable() as Collection<VerduraFechaList>)
                                }
                                adapterPropia.set(listaVerduraPropia)
                                adapterAjena.set(listaVerduraAjena)

                                binding.submit.setBackgroundResource(R.color.green)
                                binding.submit.setOnClickListener {
                                val data = adapterPropia.getData()
                                var verdura: VerduraFechaList
                                var lista_verduras = ArrayList<VerduraFechaList>()
                                var count_verduras_otro = 0
                                var count_verduras = 0
                                var verdura_en_parcela = true
                                for (each in data.keys) {
                                    verdura = result_verduras.body().orEmpty().get(each)
                                     var visitasQuintas = VisitaFechaList.getUltimaVisita(
                                            result_visitas.visitas!!,
                                            result_quinta.quintas!!
                                        )


                                    count_verduras +=1
                                    lista_verduras.add(verdura)
                                }
                                var id_fp = result_quinta.quintas!!.get(position).fpId
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
                                        null,
                                        cantidad_input!!,
                                        idFp = result_quinta.quintas!!.get(position).fpId,
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
    fun initView(listaVerduraPropia: List<VerduraFechaList>,listaVerduraAjena:List<VerduraFechaList>) {
        binding.recyclerVerdurasPropia.layoutManager = LinearLayoutManager(this)
        binding.recyclerVerdurasAjena.layoutManager = LinearLayoutManager(this)
        this.adapterPropia = VerduraAdapter(listaVerduraPropia)
        this.adapterAjena = VerduraAdapter(listaVerduraAjena)
        binding.recyclerVerdurasPropia.adapter = adapterPropia
        binding.recyclerVerdurasAjena.adapter = adapterAjena
    }





}