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
import com.sample.foo.labsof.Adapter.UserAdapter
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.launch

class ListarTecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_tec)
        val session = Session(this)
        val userSession= session.getSession()
        if (!session.haveSesion()|| userSession.isTecnico()) {
            finish()
        }
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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerUser)
        recyclerView.layoutManager= LinearLayoutManager(this)
        val bCreate = DialogHelper.espera(this@ListarTecActivity)
        bCreate.show()
        lifecycleScope.launch{
            val user= UserConeccion.get().getTecnicos()
            bCreate.dismiss()
            if(user.error== null) {
                recyclerView.adapter = UserAdapter(user, register,this@ListarTecActivity)
            }else{
                DialogHelper.dialogo(
                    this@ListarTecActivity,
                    "Error",
                    user.error!!,
                    true,
                    false,
                    {},
                    {}
                )
            }
        }

    }

    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent =
                    Intent(this@ListarTecActivity, ListarTecActivity::class.java)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)

            }
        }
}