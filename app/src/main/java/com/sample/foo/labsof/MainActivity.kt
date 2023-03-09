package com.sample.foo.labsof

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()

        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "1")
        toolbar.setArguments(bun)
        val sesion=Session(this@MainActivity)
         if (sesion.haveSesion()) {
             val dCreate= DialogHelper.espera(this@MainActivity)
             dCreate.show()
             lifecycleScope.launch {
                 val user = UserConeccion.getSingle(sesion.getSession().id_user)
                 dCreate.dismiss()
                 if (user.error!=null){
                    sesion.closeSession()
                    val intent =
                        Intent(this@MainActivity, MainActivity::class.java)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                 sesion.saveSession(user)

             }
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