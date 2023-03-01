package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.databinding.ActivityQuintasBinding

class QuintasActivity: AppCompatActivity() {
    lateinit var binding: ActivityQuintasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuintasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        FT.add(R.id.toolbar, toolbar)

        FT.commit()

        binding.listaQuintas.setOnClickListener { view: View ->
            val intent = Intent(this, ListadoQuintasActivity::class.java)
            startActivity(intent)
        }
        binding.crearQuinta.setOnClickListener { view: View ->
            val intent = Intent(this, CrearQuintaActivity::class.java)
            startActivity(intent)
        }

    }
    }