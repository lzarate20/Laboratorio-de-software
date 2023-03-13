package com.sample.foo.labsof


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.VerduraAdapter
import com.sample.foo.labsof.Coneccion.*
import com.sample.foo.labsof.DataClass.*

import com.sample.foo.labsof.databinding.ActivityCrearBolsonBinding
import com.sample.foo.labsof.helpers.DialogHelper

import kotlinx.coroutines.launch


class EditarBolson: AppCompatActivity() {


    lateinit var binding: ActivityCrearBolsonBinding
    lateinit var  adapterPropia:VerduraAdapter
    lateinit var  adapterAjena:VerduraAdapter
    lateinit var adapterSpinner:ArrayAdapter<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearBolsonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        binding.submit.text = "Editar bolson"
        binding.title.text = "Editar bolson"
        val bolson_id:Int = intent.getIntExtra("bolson",-1)
        val spinner: Spinner = binding.familiaProductora
        val dCreate= DialogHelper.espera(this)
        dCreate.show()
        lifecycleScope.launch{
            val bolson_item = BolsonConeccion.getBolson(bolson_id)
            val verduras = bolson_item!!.verduras!!
            val result_visitas = VisitaConeccion.get().getVisitasPasadas()
            val result_quinta = QuintaConeccion.get()
            val result_verduras = VerduraConeccion.get()
            val ronda_actual = RondaConeccion.getRonda(bolson_item.idRonda!!)
            val result_bolson = BolsonConeccion.getBolsonByRonda(bolson_item.idRonda)!!.toMutableList()
            dCreate.dismiss()
            result_bolson.removeIf{ it.id_bolson == bolson_item.id_bolson }
            val quintaActual = result_quinta.quintas!!.first { it.fpId == bolson_item.idFp }

                if(ronda_actual != null) {
                    var cantidad_input:Int? = bolson_item.cantidad
                    binding.cantidad.setText(bolson_item.cantidad.toString())
                    binding.cantidad.doOnTextChanged{ text, start, count, after ->
                        cantidad_input = text.toString().toIntOrNull()
                    }
                    val listaQuintas = result_quinta.quintas!!.filter { result_visitas.getUltimavisita(it.id_quinta) != null }
                    initSpinner(spinner, listaQuintas)
                    spinner.setSelection(adapterSpinner.getPosition(quintaActual.nombre.toString()))
                    val id_quinta = quintaActual.id_quinta
                    var visita = result_visitas.getUltimavisita(id_quinta)
                    var listaVerduraPropia =  visita!!.parcelas!!.distinctBy { it.verdura!!.id_verdura }
                    var listaVerduraAjena = ArrayList<ParcelaVerdura>()
                    var visitaAjena:VisitaFechaList?
                    for(each in listaQuintas.subList(1,listaQuintas.size-1)){
                        visitaAjena = VisitaFechaList.getVisitaById(each.id_quinta!!,result_visitas.visitas!!)
                        val verduras = visitaAjena.parcelas!!.map{it.verdura}.filter { each -> listaVerduraPropia.all { it.verdura!!.id_verdura != each!!.id_verdura } && listaVerduraAjena.all{ it.verdura!!.id_verdura != each!!.id_verdura}}
                        listaVerduraAjena.addAll(verduras.asIterable() as Collection<ParcelaVerdura>)
                    }
                    val listVerduraSelectedPropia:ArrayList<VerduraFechaList> = ArrayList<VerduraFechaList>()
                    val listVerduraSelectedAjena:ArrayList<VerduraFechaList> = ArrayList<VerduraFechaList>()
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
                            for(each in listaQuintas) {
                                visitaAjena = result_visitas.getUltimavisita(each.id_quinta)!!
                                    val verduras =
                                        visitaAjena!!.parcelas!!.filter { each -> listaVerduraPropia.all { it.verdura!!.id_verdura != each!!.verdura!!.id_verdura } && listaVerduraAjena.all { it.verdura!!.id_verdura != each!!.verdura!!.id_verdura } }
                                    listaVerduraAjena.addAll(verduras.asIterable() as Collection<ParcelaVerdura>)

                            }

                            for(each in verduras){
                                val lp = listaVerduraPropia.map { it.verdura }
                                val la = listaVerduraAjena.map{it.verdura}
                                if(la.any { it!!.id_verdura == each.id_verdura }&& lp.none { it!!.id_verdura == each.id_verdura }){
                                    listVerduraSelectedAjena.add(each)
                                }
                                else{
                                    listVerduraSelectedPropia.add(each)
                                }
                            }
                            listaVerduraAjena = listaVerduraAjena.distinctBy { it.verdura!!.id_verdura } as ArrayList<ParcelaVerdura>
                            initView(listaVerduraPropia,listaVerduraAjena,listVerduraSelectedPropia,listVerduraSelectedAjena)


                            binding.submit.setOnClickListener {
                                val dataPropia = adapterPropia.getData()
                                val dataAjena= adapterAjena.getData()
                                var verdura: VerduraFechaList
                                var lista_verduras = ArrayList<VerduraFechaList>()
                                var count_verduras_otro = 0
                                var count_verduras = 0
                                var verdura_en_parcela = true
                                for (each in dataPropia.keys) {
                                    verdura = result_verduras!!.first { each == it.id_verdura }
                                    count_verduras +=1
                                    lista_verduras.add(verdura)
                                }
                                for(each in dataAjena.keys){
                                    verdura = result_verduras!!.first { each == it.id_verdura }
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
                                        bolson_id,
                                        cantidad_input!!,
                                        idFp = result_quinta.quintas!!.get(position).fpId,
                                        idRonda = bolson_item.idRonda,
                                        verduras = lista_verduras
                                    )
                                    val dCreate= DialogHelper.espera(this@EditarBolson)
                                    dCreate.show()
                                    var listo = lifecycleScope.launch { BolsonConeccion.put(bolson)
                                    dCreate.dismiss()}
                                    listo.invokeOnCompletion {
                                        val intent = Intent(
                                            this@EditarBolson,
                                            ListadoBolsonesActivity::class.java
                                        )
                                        intent.putExtra("bolson", bolson.id_bolson)
                                        setResult(RESULT_OK, intent)
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
        }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
        super.onBackPressed()
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
        fun initView(listaVerduraPropia: List<ParcelaVerdura>,listaVerduraAjena:List<ParcelaVerdura>,listaVerduraSelectedPropia:ArrayList<VerduraFechaList>,listaVerduraSelectedAjena:ArrayList<VerduraFechaList>) {
                binding.recyclerVerdurasPropia.layoutManager = LinearLayoutManager(this)
                this.adapterPropia = VerduraAdapter(listaVerduraPropia as MutableList<ParcelaVerdura>,listaVerduraSelectedPropia)
                binding.recyclerVerdurasPropia.adapter = adapterPropia
            if(!listaVerduraPropia.isEmpty()){
                binding.vaciaPropia.visibility = View.GONE
                binding.recyclerVerdurasPropia.visibility = View.VISIBLE
            }
            else{
                binding.recyclerVerdurasPropia.visibility = View.GONE
                binding.vaciaPropia.visibility = View.VISIBLE
            }
                binding.recyclerVerdurasAjena.layoutManager = LinearLayoutManager(this)
                this.adapterAjena = VerduraAdapter(listaVerduraAjena as MutableList<ParcelaVerdura>,listaVerduraSelectedAjena)
                binding.recyclerVerdurasAjena.adapter = adapterAjena
            if(!listaVerduraAjena.isEmpty()) {
                binding.vaciaAjena.visibility = View.GONE
                binding.recyclerVerdurasAjena.visibility = View.VISIBLE
            }
            else{
                binding.recyclerVerdurasAjena.visibility = View.GONE
                binding.vaciaAjena.visibility=View.VISIBLE
            }
        }






    }

