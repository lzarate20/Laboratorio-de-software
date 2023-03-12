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
import com.sample.foo.labsof.Adapter.QuintaAdapter
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.Coneccion.FamiliaProductoraConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.Service.BolsonService
import com.sample.foo.labsof.databinding.ActivityListaQuintasBinding
import kotlinx.coroutines.launch
import com.sample.foo.labsof.helpers.DialogHelper
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ListadoQuintasActivity: AppCompatActivity() {

    lateinit var binding: ActivityListaQuintasBinding
    private lateinit var adapter: QuintaAdapter
    lateinit var listaQuintas : ListQuintas

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
        val dCreate = DialogHelper.espera(this)
        dCreate.show()
        lifecycleScope.launchWhenCreated{
            try {
                listaQuintas = QuintaConeccion.get()
                val lista_familias = FamiliaProductoraConeccion.get()
            if(listaQuintas !=null && lista_familias!= null)
                initView(listaQuintas,lista_familias!!)
            }
            catch (e: IOException) {

            }
            catch (e: HttpException){

            }
            dCreate.dismiss()


        }

    }

    fun onItemSelected(quinta: Quinta){
        val intent = Intent(this, EditarQuintaActivity::class.java)
        intent.putExtra("quinta",quinta.id_quinta)
        startActivity(intent)
    }

    fun onItemRoute(quinta: Quinta){
        val intent = Intent(this, RutaActivity::class.java)
        intent.putExtra("quinta",quinta.id_quinta)
        startActivity(intent)
    }

    fun initView(listaQ: ListQuintas,listaF: List<FamiliaProductora>) {
        val recyclerView = binding.recyclerQuintas
        val textView = binding.sinQuintas
        if (listaQ.quintas!!.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = QuintaAdapter(listaQ.quintas!!,listaF,{onItemSelected(it)},{onItemRoute(it)},{deleteItem(it)})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun deleteItem(quinta: Quinta){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Estás seguro que querés eliminar la quinta? Se eliminará también la familia")
        builder.setPositiveButton("Si"){dialogInterface, which ->
            val dCreate = DialogHelper.espera(this)
            dCreate.show()
            lifecycleScope.launchWhenCreated{
                var res = QuintaConeccion.delete(quinta.id_quinta!!)
                var resF = FamiliaProductoraConeccion.delete(quinta.fpId!!)
                dCreate.dismiss()
                listaQuintas.quintas?.toMutableList()?.removeIf{ it.id_quinta == quinta.id_quinta }
                finish()
                startActivity(intent)
                Toast.makeText(applicationContext,"Se ha borrado correctamente", Toast.LENGTH_LONG).show()

            }
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}