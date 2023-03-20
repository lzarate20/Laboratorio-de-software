package com.sample.foo.labsof

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.Coneccion.ParcelaConeccion
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        val url = findViewById<ImageButton>(R.id.url)
        url.setOnClickListener { v->
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(v.context)
            builder.setTitle("Cambiar url de conección")
            var linearLayout = LinearLayout(v.context)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val uri = EditText(v.context)
            uri.width = 200
            var text = TextView(v.context)
            text.text = "url: "
            linearLayout.addView(text)
            linearLayout.addView(uri)
            builder.setView(linearLayout)
            builder.setPositiveButton(
                "Cambiar",
                DialogInterface.OnClickListener { dialog, which ->
                    if(Verificacion.url(uri)){
                        var new_url = uri.text.toString().removeSuffix("/").replace(" ","")
                        val field = Coneccion::class.java.getDeclaredField("url")
                        field.isAccessible = true
                        field.set(null, new_url)
                        val field2 = Coneccion::class.java.getDeclaredField("api")
                        field2.isAccessible = true
                        field2.set(
                            null, Retrofit.Builder().baseUrl(Coneccion.url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                        )}else{
                            DialogHelper.dialogo(this@MainActivity,"Error",
                            "Debe introducir una url valida. \n Ejemplo: http://lajusta.com",true,false,{},{})
                    }
                })
            builder.setNegativeButton(
                "Cancelar",
                DialogInterface.OnClickListener { dialog, which ->
                })
            builder.create()?.show()
        }
    }

    override fun onBackPressed() {

    }

}