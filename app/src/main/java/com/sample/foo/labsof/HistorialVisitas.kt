package com.sample.foo.labsof

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.VisitaAdapter
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import kotlinx.coroutines.launch

class HistorialVisitas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_visitas)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()
        initRecyclerView()

    }
    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVisita)
        recyclerView.layoutManager= LinearLayoutManager(this)
        lifecycleScope.launch{
            val visitas= VisitaConeccion.get().getVisitasPasadas().ordenarFechaYTecnico()
            val user= UserConeccion.get().getTecnicos()
            val quinta= QuintaConeccion.get()
            val es= findViewById<TextView>(R.id.esperando)
            es.visibility= View.GONE
            recyclerView.adapter= VisitaAdapter(visitas.union(user, quinta))
            recyclerView.visibility= View.VISIBLE
        }

    }
}