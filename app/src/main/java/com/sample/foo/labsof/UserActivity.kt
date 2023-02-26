package com.sample.foo.labsof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.Session

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        var bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()


        val session = Session(this@UserActivity)
        val userSession= session.getSession()
        if (!session.haveSesion()|| userSession.isTecnico()) {
            finish()
        }
        val crear= findViewById<Button>(R.id.crear)
        val lTec= findViewById<Button>(R.id.listadoTecnicos)
        val lAdmin= findViewById<Button>(R.id.listadoAdmin)
        crear.setOnClickListener { view: View ->
            val intent = Intent(this, CrearUserActivity::class.java)
            startActivity(intent)
        }
        lTec.setOnClickListener { view: View ->
            val intent = Intent(this, ListarTecActivity::class.java)
            startActivity(intent)
        }
        lAdmin.setOnClickListener { view: View ->
            val intent = Intent(this, ListarAdminActivity::class.java)
            startActivity(intent)
        }
    }
}