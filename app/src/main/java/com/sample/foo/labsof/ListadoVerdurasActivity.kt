package com.sample.foo.labsof

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.LVerduraAdapter
import com.sample.foo.labsof.Adapter.UserAdapter
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.Listados.ListVerduras
import kotlinx.coroutines.launch

class ListadoVerdurasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_verduras)
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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVerdura)
        recyclerView.layoutManager= LinearLayoutManager(this)
        var builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this)
        builder.setTitle("Esperando información")
        builder.setMessage("Su solicitud esta siendo procesada")
        builder.setCancelable(false)
        var bCreate = builder.create()
        bCreate.show()
        lifecycleScope.launch{

            val verdura= VerduraConeccion.get()
            bCreate.dismiss()
            if(verdura!= null) {
                recyclerView.adapter = LVerduraAdapter(ListVerduras(verdura))
            }else{
                builder.setTitle("Error")
                builder.setMessage("Ocurrio un error al intentar obtener las verduras")
                builder.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                builder.create().show()
            }
        }

    }
}