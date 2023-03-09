package com.sample.foo.labsof

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class Visitas : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitas)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()
        val cVisita: Button = findViewById(R.id.crearVisita)
        cVisita.setOnClickListener { view: View ->
            val intent = Intent(this, CrearVisita::class.java)
            startActivity(intent)
        }
        val hVisita: Button = findViewById(R.id.historialVisitas)
        hVisita.setOnClickListener { view: View ->
            val intent = Intent(this, HistorialVisitas::class.java)
            startActivity(intent)
        }
        val pVisita: Button = findViewById(R.id.proximasVisitas)
        pVisita.setOnClickListener { view: View ->
            val intent = Intent(this, ProximasVisitas::class.java)
            startActivity(intent)
        }
    }


}