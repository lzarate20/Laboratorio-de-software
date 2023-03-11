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
import com.sample.foo.labsof.Coneccion.*
import com.sample.foo.labsof.DataClass.*

import com.sample.foo.labsof.Service.*
import com.sample.foo.labsof.databinding.ActivityCrearBolsonBinding

import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CrearBolson : AppCompatActivity() {

    lateinit var binding: ActivityCrearBolsonBinding
    lateinit var  adapterPropia:VerduraAdapter
    lateinit var  adapterAjena:VerduraAdapter
    lateinit var adapterSpinner:ArrayAdapter<String?>

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

        val spinner:Spinner = binding.familiaProductora
        lifecycleScope.launch{
                val result_ronda = RondaConeccion.getRondas()
                val result_visitas = VisitaConeccion.get().getVisitasPasadas()
                val result_quinta = QuintaConeccion.get()
                val result_verduras = VerduraConeccion.get()
                val result_bolson = BolsonConeccion.getBolsonByRonda(null)


                if(result_ronda != null) {
                    val ronda_actual = Ronda.getRondaActual(result_ronda)
                    var cantidad_input:Int? = null
                    binding.cantidad.doOnTextChanged{ text, start, count, after ->
                        cantidad_input = text.toString().toIntOrNull()
                    }
                    var listaQuintas = result_quinta.quintas!!.filter { result_visitas.getUltimavisita(it.id_quinta) != null }
                    initSpinner(spinner, listaQuintas)
                    var id_quinta = result_quinta.quintas!!.first().id_quinta
                    var visita = result_visitas.getUltimavisita(id_quinta)
                    var listaVerduraPropia =  visita!!.parcelas!!.distinctBy { it.verdura!!.id_verdura }
                    var listaVerduraAjena = ArrayList<ParcelaVerdura>()
                    var quintas = result_quinta.quintas!!
                    var visitaAjena:VisitaFechaList
                    for(each in quintas.subList(1,quintas.size-1)){
                        visitaAjena = VisitaFechaList.getVisitaById(each.id_quinta!!,result_visitas.visitas!!)
                        var verduras = visitaAjena.parcelas!!.map{it.verdura}.filter { each -> listaVerduraPropia.all { it.verdura!!.id_verdura != each!!.id_verdura } && listaVerduraAjena.all{ it.verdura!!.id_verdura != each!!.id_verdura}}
                        listaVerduraAjena.addAll(verduras.asIterable() as Collection<ParcelaVerdura>)
                    }

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                                visita = result_visitas.getUltimavisita(result_quinta.quintas!!.get(position).id_quinta!!)!!
                                listaVerduraPropia =  visita!!.parcelas!!.distinctBy { it.verdura!!.id_verdura }
                                listaVerduraAjena = ArrayList<ParcelaVerdura>()
                                listaQuintas.toMutableList().removeAt(position)
                                for(each in listaQuintas){
                                    visitaAjena = result_visitas.getUltimavisita(each.id_quinta)!!
                                    var verduras = visitaAjena.parcelas!!.filter { each -> listaVerduraPropia.all { it.verdura!!.id_verdura != each!!.verdura!!.id_verdura } && listaVerduraAjena.all{ it.verdura!!.id_verdura != each!!.verdura!!.id_verdura}}
                                    listaVerduraAjena.addAll(verduras.asIterable() as Collection<ParcelaVerdura>)
                                }
                            listaVerduraAjena = listaVerduraAjena.distinctBy { it.verdura!!.id_verdura } as ArrayList<ParcelaVerdura>
                            initView(listaVerduraPropia,listaVerduraAjena)


                                binding.submit.setOnClickListener {
                                    val dataPropia = adapterPropia.getData()
                                    val dataAjena= adapterAjena.getData()
                                    var verdura: VerduraFechaList
                                    var lista_verduras = ArrayList<VerduraFechaList>()
                                    var count_verduras_otro = 0
                                    var count_verduras = 0
                                    var verdura_en_parcela = true
                                    for (each in dataPropia.keys) {
                                        verdura = result_verduras!!.get(each)
                                        count_verduras +=1
                                        lista_verduras.add(verdura)
                                    }
                                    for(each in dataAjena.keys){
                                        verdura = result_verduras!!.get(each)
                                        count_verduras +=1
                                        count_verduras_otro +=1
                                        lista_verduras.add(verdura)
                                    }
                                    var id_fp = result_quinta.quintas!!.get(position).fpId
                                    if (result_bolson!!.any { it.idFp == id_fp }){
                                        binding.errores.text =
                                            "Ya existe un bolson para dicha familia"
                                        binding.errores.visibility = View.VISIBLE
                                    }
                                    else if (count_verduras <1 || count_verduras_otro>2 ) {
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
                                        lifecycleScope.launch { BolsonConeccion.post(bolson) }
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
    }
    fun initSpinner(spinner: Spinner,listQuinta: List<Quinta>){
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listQuinta.map{it.nombre}
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapterSpinner = adapter
            spinner.adapter = adapterSpinner
        }
    }
    fun initView(listaVerduraPropia: List<ParcelaVerdura>,listaVerduraAjena:List<ParcelaVerdura>) {
        if(!listaVerduraPropia.isEmpty()){
            binding.vaciaPropia.visibility = View.GONE
            binding.recyclerVerdurasPropia.layoutManager = LinearLayoutManager(this)
            this.adapterPropia = VerduraAdapter(listaVerduraPropia as MutableList<ParcelaVerdura>)
            binding.recyclerVerdurasPropia.adapter = adapterPropia
        }
        else{
            binding.recyclerVerdurasPropia.visibility = View.GONE
        }
        if(!listaVerduraAjena.isEmpty()) {
            binding.recyclerVerdurasAjena.layoutManager = LinearLayoutManager(this)
            this.adapterAjena = VerduraAdapter(listaVerduraAjena as MutableList<ParcelaVerdura>)
            binding.recyclerVerdurasAjena.adapter = adapterAjena
            binding.vaciaAjena.visibility = View.GONE
        }
        else{
            binding.recyclerVerdurasAjena.visibility = View.GONE
        }
    }





}