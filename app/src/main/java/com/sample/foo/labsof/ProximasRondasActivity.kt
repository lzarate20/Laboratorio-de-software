package com.sample.foo.labsof

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Adapter.RondaAdapter
import com.sample.foo.labsof.Coneccion.FamiliaProductoraConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.databinding.ActivityListaBolsonesBinding
import com.sample.foo.labsof.databinding.ActivityListaRondaBinding
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProximasRondasActivity: AppCompatActivity() {

    lateinit var binding: ActivityListaRondaBinding
    private lateinit var adapter: RondaAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListaRondaBinding.inflate(layoutInflater)
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
        lifecycleScope.launch {
            var result = RondaConeccion.getRondas()
            dCreate.dismiss()
            if(result != null){
                result = result.filter { Ronda.Compare.isAfterToday(it) }.sortedWith(
                    Comparator<Ronda>( {t, t2 ->
                        var day1 = LocalDate.of(t.fecha_inicio[0],t.fecha_inicio[1],t.fecha_inicio[2])
                        var day2 = LocalDate.of(t2.fecha_inicio[0],t2.fecha_inicio[1],t2.fecha_inicio[2])
                       day1.compareTo(day2)
                }))
                initView(result)
            }
        }
    }

    fun onItemSelected(ronda:Ronda){
        val intent = Intent(this, EditarRondaActivity::class.java)
        intent.putExtra("ronda",ronda.id_ronda)
        startActivity(intent)
    }

    fun initView(listaR: List<Ronda>) {
        val recyclerView = binding.recyclerRondas
        val textView = binding.sinRondas
        if (listaR.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = RondaAdapter(listaR,{onItemSelected(it)},{deleteItem(it)})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }

    fun deleteItem(ronda:Ronda){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Estás seguro que querés eliminar la ronda?")
        builder.setPositiveButton("Si"){dialogInterface, which ->
            val dCreate = DialogHelper.espera(this@ProximasRondasActivity)
            dCreate.show()
            lifecycleScope.launchWhenCreated{
                var res = RondaConeccion.delete(ronda.id_ronda!!)
                dCreate.dismiss()
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