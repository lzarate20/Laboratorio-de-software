package com.sample.foo.labsof
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch

class HistorialVisitas : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVisita)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dCreate = DialogHelper.espera(this@HistorialVisitas)
        dCreate.show()
        lifecycleScope.launch {
            val visitas = VisitaConeccion.get().getVisitasPasadas().ordenarFechaYTecnico()
            val user = UserConeccion.get().getTecnicos()
            val quinta = QuintaConeccion.get()
            dCreate.dismiss()
            if (visitas.error == null && user.error == null && quinta.error == null) {
                if (visitas.visitas?.size==0){
                    DialogHelper.dialogo(this@HistorialVisitas,
                        "Error","No hay visitas pasadas guardadas",
                        true,false,{finish()},{})
                }
                recyclerView.adapter = VisitaAdapter(visitas.union(user, quinta),register,this@HistorialVisitas)
            }else{
                DialogHelper.dialogo(this@HistorialVisitas,
                "Error","Ocurrio un error al intentar obtener los datos de las visitas",
                true,false,{finish()},{})
            }
        }

    }
    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent =
                    Intent(this@HistorialVisitas, HistorialVisitas::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)

            }
        }


}