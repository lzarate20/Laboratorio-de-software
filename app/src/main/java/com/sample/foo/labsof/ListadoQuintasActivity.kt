package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.QuintaAdapter
import com.sample.foo.labsof.Coneccion.FamiliaProductoraConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.databinding.ActivityListaQuintasBinding
import retrofit2.HttpException
import java.io.IOException

class ListadoQuintasActivity: AppCompatActivity() {

    lateinit var binding: ActivityListaQuintasBinding
    private lateinit var adapter: QuintaAdapter
    lateinit var listaBolsones : ListQuintas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaQuintasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()

        lifecycleScope.launchWhenCreated{
            try {
                val lista_quintas = QuintaConeccion.get()
                val lista_familias = FamiliaProductoraConeccion.get()
                initView(lista_quintas,lista_familias!!)
            }
            catch (e: IOException) {

            }
            catch (e: HttpException){

            }



        }

    }

    fun initView(listaQ: ListQuintas,listaF: List<FamiliaProductora>) {
        val recyclerView = binding.recyclerQuintas
        val textView = binding.sinQuintas
        if (listaQ.quintas!!.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = QuintaAdapter(listaQ.quintas!!,listaF,{},{})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

}