package com.sample.foo.labsof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.databinding.ActivityBolsonesBinding

class Bolsones : AppCompatActivity() {
    lateinit var binding: ActivityBolsonesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBolsonesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        FT.add(R.id.toolbar, toolbar)

        FT.commit()
        binding.listaBolsones.text = "Lista de bolsones"
        binding.listaBolsones.setOnClickListener { view: View ->
            val intent = Intent(this, ListadoBolsones::class.java)
            startActivity(intent)
        }

        binding.crearBolson.text = "Crear bolson"
        binding.crearBolson.setOnClickListener { view: View ->
            val intent = Intent(this, CrearBolson::class.java)
            startActivity(intent)
        }

    }
}