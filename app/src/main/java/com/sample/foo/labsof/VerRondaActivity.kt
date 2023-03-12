package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Adapter.VerduraAdapter
import com.sample.foo.labsof.Coneccion.BolsonConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.databinding.ActivityEditarBolsonBinding
import com.sample.foo.labsof.databinding.ActivityListaRondaBinding
import com.sample.foo.labsof.databinding.ActivityVerRondaBinding
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch

class VerRondaActivity: AppCompatActivity() {

    lateinit var binding: ActivityVerRondaBinding
    lateinit var  adapter: BolsonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRondaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        val ronda_id:Int = intent.getIntExtra("ronda",-1)
        var ronda: Ronda?
        var bolsones:List<Bolson>?
        var quintas:ListQuintas
        val dCreate = DialogHelper.espera(this@VerRondaActivity)
        dCreate.show()
        lifecycleScope.launch{
            var cantidad = 0
            ronda = RondaConeccion.getRonda(ronda_id)
            bolsones = BolsonConeccion.getBolsonByRonda(ronda_id)
            quintas = QuintaConeccion.get()
            dCreate.dismiss()
            if(!bolsones?.isEmpty()!!){
                for(bolson in bolsones!!){
                    cantidad +=bolson.cantidad
                }
                binding.fechaInicio.setText(ConversorDate.convertToInput(ronda!!.fecha_inicio))
                binding.fechaFin.setText(ConversorDate.convertToInput(ronda!!.fecha_fin))
                binding.cantidad.text = cantidad.toString()
                initView(bolsones!!,quintas!!.quintas!!, ronda!!)
            }
        }


    }

    override fun onStart(){
        super.onStart()
        adapter.notifyDataSetChanged()
    }

    fun initView(listaB: List<Bolson>,listaQ: List<Quinta>,ronda:Ronda) {
        val recyclerView = binding.bolsones
        val textView = binding.cargando
        if (listaB.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = BolsonAdapter(listaB.toMutableList(), listaQ.toMutableList(), ronda, { onItemSelected(it) }, {})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun onItemSelected(bolson: Bolson){
        val intent = Intent(this, VerBolsonActivity::class.java)
        intent.putExtra("bolson",bolson.id_bolson)
        startActivity(intent)
    }


}