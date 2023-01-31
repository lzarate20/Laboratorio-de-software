package com.sample.foo.labsof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class Bolsones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bolsones)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        FT.add(R.id.toolbar, toolbar)

        FT.commit()
        val listaBolson: Button = findViewById(R.id.listaBolsones)
        listaBolson.setOnClickListener { view: View ->
            val intent = Intent(this, ListadoBolsones::class.java)
            startActivity(intent)
        }
    }
}