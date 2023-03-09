package com.sample.foo.labsof

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.LVerduraAdapter
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.Listados.ListVerduras
import com.sample.foo.labsof.helpers.DialogHelper
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

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVerdura)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val bCreate = DialogHelper.espera(this@ListadoVerdurasActivity)
        bCreate.show()
        lifecycleScope.launch {
            val verdura = VerduraConeccion.get()
            bCreate.dismiss()
            if (verdura != null) {
                recyclerView.adapter = LVerduraAdapter(
                    ListVerduras(verdura), register,this@ListadoVerdurasActivity
                )
            } else {
                DialogHelper.dialogo(this@ListadoVerdurasActivity,"Error","No se puede eliminar la verdura",true,false,{},{})
            }
        }

    }

    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent =
                    Intent(this@ListadoVerdurasActivity, ListadoVerdurasActivity::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)

            }
        }
}