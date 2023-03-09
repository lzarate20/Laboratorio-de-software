package com.sample.foo.labsof

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.VisitaAdapter
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch

class ProximasVisitas : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proximas_visitas)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()
        initRecyclerView()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVisita)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val dCreate = DialogHelper.espera(this@ProximasVisitas)
        dCreate.show()
        lifecycleScope.launch {
            val visitas = VisitaConeccion.get().getVisitasPasadas().ordenarFechaYTecnico()
            val user = UserConeccion.get().getTecnicos()
            val quinta = QuintaConeccion.get()
            dCreate.dismiss()
            if (visitas.error == null && user.error == null && quinta.error == null) {
                if (visitas.visitas?.size==0){
                    DialogHelper.dialogo(this@ProximasVisitas,
                    "Error","No hay visitas proximas guardadas",
                    true,false,{finish()},{})
                }
                recyclerView.adapter = VisitaAdapter(visitas.union(user, quinta))
            }else{
                DialogHelper.dialogo(this@ProximasVisitas,
                    "Error","Ocurrio un error al intentar obtener los datos de las visitas",
                    true,false,{finish()},{})
            }
        }
    }

}