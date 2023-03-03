package com.sample.foo.labsof

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.databinding.ActivityBolsonesBinding
import com.sample.foo.labsof.databinding.ActivityRondaBinding

class RondaActivity: AppCompatActivity() {

    lateinit var binding: ActivityRondaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRondaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        binding.crearRonda.setOnClickListener { view: View ->
            val intent = Intent(this, CrearRondaActivity::class.java)
            startActivity(intent)
        }
        binding.proximasRondas.setOnClickListener { view: View ->
            val intent = Intent(this, ProximasRondasActivity::class.java)
            startActivity(intent)
        }
    }
}