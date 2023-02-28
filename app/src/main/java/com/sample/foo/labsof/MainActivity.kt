package com.sample.foo.labsof

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.Session
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()

        val toolbar: Fragment = ToolbarFragment()
        var bun = Bundle()
        bun.putString("toolbar", "1")
        toolbar.setArguments(bun)
         if (Session(this@MainActivity).haveSesion()) {
             val menu_inicio: Fragment = Menu()
            FT.add(R.id.inicio_menu, menu_inicio)
        } else {
           val menu_inicio: Fragment = InicioSesionFragment()
            FT.add(R.id.inicio_menu, menu_inicio)
        }
        FT.add(R.id.toolbar, toolbar)
        FT.commit()

    }

    override fun onBackPressed() {

    }

}