package com.sample.foo.labsof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.Session

class VerduraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verdura)
        val session = Session(this@VerduraActivity)

        if (!session.haveSesion()) {
            finish()
        }
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()


        val crear = findViewById<Button>(R.id.crear)
        val list = findViewById<Button>(R.id.listado)
        crear.setOnClickListener { view: View ->
            val intent = Intent(this, CrearVerduraActivity::class.java)
            startActivity(intent)
        }
        list.setOnClickListener { view: View ->
            val intent = Intent(this, ListadoVerdurasActivity::class.java)
            startActivity(intent)
        }
    }
}