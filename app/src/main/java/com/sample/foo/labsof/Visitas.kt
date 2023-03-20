package com.sample.foo.labsof

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.Session

class Visitas : AppCompatActivity() {
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
        val tipo= this.intent.getIntExtra("tipo", 0)
        val titulo = findViewById<TextView>(R.id.titulo)
        val cVisita: Button = findViewById(R.id.crearVisita)
        val compa = findViewById<Button>(R.id.compa)
        var nTipo= 0
        if(tipo== 1){
            titulo.text = "Visitas de mis compañeros"
            cVisita.visibility= View.GONE
            nTipo = 0
        }else{
            if(tipo== 2){
                compa.visibility=View.VISIBLE
                nTipo=1
            }
        }
       cVisita.setOnClickListener { view: View ->
            val intent = Intent(this, CrearVisita::class.java)

           startActivity(intent)
        }
        val hVisita: Button = findViewById(R.id.historialVisitas)
        hVisita.setOnClickListener { view: View ->
            val intent = Intent(this, HistorialVisitas::class.java)
            intent.putExtra("tipo", nTipo)
            startActivity(intent)
        }
        val pVisita: Button = findViewById(R.id.proximasVisitas)
        pVisita.setOnClickListener { view: View ->
            val intent = Intent(this, ProximasVisitas::class.java)
            intent.putExtra("tipo", nTipo)
            startActivity(intent)
        }
        compa.setOnClickListener { view: View ->
            val intent = Intent(this, Visitas::class.java)
            intent.putExtra("tipo", 1)
            startActivity(intent)
        }
    }


}