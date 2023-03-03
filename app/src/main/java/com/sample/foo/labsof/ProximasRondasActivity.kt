package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.foo.labsof.Adapter.BolsonAdapter
import com.sample.foo.labsof.Adapter.RondaAdapter
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.databinding.ActivityListaBolsonesBinding
import com.sample.foo.labsof.databinding.ActivityListaRondaBinding
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProximasRondasActivity: AppCompatActivity() {

    lateinit var binding: ActivityListaRondaBinding
    private lateinit var adapter: RondaAdapter
    lateinit var listaBRondas : List<Ronda>

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
        lifecycleScope.launch {
            var result = RondaConeccion.getRondas()
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

    fun initView(listaR: List<Ronda>) {
        val recyclerView = binding.recyclerRondas
        val textView = binding.sinRondas
        if (listaR.isEmpty()) {
            textView.setVisibility(View.VISIBLE)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = RondaAdapter(listaR,{},{})
            recyclerView.adapter = adapter
            textView.setVisibility(View.GONE)
        }
    }
}