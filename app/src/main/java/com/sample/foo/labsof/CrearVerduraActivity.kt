package com.sample.foo.labsof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch

class CrearVerduraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_verdura)
        val session = Session(this)
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
        val mes = arrayOf<String>(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )
        val nombre = findViewById<EditText>(R.id.nombre)
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val cosecha = findViewById<Spinner>(R.id.cosecha)
        val siembra = findViewById<Spinner>(R.id.siembra)
        val image = findViewById<EditText>(R.id.image)
        creacionSpinner(cosecha, mes)
        creacionSpinner(siembra, mes)
        val guardar = findViewById<Button>(R.id.guardar)
        guardar.setOnClickListener { view: View ->
            if (Verificacion.notVacio(nombre) && Verificacion.notVacio(descripcion)
                && Verificacion.notVacio(image)
            ) {
                val verdura = Verdura()
                verdura.nombre = nombre.text.toString()
                verdura.descripcion = descripcion.text.toString()
                verdura.tiempo_cosecha =
                    ConversorDate.convertToBD(ConversorDate.convertMesBD(cosecha.selectedItemPosition + 1))
                verdura.mes_siembra =
                    ConversorDate.convertToBD(ConversorDate.convertMesBD(siembra.selectedItemPosition + 1))
                verdura.archImg = image.text.toString()
                val bCreate = DialogHelper.espera(this@CrearVerduraActivity)
                bCreate.show()
                lifecycleScope.launch {
                    val v = VerduraConeccion.post(verdura)
                    bCreate.dismiss()
                    if (v.error == null) {
                        DialogHelper.dialogo(
                            this@CrearVerduraActivity,
                            "Guardado exitoso",
                            "Se guardo correctamente la verdura",
                            true, false, {}, {})
                    } else {
                        DialogHelper.dialogo(
                            this@CrearVerduraActivity,
                            "Error",
                            v.error!!,
                            true,
                            false,
                            { finish() },
                            {}
                        )
                    }
                }

            } else {
                DialogHelper.dialogo(
                    this@CrearVerduraActivity,
                    "Error",
                    "Todos los campos deben estar completos",
                    true,
                    false,
                    {},
                    {})
            }
        }
    }

    private fun creacionSpinner(spinner: Spinner, list: Array<String>) {
        val mSortAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            list
        )
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mSortAdapter
    }
}