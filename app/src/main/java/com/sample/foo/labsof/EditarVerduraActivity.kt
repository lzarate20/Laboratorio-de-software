package com.sample.foo.labsof

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch

class EditarVerduraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_verdura)
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
        val id: Int = this.intent.getIntExtra("id", 0)
        val dCreate = DialogHelper.espera(this)
        dCreate.show()
        var ver:VerduraFechaList?=null
        lifecycleScope.launch {
            ver = VerduraConeccion.getSingle(id)
            dCreate.dismiss()
            if (ver == null) {
                DialogHelper.dialogo(
                    this@EditarVerduraActivity,
                    "Error",
                    "No se pudo obtener los datos de la verdura",
                    true,
                    true,
                    { finish() },
                    {}
                )
            } else {
                nombre.setText(ver?.nombre)
                descripcion.setText(ver?.descripcion)
                image.setText(ver?.archImg)
                cosecha.setSelection(ver?.tiempo_cosecha!![1] - 1)
                siembra.setSelection(ver?.mes_siembra!![1] - 1)
            }
        }
        guardar.setOnClickListener { view: View ->
            DialogHelper.dialogo(
                this,
                "Guardar",
                "¿Seguro que quiere guardar los cambios?",
                true,
                true,
                {
                    if (Verificacion.notVacio(nombre) && Verificacion.notVacio(descripcion)
                        && Verificacion.notVacio(image)
                    ) {
                        val verdura = Verdura(ver!!)
                        verdura.nombre = nombre.text.toString()
                        verdura.descripcion = descripcion.text.toString()
                        verdura.tiempo_cosecha =
                            ConversorDate.convertToBD(ConversorDate.convertMesBD(cosecha.selectedItemPosition + 1))
                        verdura.mes_siembra =
                            ConversorDate.convertToBD(ConversorDate.convertMesBD(siembra.selectedItemPosition + 1))
                        verdura.archImg = image.text.toString()
                        val bCreate = DialogHelper.espera(this@EditarVerduraActivity)
                        bCreate.show()
                        lifecycleScope.launch {
                            val v = VerduraConeccion.put(verdura)
                            bCreate.dismiss()
                            if (v.error == null) {
                                DialogHelper.dialogo(
                                    this@EditarVerduraActivity,
                                    "Guardado exitoso",
                                    "Se guardaron correctamente los cambios de la verdura",
                                    true,
                                    false,
                                    {
                                        val intent = Intent()
                                        setResult(Activity.RESULT_OK, intent)
                                        finish()
                                    },
                                    {})
                            } else {
                                DialogHelper.dialogo(
                                    this@EditarVerduraActivity,
                                    "Error", v.error!!,
                                    true, false, {}, {}
                                )
                            }
                        }

                    } else {
                        DialogHelper.dialogo(
                            this@EditarVerduraActivity,
                            "Error",
                            "Todos los campos deben estar completos",
                            true,false, {}, {})
                    }
                },
                {})

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