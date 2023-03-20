package com.sample.foo.labsof

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
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
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.launch

class ProximasVisitas : AppCompatActivity() {
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
        val tipo= this.intent.getIntExtra("tipo", 0)

        initRecyclerView(tipo)

    }
    private fun initRecyclerView(tipo:Int){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVisita)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val dCreate = DialogHelper.espera(this@ProximasVisitas)
        dCreate.show()
        lifecycleScope.launch {
            val resultVisitas = VisitaConeccion.get()
            var visitas = resultVisitas.getVisitasFuturas().ordenarFechaYTecnico()
            val session = Session(this@ProximasVisitas).getSession()
            if(session.isTecnico()&& tipo ==1){
                visitas= resultVisitas.getMisVisitas(session.id_user!!).getVisitasFuturas().ordenarFecha()
            }
            val user = UserConeccion.get().getTecnicos()
            val quinta = QuintaConeccion.get()
            dCreate.dismiss()
            if (visitas.error == null && user.error == null && quinta.error == null) {
                if (visitas.visitas?.size==0){
                    DialogHelper.dialogo(this@ProximasVisitas,
                    "Error","No hay visitas proximas guardadas",
                    true,false,{finish()},{})
                }
                recyclerView.adapter = VisitaAdapter(visitas.union(user, quinta),register,this@ProximasVisitas)
            }else{
                DialogHelper.dialogo(this@ProximasVisitas,
                    "Error","Ocurrio un error al intentar obtener los datos de las visitas",
                    true,false,{finish()},{})
            }
        }
    }
    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent =
                    Intent(this@ProximasVisitas, ProximasVisitas::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)

            }
        }

}